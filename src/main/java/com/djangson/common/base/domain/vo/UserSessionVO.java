package com.djangson.common.base.domain.vo;

import com.djangson.common.annotation.FieldDesc;
import com.djangson.common.util.JsonUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @Description: 用户SessionVO
 * @Author: wangqinjun@vichain.com
 * @Date: 2018/09/20 16:58
 */
@Getter
@Setter
@Accessors(chain=true)
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

    @Override
    public String toString() {
        return JsonUtil.toJsonString(this);
    }
}
