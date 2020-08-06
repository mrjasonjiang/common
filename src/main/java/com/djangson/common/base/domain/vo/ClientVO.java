package com.djangson.common.base.domain.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @Description:
 * @Author: wangqinjun@vichain.com
 * @Date: 2020/4/27 21:11
 */
@Getter
@Setter
@Accessors(chain=true)
public class ClientVO implements Cloneable {

    private String clientCode;
    private String clientName;
    private Long sessionDuration;
    private Integer privilegeLevel;

    @Override
    public ClientVO clone() {
        try {
            return (ClientVO) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
