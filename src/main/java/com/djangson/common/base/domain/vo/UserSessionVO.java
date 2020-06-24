package com.djangson.common.base.domain.vo;

import com.djangson.common.annotation.FieldDesc;
import com.djangson.common.util.JsonUtil;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @Description: 用户SessionVO
 * @Author: wangqinjun@vichain.com
 * @Date: 2018/09/20 16:58
 */
public class UserSessionVO {

    @FieldDesc("客户端编码")
    private String client;

    @FieldDesc("会话令牌")
    private String token;

    @FieldDesc("用户主键")
    private Long userId;

    @FieldDesc("用户账号")
    private String account;

    @FieldDesc("用户姓名")
    private String userName;

    @FieldDesc("用户邮箱")
    private String email;

    @FieldDesc("用户手机号")
    private String phone;

    @FieldDesc("用户头像")
    private Long avatarId;

    @FieldDesc("用户头像校验码")
    private String avatarCode;

    @FieldDesc("当前登录企业")
    private Long currentEntId;

    @FieldDesc("当前所选语言")
    private String currentLanguage; // 当前语言

    @FieldDesc("是否激活")
    private Integer isActive; // 是否激活

    @FieldDesc("是否启用")
    private Integer isEnable; // 用户有效状态，0：禁止登录，1：正常

    @FieldDesc("用户企业列表")
    private List<Long> enterpriseList;

    @FieldDesc("用户角色列表")
    private List<Long> roleList;

    @FieldDesc("用户即时通信登录服务器地址")
    private InetSocketAddress clientAddress;

    public String getClient() {
        return client;
    }

    public UserSessionVO setClient(String client) {
        this.client = client;
        return this;
    }

    public String getToken() {
        return token;
    }

    public UserSessionVO setToken(String token) {
        this.token = token;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public UserSessionVO setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public String getAccount() {
        return account;
    }

    public UserSessionVO setAccount(String account) {
        this.account = account;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public UserSessionVO setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserSessionVO setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public UserSessionVO setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public Long getAvatarId() {
        return avatarId;
    }

    public UserSessionVO setAvatarId(Long avatarId) {
        this.avatarId = avatarId;
        return this;
    }

    public String getAvatarCode() {
        return avatarCode;
    }

    public UserSessionVO setAvatarCode(String avatarCode) {
        this.avatarCode = avatarCode;
        return this;
    }

    public Long getCurrentEntId() {
        return currentEntId;
    }

    public UserSessionVO setCurrentEntId(Long currentEntId) {
        this.currentEntId = currentEntId;
        return this;
    }

    public String getCurrentLanguage() {
        return currentLanguage;
    }

    public UserSessionVO setCurrentLanguage(String currentLanguage) {
        this.currentLanguage = currentLanguage;
        return this;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public UserSessionVO setIsActive(Integer isActive) {
        this.isActive = isActive;
        return this;
    }

    public Integer getIsEnable() {
        return isEnable;
    }

    public UserSessionVO setIsEnable(Integer isEnable) {
        this.isEnable = isEnable;
        return this;
    }

    public List<Long> getEnterpriseList() {
        return enterpriseList;
    }

    public UserSessionVO setEnterpriseList(List<Long> enterpriseList) {
        this.enterpriseList = enterpriseList;
        return this;
    }

    public List<Long> getRoleList() {
        return roleList;
    }

    public UserSessionVO setRoleList(List<Long> roleList) {
        this.roleList = roleList;
        return this;
    }

    public InetSocketAddress getClientAddress() {
        return clientAddress;
    }

    public UserSessionVO setClientAddress(InetSocketAddress clientAddress) {
        this.clientAddress = clientAddress;
        return this;
    }

    @Override
    public String toString() {
        return JsonUtil.toJsonString(this);
    }
}
