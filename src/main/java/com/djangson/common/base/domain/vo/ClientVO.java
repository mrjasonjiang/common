package com.djangson.common.base.domain.vo;

/**
 * @Description:
 * @Author: wangqinjun@vichain.com
 * @Date: 2020/4/27 21:11
 */
public class ClientVO implements Cloneable {

    private String clientCode;
    private String clientName;
    private Long sessionDuration;
    private Integer privilegeLevel;

    public String getClientCode() {
        return clientCode;
    }

    public ClientVO setClientCode(String clientCode) {
        this.clientCode = clientCode;
        return this;
    }

    public String getClientName() {
        return clientName;
    }

    public ClientVO setClientName(String clientName) {
        this.clientName = clientName;
        return this;
    }

    public Long getSessionDuration() {
        return sessionDuration;
    }

    public ClientVO setSessionDuration(Long sessionDuration) {
        this.sessionDuration = sessionDuration;
        return this;
    }

    public Integer getPrivilegeLevel() {
        return privilegeLevel;
    }

    public ClientVO setPrivilegeLevel(Integer privilegeLevel) {
        this.privilegeLevel = privilegeLevel;
        return this;
    }

    @Override
    public ClientVO clone() {
        try {
            return (ClientVO) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
