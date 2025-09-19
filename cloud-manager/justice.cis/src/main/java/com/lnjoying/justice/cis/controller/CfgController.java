package com.lnjoying.justice.cis.controller;

import com.lnjoying.justice.cis.controller.dto.response.DockerContainerInfo;
import com.lnjoying.justice.cis.domain.dto.req.UpdateContainersCfgReq;
import com.lnjoying.justice.cis.domain.model.ConfigInfo;
import com.lnjoying.justice.cis.service.CisManagerService;
import com.lnjoying.justice.commonweb.controller.RestWebController;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static com.lnjoying.justice.cis.domain.model.ConfigInfo.CONFIG_PREFIX;
import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.getUserId;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isAdmin;
import static com.lnjoying.justice.schema.common.ErrorCode.PARSE_CFG_ERROR;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/10/25 19:32
 */

@Controller
@RestSchema(schemaId = "cfg-manager")
@RequestMapping("/cis/v1/docker/cfgs")
@Api(value = "Cfg Controller",tags = {"Cfg Controller"})
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "container_cfg"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "container_cfgs"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "容器配置"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "tbl_cfgdata_container_info")})})
public class CfgController extends RestWebController
{

    private static Logger LOGGER = LogManager.getLogger(CfgController.class);

    @Autowired
    private CisManagerService cisManagerService;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get cfg", response = Object.class)
    @LNJoyAuditLog(value = "get cfg",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.QUERY_STRING,
                            bindParameterName = "userId"))
    public ResponseEntity<List<DockerContainerInfo>> getContainersByCfg(@ApiParam(value = "user_id", required = true, name = "user_id") @RequestParam(value="user_id") String userId,
                                                             @ApiParam(value = "data_id", required = true, name = "data_id") @RequestParam(value="data_id") String dataId,
                                                             @ApiParam(value = "group", required = true, name = "group") @RequestParam(value="group") String group,
                                                             @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        try
        {

            LOGGER.info("get container inst list, user_id: {}, data_id: {}, group: {}", userId, dataId, group);
            if (!isAdmin())
            {
                userId = getUserId();
            }
            return ResponseEntity.ok().body(cisManagerService.getContainersByCfg(userId, group, dataId));
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update cfg", response = Object.class)
    public ResponseEntity<Object> updateContainersCfg(@ApiParam(value = "", required = true, name = "updateContainersCfgReq") @RequestBody UpdateContainersCfgReq updateContainersCfgReq,
                                                                        @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        try
        {

            LOGGER.info("update containers cfg , req: {}", updateContainersCfgReq);
            ConfigInfo configInfo = ConfigInfo.parseCfg(updateContainersCfgReq.getCfg().substring(CONFIG_PREFIX.length()));
            if (Objects.isNull(configInfo))
            {
                throw new WebSystemException(PARSE_CFG_ERROR, ErrorLevel.ERROR);
            }

            if (!isAdmin())
            {
                configInfo.setUserId(getUserId());
            }

            cisManagerService.updateContainersCfg(configInfo, updateContainersCfgReq);
            return ResponseEntity.ok().body(null);
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }

    }


}
