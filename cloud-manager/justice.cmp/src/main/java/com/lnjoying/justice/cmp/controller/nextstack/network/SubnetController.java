package com.lnjoying.justice.cmp.controller.nextstack.network;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.config.DescriptionConfig;
import com.lnjoying.justice.cmp.db.model.TblCmpSubnetKey;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.baremetal.CommonReq;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.network.CreateSubnetReq;
import com.lnjoying.justice.cmp.entity.search.nextstack.network.SubnetSearchCritical;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.BaseRsp;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.network.*;
import com.lnjoying.justice.cmp.handler.actiondescription.i18n.zh_cn.CmpNextStackActionDescriptionTemplate;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.nextstack.network.NetworkService;
import com.lnjoying.justice.commonweb.controller.RestWebController;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isAdmin;

@RestSchema(schemaId = "subnet")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/api/network/v1")
@Api(value = "Subnet Controller",tags = "Subnet Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
                properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "ns_subnet"),
                        @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "ns_subnets"),
                        @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "子网"),
                        @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpSubnet")})})
public class SubnetController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    private static final String REG_UUID = "(EE_)?[0-9a-f]{32}";

    @Autowired
    private NetworkService networkService;

    @Autowired
    private CloudService cloudService;

    @GetMapping(value = "/subnets", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get subnets", response = Object.class, notes = DescriptionConfig.GET_SUBNETS)
    public Object getSubnets(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                             @ApiParam(name = "name") @RequestParam(required = false) String name,
                             @ApiParam(name = "page_size") @RequestParam(required = false, value = "page_size") Integer pageSize,
                             @ApiParam(name = "page_num") @RequestParam(required = false, value = "page_num") Integer pageNum,
                             @ApiParam(name = "vpc_id") @RequestParam(required = false, value = "vpc_id") String vpcId,
                             @ApiParam(name = "subnet_phase") @RequestParam(required = false,value = "subnet_phase") Integer subnetPhase,
                             @ApiParam(name = "user_id") @RequestParam(required = false, value = "user_id") String userId,
                             @RequestHeader(name = UserHeadInfo.USERID, required = false) String operUserId) throws WebSystemException
    {
        try
        {
            LOGGER.info("get subnets, cloud:{}", cloudId);

            String filterUserId = operUserId;
            if (isAdmin() || cloudService.isOwner(cloudId, operUserId))
            {
                filterUserId = userId;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            SubnetSearchCritical pageSearchCritical = new SubnetSearchCritical();
            pageSearchCritical.setName(name);
            pageSearchCritical.setVpcId(vpcId);
            if (pageNum != null) pageSearchCritical.setPageNum(pageNum);
            if (pageSize != null) pageSearchCritical.setPageSize(pageSize);
            if (subnetPhase != null) pageSearchCritical.setPhaseStatus(subnetPhase);

            return networkService.getSubnets(cloudId, pageSearchCritical, filterUserId);
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/subnets/{subnetId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get subnet detail info", response = SubnetDetailInfoRspVo.class, notes = DescriptionConfig.GET_SUBNET)
    public Object getSubnet(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                            @ApiParam(value = "subnetId", required = true, name = "subnetId")
                            @PathVariable("subnetId") @Pattern(regexp = REG_UUID) String subnetId,
                            @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.info("get subnet detail info, cloudId:{}, subnetId:{}", cloudId, subnetId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            return networkService.getSubnet(cloudId, subnetId, operUserId);
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }

    @PostMapping(value = "/subnets", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "create subnet", response = BaseRsp.class, notes = DescriptionConfig.POST_SUBNET)
    @LNJoyAuditLog(value = "create subnet",
            resourceMapperId = CmpResources.NS_SUBNET,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.ADD_SUBNET,
            associatedResourceCnName = "子网",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                    resourceIdParseSPEL = "#obj?.body?.get('subnetId')?.toString()", resourcePrimaryKeyClass = TblCmpSubnetKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpSubnetKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> postSubnet(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                             @ApiParam(value = "CreateSubnetReq", required = true, name = "CreateSubnetReq") @RequestBody @Valid CreateSubnetReq subnet,
                                             @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                             @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.info("create subnet, cloud:{}, request:{}", cloudId, subnet);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return networkService.createSubnet(cloudId, subnet, bpId, userId);
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }

    @DeleteMapping(value = "/subnets/{subnetId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "remove subnet", response = BaseRsp.class, notes = DescriptionConfig.DEL_SUBNET)
    @LNJoyAuditLog(value = "remove subnet",
            resourceMapperId = CmpResources.NS_SUBNET,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.DELETE_SUBNET,
            associatedResourceCnName = "子网",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "subnetId", resourcePrimaryKeyClass = TblCmpSubnetKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpSubnetKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> removeSubnet(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                               @ApiParam(value = "subnetId", required = true, name = "subnetId") @PathVariable("subnetId") @Pattern(regexp = REG_UUID) String subnetId,
                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.info("remove subnet, cloudId:{}, subnetId: {}", cloudId, subnetId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return networkService.removeSubnet(cloudId, subnetId, operUserId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/subnets/{subnetId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update subnet", response = BaseRsp.class, notes = DescriptionConfig.UPDATE_SUBNET)
    @LNJoyAuditLog(value = "update subnet",
            resourceMapperId = CmpResources.NS_SUBNET,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.UPDATE_SUBNET,
            associatedResourceCnName = "子网",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "subnetId", resourcePrimaryKeyClass = TblCmpSubnetKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpSubnetKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> updateSubnet(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                               @ApiParam(value = "subnetId", required = true, name = "subnetId") @PathVariable("subnetId") @Pattern(regexp = REG_UUID) String subnetId,
                                               @ApiParam(value = "CommonReq", required = true, name = "CommonReq") @RequestBody @Valid CommonReq request,
                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.debug("update subnet, cloud:{}, subnetId: {}", cloudId, subnetId);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
                networkService.checkSubnetUser(cloudId, subnetId, userId);
            }

            return cloudService.sendHttpRequestToCloud(cloudId);
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/subnet_count", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get subnet count", response = Object.class)
    public ResponseEntity<Long> getSubnetCount(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get subnet count, cloud:{}", cloudId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            return ResponseEntity.ok(networkService.getSubnetCount(cloudId, operUserId));
        }
        catch (Exception e)
        {
            LOGGER.error("get subnet count failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/vips/{subnetId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get vip list", response = Object.class)
    public ResponseEntity<List<String>> getVips(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                @ApiParam(value = "subnetId", required = true, name = "subnetId") @PathVariable("subnetId")String subnetId,
                                                @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get vip list, cloud:{}, subnetId:{}", cloudId, subnetId);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, true);
                networkService.checkSubnetUser(cloudId, subnetId, userId);
            }

            return ResponseEntity.ok(networkService.getVips(cloudId, subnetId));
        }
        catch (Exception e)
        {
            LOGGER.error("get vips failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }
}
