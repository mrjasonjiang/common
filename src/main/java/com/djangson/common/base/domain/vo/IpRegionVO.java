package com.djangson.common.base.domain.vo;

import com.djangson.common.annotation.FieldDesc;
import com.djangson.common.util.JsonUtil;

/**
 * @Description: IP地址VO
 * @Author: wangqinjun@vichain.com
 * @Date: 2018/09/20 16:58
 */
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

    public String getIpv4() {
        return ipv4;
    }

    public IpRegionVO setIpv4(String ipv4) {
        this.ipv4 = ipv4;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public IpRegionVO setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getRegion() {
        return region;
    }

    public IpRegionVO setRegion(String region) {
        this.region = region;
        return this;
    }

    public String getCity() {
        return city;
    }

    public IpRegionVO setCity(String city) {
        this.city = city;
        return this;
    }

    public String getIsp() {
        return isp;
    }

    public IpRegionVO setIsp(String isp) {
        this.isp = isp;
        return this;
    }

    @Override
    public String toString() {
        return JsonUtil.toJsonString(this);
    }
}
