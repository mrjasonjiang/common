//package com.djangson.common.config;
//
//import com.djangson.common.constant.ClientConstants;
//import com.vichain.common.base.domain.vo.ClientVO;
//import com.vichain.common.enums.SystemPrivilegeLevel;
//import com.vichain.common.util.ClientUtil;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @Description:
// * @Auther: wangqinjun@vichain.com
// * @Date: 2020/4/26 14:03
// */
//@Configuration
//public class ClientConfigure {
//
//    @Bean
//    public ClientUtil.ClientConfig clientConfig() {
//        List<ClientVO> clientVOList = new ArrayList<>();
//        clientVOList.add(new ClientVO().setClientCode(ClientConstants.CLIENT_BMS).setClientName("惠展后台管理系统").setPrivilegeLevel(SystemPrivilegeLevel.NO_LIMITED.getCode()).setSessionDuration(30 * 60L));
//        clientVOList.add(new ClientVO().setClientCode(ClientConstants.CLIENT_PLATFORM).setClientName("惠展saas业务系统").setPrivilegeLevel(SystemPrivilegeLevel.LIMITED.getCode()).setSessionDuration(30 * 60L));
//        clientVOList.add(new ClientVO().setClientCode(ClientConstants.HUIZHAN_MA).setClientName("惠展小程序系统").setPrivilegeLevel(SystemPrivilegeLevel.NO_LIMITED.getCode()).setSessionDuration(30 * 60L));
//        return new ClientUtil.ClientConfig(clientVOList);
//    }
//}
