/*
 * FileName：WxUtil.java
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
package com.jeeplus.modules.weixin.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.weixin.entity.WxAccount;
import com.jeeplus.modules.weixin.entity.WxMenu;
import com.jeeplus.modules.weixin.entity.WxMsgNews;
import com.jeeplus.modules.weixin.entity.WxMsgText;
import com.jeeplus.modules.weixin.mapper.WxMsgNewsMapper;
import com.jeeplus.modules.weixin.mapper.WxMsgTextMapper;
import com.jeeplus.wxapi.process.MsgType;
import com.jeeplus.wxapi.util.Constants;
import com.jeeplus.wxapi.vo.Matchrule;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WxUtil {

    private static WxMsgTextMapper wxMsgTextMapper = SpringContextHolder.getBean(WxMsgTextMapper.class);

    private static WxMsgNewsMapper wxMsgNewsMapper = SpringContextHolder.getBean(WxMsgNewsMapper.class);

    /**
     * 获取微信公众账号的菜单
     *
     * @param menus     菜单列表
     * @param matchrule 个性化菜单配置
     * @return
     */
    public static JSONObject prepareMenus(List<WxMenu> menus, Matchrule matchrule) {
        JSONObject root = new JSONObject();
        if (!CollectionUtils.isEmpty(menus)) {
            List<WxMenu> parentAM = new ArrayList<WxMenu>();
            Map<Long, List<JSONObject>> subAm = new HashMap<Long, List<JSONObject>>();
            for (WxMenu m : menus) {
                if (m.getParentId().intValue() == 0) {//一级菜单
                    parentAM.add(m);
                } else {//二级菜单
                    if (subAm.get(m.getParentId()) == null) {
                        subAm.put(m.getParentId(), new ArrayList<JSONObject>());
                    }
                    List<JSONObject> tmpMenus = subAm.get(m.getParentId());
                    tmpMenus.add(getMenuJSONObj(m));
                    subAm.put(m.getParentId(), tmpMenus);
                }
            }
            JSONArray arr = new JSONArray();
            for (WxMenu m : parentAM) {
                if (subAm.get(m.getId()) != null) {//有子菜单
                    arr.add(getParentMenuJSONObj(m, subAm.get(m.getId())));
                } else {//没有子菜单
                    arr.add(getMenuJSONObj(m));
                }
            }
            root.put("button", arr);
            root.put("matchrule", JSONObject.toJSON(matchrule).toString());
        }
        //添加消息id
        root.put("msgs", getMsg());
        return root;
    }

    /**
     * 此方法是构建菜单对象的；构建菜单时，对于  key 的值可以任意定义；
     * 当用户点击菜单时，会把key传递回来；对已处理就可以了
     *
     * @param menu
     * @return
     */
    public static JSONObject getMenuJSONObj(WxMenu menu) {
        JSONObject obj = new JSONObject();
        obj.put("name", menu.getTitle());
        obj.put("type", menu.getMtype());
        if (Constants.MENU_NEED_KEY.contains(menu.getMtype())) {//事件菜单
            if ("fix".equals(menu.getEventType())) {//fix 消息
                obj.put("key", "_fix_" + menu.getMsgId());//以 _fix_ 开头
            } else {
                if (StringUtils.isEmpty(menu.getInputCode())) {//如果inputcode 为空，默认设置为 subscribe，以免创建菜单失败
                    obj.put("key", "subscribe");
                } else {
                    obj.put("key", menu.getInputCode());
                }
            }
            //存msgtype id
            obj.put("msgType", menu.getMsgType());
            obj.put("msgId", menu.getMsgId());//
        } else {//链接菜单-view
            obj.put("url", menu.getUrl());
        }
        return obj;
    }

    public static JSONObject getParentMenuJSONObj(WxMenu menu, List<JSONObject> subMenu) {
        JSONObject obj = new JSONObject();
        obj.put("name", menu.getTitle());
        obj.put("sub_button", subMenu);
        return obj;
    }

    /**
     * 获取消息
     *
     * @return
     */
    public static JSONArray getMsg() {
        JSONArray arr = new JSONArray();
        List<WxMsgText> msgTextList = wxMsgTextMapper.findList(new WxMsgText());
        List<WxMsgNews> msgNews = wxMsgNewsMapper.findList(new WxMsgNews());
        if (CollectionUtils.isNotEmpty(msgTextList)) {
            for (WxMsgText text : msgTextList) {
                JSONObject obj = new JSONObject();
                obj.put("id", text.getBaseId());
                obj.put("title", text.getTitle());
                obj.put("type", MsgType.Text.toString());
                arr.add(obj);
            }
        }
        if (CollectionUtils.isNotEmpty(msgNews)) {
            for (WxMsgNews news : msgNews) {
                JSONObject obj = new JSONObject();
                obj.put("id", news.getBaseId());
                obj.put("title", news.getTitle());
                obj.put("type", MsgType.News.toString());
                arr.add(obj);
            }
        }
        return arr;
    }

    /**
     * 构造当前菜单数据
     *
     * @param list
     * @return
     */
    public static JSONObject getAccount(List<WxAccount> list, String curentName) {
        JSONObject obj = new JSONObject();
        obj.put("correct", curentName);
        JSONArray arr = new JSONArray();
        if (CollectionUtils.isNotEmpty(list)) {
            for (WxAccount account : list) {
                JSONObject objAccount = new JSONObject();
                objAccount.put("id", account.getId());
                objAccount.put("name", account.getName());
                arr.add(objAccount);
            }
        }
        obj.put("list", arr);
        return obj;
    }

    /**
     * 判断是否微信返回错误
     *
     * @param jsonObject
     * @return
     */
    public static boolean isWxError(JSONObject jsonObject) {
        if (null == jsonObject || jsonObject.getIntValue("errcode") != 0) {
            return true;
        }
        return false;
    }
}
