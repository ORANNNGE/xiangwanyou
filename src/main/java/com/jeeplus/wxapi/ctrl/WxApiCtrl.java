/*
 * FileName：WxApiCtrl.java
 * <p>
 * Copyright (c) 2017-2020, <a href="http://www.webcsn.com">hermit (794890569@qq.com)</a>.
 * <p>
 * Licensed under the GNU General Public License, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/gpl-3.0.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.jeeplus.wxapi.ctrl;

import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.weixin.entity.*;
import com.jeeplus.modules.weixin.mapper.WxFansMapper;
import com.jeeplus.modules.weixin.mapper.WxMsgBaseMapper;
import com.jeeplus.modules.weixin.mapper.WxMsgNewsMapper;
import com.jeeplus.modules.weixin.mapper.WxMsgTextMapper;
import com.jeeplus.modules.weixin.util.SignUtil;
import com.jeeplus.modules.weixin.util.WxUtil;
import com.jeeplus.wxapi.exception.WxErrorException;
import com.jeeplus.wxapi.process.MsgType;
import com.jeeplus.wxapi.process.MsgXmlUtil;
import com.jeeplus.wxapi.process.WxApiClient;
import com.jeeplus.wxapi.process.WxMessageBuilder;
import com.jeeplus.wxapi.vo.Matchrule;
import com.jeeplus.wxapi.vo.MsgRequest;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * 微信与开发者服务器交互接口
 */
@Controller
@RequestMapping("/wxapi")
public class WxApiCtrl extends BaseController {

    private static Logger log = LogManager.getLogger(WxApiCtrl.class);
    @Autowired
    private WxMsgBaseMapper msgBaseDao;

    @Autowired
    private WxMsgNewsMapper msgNewsDao;
    @Autowired
    private WxFansMapper fansDao;
    @Autowired
    private WxMsgTextMapper msgTextDao;


    /**
     * GET请求：进行URL、Tocken 认证；
     * 1. 将token、timestamp、nonce三个参数进行字典序排序
     * 2. 将三个参数字符串拼接成一个字符串进行sha1加密
     * 3. 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
     */
    @ResponseBody
    @RequestMapping(value = "/{account}/message", method = RequestMethod.GET)
    public String doGet(HttpServletRequest request, @PathVariable String account) {
        //如果是多账号，根据url中的account参数获取对应的MpAccount处理即可

        Set<String> keySet = request.getParameterMap().keySet();
        Iterator<String> iterator = keySet.iterator();
        while (iterator.hasNext()) {
            //如果存在，则调用next实现迭代  
            String key = iterator.next();
            log.info("key: " + key + " value: " + request.getParameterMap().get(key));
        }

        WxAccount mpAccount = (WxAccount) CacheUtils.get("WxAccount");
        if (mpAccount != null) {
            String token = mpAccount.getToken();//获取token，进行验证；
            String signature = request.getParameter("signature");// 微信加密签名
            String timestamp = request.getParameter("timestamp");// 时间戳
            String nonce = request.getParameter("nonce");// 随机数
            String echostr = request.getParameter("echostr");// 随机字符串

            // 校验成功返回  echostr，成功成为开发者；否则返回error，接入失败
            if (SignUtil.validSign(signature, token, timestamp, nonce)) {
                return echostr;
            }
        }
        return "error";
    }

    /**
     * POST 请求：进行消息处理；
     */
    @RequestMapping(value = "/{account}/message", method = RequestMethod.POST)
    public @ResponseBody
    String doPost(HttpServletRequest request, @PathVariable String account, HttpServletResponse response) {
        //处理用户和微信公众账号交互消息
        WxAccount mpAccount = (WxAccount) CacheUtils.get("WxAccount");
        try {
            MsgRequest msgRequest = MsgXmlUtil.parseXml(request);//获取发送的消息
            processMsg(msgRequest, mpAccount);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
            return "error";
        }
        return null;
    }

    /**
     * 处理消息
     * 开发者可以根据用户发送的消息和自己的业务，自行返回合适的消息；
     *
     * @param msgRequest : 接收到的消息
     * @param mpAccount  ： appId
     */
    public String processMsg(MsgRequest msgRequest, WxAccount mpAccount) throws WxErrorException {
        String msgtype = msgRequest.getMsgType();// 接收到的消息类型
        String respXml = null;// 返回的内容；
        if (msgtype.equals(MsgType.Text.toString())) {
            /**
             * 文本消息，一般公众号接收到的都是此类型消息
             */
            respXml = this.processTextMsg(msgRequest, mpAccount);
        } else if (msgtype.equals(MsgType.Event.toString())) {// 事件消息
            /**
             * 用户订阅公众账号、点击菜单按钮的时候，会触发事件消息
             */
            respXml = this.processEventMsg(msgRequest, mpAccount);

            // 其他消息类型，开发者自行处理
        } else if (msgtype.equals(MsgType.Image.toString())) {// 图片消息

        } else if (msgtype.equals(MsgType.Location.toString())) {// 地理位置消息

        }

        // 如果没有对应的消息，默认返回订阅消息；
        if (StringUtils.isEmpty(respXml)) {

            WxMsgText text = msgTextDao.getMsgTextByInputCode(MsgType.SUBSCRIBE.toString());
            if (text != null) {
                respXml = MsgXmlUtil.textToXml(WxMessageBuilder.getMsgResponseText(msgRequest, text));
            }
        }
        return respXml;
    }

    // 处理文本消息
    private String processTextMsg(MsgRequest msgRequest, WxAccount mpAccount) {
        String content = msgRequest.getContent();
        if (!StringUtils.isEmpty(content)) {// 文本消息
            String tmpContent = content.trim();
            List<WxMsgNews> msgNews = msgNewsDao.getRandomMsgByContent(tmpContent, mpAccount.getMsgCount());
            if (!CollectionUtils.isEmpty(msgNews)) {
                return MsgXmlUtil.newsToXml(WxMessageBuilder.getMsgResponseNews(msgRequest, msgNews));
            }
        }
        return null;
    }

    // 处理事件消息
    private String processEventMsg(MsgRequest msgRequest, WxAccount mpAccount) throws WxErrorException {
        String key = msgRequest.getEventKey();
        if (MsgType.SUBSCRIBE.toString().equals(msgRequest.getEvent())) {// 订阅消息
            logger.info("关注者openId----------" + msgRequest.getFromUserName());
            String openId = msgRequest.getFromUserName();
            WxFans fans = WxApiClient.syncAccountFans(openId, mpAccount);
            // 用户关注微信公众号后更新粉丝表
            if (null != fans) {
                WxFans tmpFans = fansDao.getByOpenId(openId);
                if (tmpFans == null) {
                    fans.setAccount(mpAccount.getAccount());
                    fans.preInsert();
                    fansDao.insert(fans);
                } else {
                    fans.setId(tmpFans.getId());
                    fans.preUpdate();
                    fansDao.update(fans);
                }
            }
            WxMsgText text = msgTextDao.getMsgTextBySubscribe();
            if (text != null) {
                return MsgXmlUtil.textToXml(WxMessageBuilder.getMsgResponseText(msgRequest, text));
            }
        } else if (MsgType.UNSUBSCRIBE.toString().equals(msgRequest.getEvent())) {// 取消订阅消息
            WxMsgText text = msgTextDao.getMsgTextByInputCode(MsgType.UNSUBSCRIBE.toString());
            if (text != null) {
                return MsgXmlUtil.textToXml(WxMessageBuilder.getMsgResponseText(msgRequest, text));
            }
        } else {// 点击事件消息
            if (!StringUtils.isEmpty(key)) {
                /**
                 * 固定消息
                 * _fix_ ：在我们创建菜单的时候，做了限制，对应的event_key 加了 _fix_
                 *
                 * 当然开发者也可以进行修改
                 */
                if (key.startsWith("_fix_")) {
                    String baseIds = key.substring("_fix_".length());
                    if (!StringUtils.isEmpty(baseIds)) {
                        String[] idArr = baseIds.split(",");
                        if (idArr.length > 1) {// 多条图文消息
                            List<WxMsgNews> msgNews = msgNewsDao.listMsgNewsByBaseId(idArr);
                            if (msgNews != null && msgNews.size() > 0) {
                                return MsgXmlUtil.newsToXml(WxMessageBuilder.getMsgResponseNews(msgRequest, msgNews));
                            }
                        } else {// 图文消息，或者文本消息
                            WxMsgBase msg = msgBaseDao.get(baseIds);
                            if (msg.getMsgType().equals(MsgType.Text.toString())) {
                                WxMsgText text = msgTextDao.getMsgTextByBaseId(baseIds);
                                if (text != null) {
                                    return MsgXmlUtil.textToXml(WxMessageBuilder.getMsgResponseText(msgRequest, text));
                                }
                            } else {
                                List<WxMsgNews> msgNews = msgNewsDao.listMsgNewsByBaseId(idArr);
                                if (msgNews != null && msgNews.size() > 0) {
                                    return MsgXmlUtil
                                            .newsToXml(WxMessageBuilder.getMsgResponseNews(msgRequest, msgNews));
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    // 发布菜单
    public JSONObject publishMenu(WxAccount mpAccount) throws WxErrorException {
        // 获取数据库菜单
        List<WxMenu> menus = null; //menuDao.listWxMenus(new WxMenu());
        Matchrule matchrule = new Matchrule();
        String menuJson = JSONObject.toJSONString(WxUtil.prepareMenus(menus, matchrule));
        logger.info("创建菜单传参如下:" + menuJson);
        JSONObject rstObj = WxApiClient.publishMenus(menuJson, mpAccount);// 创建普通菜单
        logger.info("创建菜单返回消息如下:" + rstObj.toString());
        // 以下为创建个性化菜单demo，只为男创建菜单；其他个性化需求 设置 Matchrule 属性即可
        // matchrule.setSex("1");//1-男 ；2-女
        // JSONObject rstObj = WxApiClient.publishAddconditionalMenus(menuJson,mpAccount);//创建个性化菜单

        // if(rstObj != null){//成功，更新菜单组
        // if(rstObj.containsKey("menu_id")){
        // menuGroupDao.updateMenuGroupDisable();
        // menuGroupDao.updateMenuGroupEnable(gid);
        // }else if(rstObj.containsKey("errcode") && rstObj.getInt("errcode") == 0){
        // menuGroupDao.updateMenuGroupDisable();
        // menuGroupDao.updateMenuGroupEnable(gid);
        // }
        // }
        return rstObj;
    }

    // 删除菜单
    public JSONObject deleteMenu(WxAccount mpAccount) throws WxErrorException {
        JSONObject rstObj = WxApiClient.deleteMenu(mpAccount);
        if (rstObj != null && rstObj.getIntValue("errcode") == 0) {// 成功，更新菜单组
            //menuGroupDao.updateMenuGroupDisable();
        }
        return rstObj;
    }
}
