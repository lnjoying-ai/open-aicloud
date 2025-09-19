package com.lnjoying.justice.sys.controller;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.constant.ResourceOperationTypeConstants;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import com.lnjoying.justice.sys.domain.dto.req.AddConfigReq;
import com.lnjoying.justice.sys.facade.NacosService;
import com.lnjoying.justice.sys.handler.actiondescription.i18n.zh_cn.ConfigActionDescriptionTemplate;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Extension;
import io.swagger.annotations.ExtensionProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Properties;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isBpAdmin;
import static com.lnjoying.justice.schema.common.ErrorCode.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/5/27 11:59
 */

@Controller
@RestSchema(schemaId = "ConfigController")
@RequestMapping(path = "/sys/v1/configs")
@Slf4j
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "config"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "configs"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "系统配置"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "")})})
public class ConfigController
{
    @NacosInjected
    private ConfigService configService;

    @Autowired
    @Qualifier("globalNacosProperties$config")
    private Properties configGlobalNacosPropertiesBeanName;

    @Autowired
    private NacosService nacosService;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get config list")
    public ResponseEntity<Object> getConfigList(@ApiParam(name = "namespace") @RequestParam(value = "namespace", required = false) String namespace,
                                                @ApiParam(name = "group") @RequestParam(value = "group", required = false) String group,
                                                @ApiParam(name = "data_id") @RequestParam(value = "data_id", required = false) String dataId,
                                                @ApiParam(name = "system") @RequestParam(value = "system", required = false) boolean system,
                                                @ApiParam(name = "page_size") @RequestParam(value = "page_size", required = false, defaultValue = Integer.MAX_VALUE + "") Integer pageSize,
                                                @ApiParam(name = "page_num") @RequestParam(value = "page_num", required = false, defaultValue = "1") Integer pageNum)
    {

        Object configList = nacosService.getConfigList(namespace, group, dataId, system, pageNum, pageSize);
        return status(OK).body(configList);
    }


    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "add config", tags = ResourceOperationTypeConstants.RESOURCE_CREATE)
    @LNJoyAuditLog(value = "add config", tags = ResourceOperationTypeConstants.RESOURCE_CREATE,
            actionDescriptionTemplateClass = ConfigActionDescriptionTemplate.class,
            isResourceInstanceNameFromArgs = true,
            resourceInstanceNameFromArgsParseSpEl = "#obj?.get('dataId')?.toString()",
            isDynamicOperationType = true,
            dynamicOperationTypeSpEl = "#obj?.get('id') == null ? '" + ResourceOperationTypeConstants.RESOURCE_CREATE + "': '" + ResourceOperationTypeConstants.RESOURCE_UPDATE + "'"
    )
    public ResponseEntity<String> addConfig(@ApiParam(required = true, name = "configReq") @RequestBody @Valid AddConfigReq configReq,
                                                              @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                              @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                              @RequestHeader(name = UserHeadInfo.USERNAME, required = false) String userName,
                                                              @RequestHeader(name = UserHeadInfo.BpName, required = false) String bpName,
                                                              @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        log.info("add config. req: {}, userId: {}", configReq, userId);

        try
        {
            if(isBpAdmin())
            {
                if(!nacosService.checkNamespaceIdExist(userId))
                {
                    nacosService.createNamespace(userId);
                }

                configReq.setNamespace(userId);
            }

            boolean published = nacosService.publishConfig(configReq);
            if (published)
            {
                return status(CREATED).body(null);
            }
        }
        catch (Exception e)
        {
            log.error("publish config error: {}", e);
        }


        throw new WebSystemException(PUBLISH_CONFIG_ERROR, ErrorLevel.ERROR);
    }


    @GetMapping(value = "/config", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get config")
    public ResponseEntity<Object> getConfig(@ApiParam(required = false, name = "namespace") @RequestParam(required = false, value = "namespace") String namespace,
                                            @ApiParam(required = true, name = "data_id") @RequestParam(required = true, value = "data_id") String dataId,
                                            @ApiParam(required = true, name = "group") @RequestParam(required = true, value = "group") String group)
    {
        Object config = nacosService.getConfig(namespace, group, dataId);
        return status(OK).body(config);
    }


    @DeleteMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete config")
    @LNJoyAuditLog(value = "delete config",
            actionDescriptionTemplate = ConfigActionDescriptionTemplate.Descriptions.DELETE_CONFIG,
            isResourceInstanceNameFromArgs = true,
            resourceInstanceNameFromArgsParseSpEl = "#obj?.get('data_id')?.toString()"
    )
    public ResponseEntity<String> deleteConfig(@ApiParam(required = true, name = "namespace") @RequestParam(required = true, value = "namespace") String namespace,
                                               @ApiParam(required = true, name = "data_id") @RequestParam(required = true, value = "data_id") String dataId,
                                               @ApiParam(required = true, name = "group") @RequestParam(required = true, value = "group") String group)
    {
        log.info("delete config, namespace: {}, group: {}, dataId: {},", dataId, group);

        nacosService.deleteConfig(namespace, dataId, group);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
