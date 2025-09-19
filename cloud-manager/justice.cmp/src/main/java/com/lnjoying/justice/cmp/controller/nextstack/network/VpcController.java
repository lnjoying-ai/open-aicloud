package com.lnjoying.justice.cmp.controller.nextstack.network;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.config.DescriptionConfig;
import com.lnjoying.justice.cmp.db.model.TblCmpVpcKey;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.baremetal.CommonReq;
import com.lnjoying.justice.cmp.domain.info.network.NetSummeryInfo;
import com.lnjoying.justice.cmp.entity.search.nextstack.network.VpcSearchCritical;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.network.*;
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

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isAdmin;

@RestSchema(schemaId = "vpc")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/api/network/v1")
@Api(value = "Vpc Controller",tags = "Vpc Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "ns_vpc"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "ns_vpcs"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "虚拟私有云"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpVpc")})})
public class VpcController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    private static final String REG_UUID = "(EE_)?[0-9a-f]{32}";

    @Autowired
    private NetworkService networkService;

    @Autowired
    private CloudService cloudService;

    @GetMapping(value = "/vpcs", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get vpcs", response = Object.class, notes = DescriptionConfig.GET_VPCS)
    public Object getVpcs(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                          @ApiParam(name = "name") @RequestParam(required = false) String name,
                          @ApiParam(name = "page_size") @RequestParam(required = false, value = "page_size") Integer pageSize,
                          @ApiParam(name = "page_num") @RequestParam(required = false, value = "page_num") Integer pageNum,
                          @ApiParam(name = "vpc_phase") @RequestParam(required = false,value = "vpc_phase") Integer vpcPhase,
                          @ApiParam(name = "user_id") @RequestParam(required = false,value = "user_id") String userId,
                          @RequestHeader(name = UserHeadInfo.USERID, required = false) String operUserId) throws WebSystemException
    {
        try
        {
            LOGGER.info("get vpcs, cloud:{}, name:{}, pageSize:{}, pageNum:{}", cloudId, name, pageSize, pageNum);

            String filterUserId = operUserId;
            if (isAdmin() || cloudService.isOwner(cloudId, operUserId))
            {
                filterUserId = userId;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            VpcSearchCritical pageSearchCritical = new VpcSearchCritical();
            pageSearchCritical.setName(name);
            if (pageNum != null) pageSearchCritical.setPageNum(pageNum);
            if (pageSize != null) pageSearchCritical.setPageSize(pageSize);

            return networkService.getVpcs(cloudId, pageSearchCritical, filterUserId);
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/vpcs/{vpcId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get vpc detail info", response = VpcDetailInfoRspVo.class, notes = DescriptionConfig.GET_VPC)
    public Object getVpc(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                         @ApiParam(value = "vpcId", required = true, name = "vpcId") @PathVariable("vpcId") @Pattern(regexp = REG_UUID) String vpcId,
                         @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.info("get vpc detail info, cloud:{}, vpcId:{}", cloudId, vpcId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            return networkService.getVpc(cloudId, vpcId, operUserId);
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }

    @PostMapping(value = "/vpcs", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "create vpc", response = BaseRsp.class, notes = DescriptionConfig.POST_VPC)
    @LNJoyAuditLog(value = "create vpc",
            resourceMapperId = CmpResources.NS_VPC,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.ADD_VPC,
            associatedResourceCnName = "虚拟私有云",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                    resourceIdParseSPEL = "#obj?.body?.get('vpcId')?.toString()", resourcePrimaryKeyClass = TblCmpVpcKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpVpcKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> postVpc(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                          @ApiParam(value = "CreateVpcReq", required = true, name = "CreateVpcReq") @RequestBody @Valid VpcCreateReqVo vpc,
                                          @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                          @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.info("create vpc, cloud:{}, request:{}", cloudId, vpc);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return networkService.createVpc(cloudId, vpc, bpId, userId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error(e.getMessage());
            throw throwWebException(e);
        }
    }

    @DeleteMapping(value = "/vpcs/{vpcId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "remove vpc", response = BaseRsp.class, notes = DescriptionConfig.DEL_VPC)
    @LNJoyAuditLog(value = "remove vpc",
            resourceMapperId = CmpResources.NS_VPC,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.DELETE_VPC,
            associatedResourceCnName = "虚拟私有云",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "vpcId", resourcePrimaryKeyClass = TblCmpVpcKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpVpcKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> removeVpc(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                            @ApiParam(value = "vpcId", required = true, name = "vpcId") @PathVariable("vpcId") @Pattern(regexp = REG_UUID) String vpcId,
                                            @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.info("remove vpc, cloud:{}, vpcId: {}", cloudId, vpcId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return networkService.removeVpc(cloudId, vpcId, operUserId);
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

    @PutMapping(value = "/vpcs/{vpcId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update vpc", response = BaseRsp.class, notes = DescriptionConfig.UPDATE_VPC)
    @LNJoyAuditLog(value = "update vpc",
            resourceMapperId = CmpResources.NS_VPC,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.UPDATE_VPC,
            associatedResourceCnName = "虚拟私有云",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "vpcId", resourcePrimaryKeyClass = TblCmpVpcKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpVpcKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> updateVpc(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                            @ApiParam(value = "vpcId", required = true, name = "vpcId") @PathVariable("vpcId") @Pattern(regexp = REG_UUID) String vpcId,
                                            @ApiParam(value = "CommonReq", required = true, name = "CommonReq") @RequestBody @Valid CommonReq request,
                                            @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.debug("update vpc, cloud:{}, vpcId: {}", cloudId, vpcId);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
                networkService.checkVpcUser(cloudId, vpcId, userId);
            }

            return cloudService.sendHttpRequestToCloud(cloudId);
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/topology/{vpcId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get topology", response = Object.class, notes = DescriptionConfig.GET_TOPOLOGY)
    public Object getAdminTopology(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                   @ApiParam(value = "vpcId", required = true, name = "vpcId") @PathVariable("vpcId") @Pattern(regexp = REG_UUID) String vpcId,
                                   @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.info("get topology, cloud:{}, vpcId: {}", cloudId, vpcId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            return networkService.getTopology(cloudId, vpcId, operUserId);
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/net_stats", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get net stats", response = Object.class)
    public ResponseEntity<NetSummeryInfo> getNetStats(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                      @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get net stats, cloud:{}", cloudId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            return ResponseEntity.ok(networkService.getNetSummery(cloudId, operUserId));
        }
        catch (Exception e)
        {
            LOGGER.error("get net summery failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/vpc_count", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get vpc count", response = Object.class)
    public ResponseEntity<Long> getVpcCount(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                            @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get vpc count, cloud:{}", cloudId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            return ResponseEntity.ok(networkService.getVpcCount(cloudId, operUserId));
        }
        catch (Exception e)
        {
            LOGGER.error("get vpc count failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }
}
