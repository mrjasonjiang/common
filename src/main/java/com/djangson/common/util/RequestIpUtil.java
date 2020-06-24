package com.djangson.common.util;

import com.djangson.common.base.domain.vo.IpRegionVO;
import com.djangson.common.constant.ErrorConstants;
import com.djangson.common.exception.BusinessException;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.List;

public class RequestIpUtil {

    /**
     * 获取http请求的真实源IP
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {

        // 获取代理层的首次IP 地址
        String ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotBlank(ip) && !ip.equalsIgnoreCase("unknown")) {
            return ip.split(",")[0].trim();
        }

        // 若非代理，则直接获取远程IP
        return request.getRemoteAddr();
    }

    /**
     * 获取http请求的真实源IP
     * @param request
     * @return
     */
    public static String getIpAddress(ServerHttpRequest request) {

        // 获取代理层的首次IP 地址
        List<String> headers = request.getHeaders().get("X-Real-IP");
        String ip = CollectionUtils.isEmpty(headers) ? null : headers.get(0);
        if (StringUtils.isNotBlank(ip) && !ip.equalsIgnoreCase("unknown")) {
            return ip.split(",")[0].trim();
        }

        // 若非代理，则直接获取远程IP
        return request.getRemoteAddress().getAddress().getHostAddress();
    }

    /**
     * 调用外部接口获取IP 地址所属地区
     * @param ip
     * @return
     */
    public static IpRegionVO getIpRegion(String ip) throws IOException {

        // 若IP 格式错误，直接返回
        if (!DataTypeValidateUtil.isIP(ip)) {
            return new IpRegionVO();
        }

        // 创建连接对象：官网：http://ip.taobao.com/
        URLConnection connection = new URL("http://ip.taobao.com/service/getIpInfo.php?ip=" + ip).openConnection();

        // 处理查询结果
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"))) {

            // 读取返回结果（字符串）
            StringBuilder result = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            // 判断读取结果
            JsonNode ipRegionJson = JsonUtil.toJson(result.toString());
            if (ipRegionJson == null || ipRegionJson.get("code").toString().equals("1")) {
                throw new BusinessException("查询失败！", ErrorConstants.OPERATION_FAIL);
            }

            // 转换成IP 对象
            IpRegionVO ipRegionVO = JsonUtil.parseObject(ipRegionJson.get("data").toString(), IpRegionVO.class);
            if (ipRegionVO == null) {
                throw new BusinessException("查询失败！", ErrorConstants.OPERATION_FAIL);
            }

            // 对无效数据过滤
            ipRegionVO.setIpv4(ip);
            ipRegionVO.setCountry(ipRegionVO.getCountry().equalsIgnoreCase("XX") ? null : ipRegionVO.getCountry());
            ipRegionVO.setRegion(ipRegionVO.getRegion().equalsIgnoreCase("XX") ? null : ipRegionVO.getRegion());
            ipRegionVO.setCity(ipRegionVO.getCity().equalsIgnoreCase("XX") ? null : ipRegionVO.getCity());
            ipRegionVO.setIsp(ipRegionVO.getIsp().equalsIgnoreCase("XX") ? null : ipRegionVO.getIsp());
            return ipRegionVO;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 获取本机IP
     * @return
     * @throws Exception
     */
    public static final String getLocalIpV4() throws Exception {
        return getLocalIp(0);
    }

    /**
     * 获取本机IP
     * @return
     * @throws Exception
     */
    public static final String getLocalIpV6() throws Exception {
        return getLocalIp(1);
    }

    /**
     * 获取本机MAC地址
     * @return
     * @throws Exception
     */
    public static String getMacAddress() throws Exception {
        Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        while (allNetInterfaces.hasMoreElements()) {
            NetworkInterface netInterface = allNetInterfaces.nextElement();
            if (netInterface.isLoopback() || netInterface.isVirtual() || netInterface.isPointToPoint() || !netInterface.isUp()) {
                continue;
            }
            byte[] mac = netInterface.getHardwareAddress();
            if (mac != null) {
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < mac.length; i++) {
                    builder.append(String.format("%02X", mac[i])).append("-");
                }
                return builder.deleteCharAt(builder.length() - 1).toString();
            }
        }
        return null;
    }

    /**
     * 获取本机IP
     * @return
     * @throws Exception
     */
    private static String getLocalIp(int ipVersion) throws Exception {
        Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        while (allNetInterfaces.hasMoreElements()) {
            Enumeration<InetAddress> addresses = allNetInterfaces.nextElement().getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress ip = addresses.nextElement();
                if (ipVersion == 0 && ip instanceof Inet4Address && !ip.getHostAddress().contains("127.0.0.1")) {
                    return ip.getHostAddress();
                }
                if (ipVersion == 1 && ip instanceof Inet6Address && !ip.getHostAddress().contains("0:0:0:0:0:0:0:1") && !ip.getHostAddress().contains("::1")) {
                    return ip.getHostAddress();
                }
            }
        }
        return null;
    }
}
