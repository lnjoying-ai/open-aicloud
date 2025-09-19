package com.lnjoying.justice.schema.service.sys;

import com.lnjoying.justice.schema.entity.sys.NodeConfig;
import io.swagger.annotations.ApiParam;
import lombok.*;

import java.io.Serializable;

/**
 * @Description: sys nacos config service
 * @Author: Merak
 * @Date: 2022/5/25 23:23
 */

public interface SysService
{

    NacosConfig getNacos();

    Boolean exists(@ApiParam(name = "namespace") String namespace, @ApiParam(name = "group") String group, @ApiParam(name = "dataId") String dataId);

    ConfigInfoBase getConfig(@ApiParam(name = "namespace") String namespace, @ApiParam(name = "group") String group, @ApiParam(name = "dataId") String dataId);
    
    NodeConfig getNodeConfig();
    @Data
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    final class NacosConfig implements Serializable
    {
        private static final long serialVersionUID = -1293234135874310717L;

        // serverAddr(nacos地址：ip:port), username(登录nacos的用户名), password（登录nacos的密码）, timeout（请求超时时间ms）
        private String serverAddr;

        private String userName;

        private String password;

        private long timeout;
    }

    @Data
    final class ConfigInfoBase implements Serializable
    {

        private String userId;

        private String group;

        private String dataId;

        private String content;

        private String md5;
    }
}
