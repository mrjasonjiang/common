package com.djangson.common.base.domain.vo;

import com.djangson.common.annotation.FieldDesc;
import com.djangson.common.util.JsonUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @Description: IP地址VO
 * @Author: wangqinjun@vichain.com
 * @Date: 2018/09/20 16:58
 */
@Getter
@Setter
@Accessors(chain=true)
public class IpRegionVO {

    @FieldDesc("IPV4地址")
    private String ipv4;

    @FieldDesc("所属国家")
    private String country;

    @FieldDesc("所属省份(地区)")
    private String region;

    @FieldDesc("所属城市")
    private String city;

    @FieldDesc("所属运营商")
    private String isp;

    @Override
    public String toString() {
        return JsonUtil.toJsonString(this);
    }
}
