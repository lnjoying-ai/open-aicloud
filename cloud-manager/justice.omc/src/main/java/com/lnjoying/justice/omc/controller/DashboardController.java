package com.lnjoying.justice.omc.controller;

import com.lnjoying.justice.omc.domain.dto.req.AddIntegrationTaskReq;
import com.lnjoying.justice.omc.domain.dto.rsp.*;
import com.lnjoying.justice.omc.service.CombRpcService;
import com.lnjoying.justice.omc.service.IntegrationTaskService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Extension;
import io.swagger.annotations.ExtensionProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.*;
import static com.lnjoying.justice.schema.service.ecrm.RegionResourceService.NodeStatus.CAN_OPERATE;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/10/25 15:10
 */

@RestSchema(schemaId = "DashboardController")
@RequestMapping(path = "/omc/v1/dashboards")
@Slf4j
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "dashboard"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "dashboards"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "仪表盘"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "")})})
public class DashboardController extends OmcWebController
{

    @Autowired
    private IntegrationTaskService integrationTaskService;

    @Autowired
    private CombRpcService combRpcService;

    @GetMapping(value = "/has-permission", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "has permission")
    public ResponseEntity<Object> hasPermission( @ApiParam(name = "var-bp") @RequestParam(value = "varBp", required = false) String varBp,
                                                 @ApiParam(name = "var-user") @RequestParam(required = false) String varUser,
                                                 @ApiParam(name = "var-site") @RequestParam(required = false) String varSite,
                                                 @ApiParam(name = "var-node_id") @RequestParam(required = false) String varNodeId,
                                                 @ApiParam(name = "var-container_name") @RequestParam(required = false) String varContainerName,
                                                 @ApiParam(name = "var-container_ref_id") @RequestParam(required = false) String varContainerRefId,
                                                 @ApiParam(name = "var-compute_node") @RequestParam(required = false) int computeNodeId,
                                                 @ApiParam(name = "var-vm_user_id") @RequestParam(required = false) String queryUserId,
                                                 @ApiParam(name = "var-vm_instance_id") @RequestParam(required = false) String varNsVmInstanceId,
                                                 @ApiParam(name = "var-cloud_id") @RequestParam(required = false) String varCloudId,
                                                 @ApiParam(name = "var-node") @RequestParam(required = false) String varNsNodeMonitorAddress,
                                                 @ApiParam(name = "dashboard_id") @RequestParam(required = false) String dashboardId )
    {

        log.info("check has permission, varBp: {}, varUser: {}, varSite: {}, varComputeNodeId: {}, queryUserId: {}, dashboardId: {}",  varBp, varUser, varSite, computeNodeId, queryUserId, dashboardId);
        HasPermissionRsp rsp = HasPermissionRsp.builder().hasPermission(false).build();
        boolean canOperate = false;
        if (isAdmin())
        {
           canOperate = true;
        }
        else
        {
            if (StringUtils.hasText(varNodeId))
            {
                try
                {
                    int status =combRpcService.getRegionResourceService().checkOperUser(varNodeId, getBpId(), getUserId());
                    if (CAN_OPERATE.value() == status)
                    {
                        canOperate = true;
                    }
                }
                catch (Exception e)
                {
                   log.error("checkOperUser error", e);
                }
            }
            else if (StringUtils.hasText(varContainerName))
            {
                canOperate = combRpcService.getCcInstService().checkOperUserByContainerName(varContainerName, queryBpId(), queryUserId());

            }
            else if (StringUtils.hasText(varContainerRefId))
            {
                canOperate = combRpcService.getCcInstService().checkOperUserByContainerRefId(varContainerRefId, queryBpId(), queryUserId());

            }
            else if (StringUtils.hasText(varNsVmInstanceId))
            {
                if (StringUtils.hasText(varNsVmInstanceId))
                {
                    canOperate = combRpcService.getCloudManagerService().checkOperUserByVmInstanceId(varCloudId, varNsVmInstanceId, queryBpId(), queryUserId());
                }
                else if (StringUtils.hasText(varNsNodeMonitorAddress))
                {
                    String [] addressArray = StringUtils.split(varNsNodeMonitorAddress, ":");
                    String nodeIp = addressArray[0];
                    canOperate = combRpcService.getCloudManagerService().checkOperUserByHypervisorNodeIp(varCloudId, nodeIp, queryBpId(), queryUserId());
                }
            }
        }

        if (canOperate)
        {
            rsp = HasPermissionRsp.builder().hasPermission(true).build();
        }

        return status(OK).body(rsp);
    }

}
