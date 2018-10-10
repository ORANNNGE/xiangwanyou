/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.weixin.entity;


import com.jeeplus.core.persistence.DataEntity;

/**
 * 微信账号Entity
 *
 * @author toteny
 * @version 2018-06-03
 */
public class WxAccount extends DataEntity<WxAccount> {

    private static final long serialVersionUID = 1L;
    private String name;        // 公众号名称
    private String account;        // 公众号账号
    private String appid;        // appid
    private String appsecret;        // appsecret
    private String url;        // 验证时用的url
    private String token;        // token
    private String qrCode;//二维码
    private Integer msgCount;        // 自动回复消息条数;默认是5条

    public WxAccount() {
        super();
    }

    public WxAccount(String id) {
        super(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppsecret() {
        return appsecret;
    }

    public void setAppsecret(String appsecret) {
        this.appsecret = appsecret;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Integer getMsgCount() {
        return msgCount;
    }

    public void setMsgCount(Integer msgCount) {
        this.msgCount = msgCount;
    }
}