package com.lnjoying.justice.cmp.controller.nextstack.network;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.common.SyncMsg;
import com.lnjoying.justice.cmp.db.model.TblCmpSecurityGroupKey;
import com.lnjoying.justice.cmp.db.model.TblCmpSecurityGroupRuleKey;
import com.lnjoying.justice.cmp.db.model.search.SecurityGroupSearchCritical;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.baremetal.CommonReq;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.network.SecurityGroupBoundUnboundReqVo;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.network.SecurityGroupCreateReqVo;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.network.SgRuleCreateUpdateReqVo;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.network.SgRulesCreateUpdateReqVo;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.network.GetSecurityGroupDetailInfoRsp;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.network.SecurityGroupsRspVo;
import com.lnjoying.justice.cmp.handler.actiondescription.i18n.zh_cn.CmpNextStackActionDescriptionTemplate;
import com.lnjoying.justice.cmp.service.nextstack.network.SgService;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
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

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.commonweb.common.SwaggerConstants.SWAGGER_RESOURCE_MODEL;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isAdmin;

@RestSchema(schemaId = "securityGroup")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/api/network/v1")
@Api(value = "Security Group Controller",tags = "Security Group Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "ns_security_group"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "ns_security_groups"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "安全组"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpSecurityGroup")})})
public class SecurityGroupController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private SgService securityGroupService;

    @Autowired
    private CloudService cloudService;

    @PostMapping(value = "/sgs", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "create securityGroup", response = Object.class)
    @LNJoyAuditLog(value = "create securityGroup",
            resourceMapperId = CmpResources.NS_SECURITY_GROUP,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.ADD_SECURITY_GROUP,
            associatedResourceCnName = "安全组",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                    resourceIdParseSPEL = "#obj?.body?.get('sgId')?.toString()", resourcePrimaryKeyClass = TblCmpSecurityGroupKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpSecurityGroupKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> addSg(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                         @ApiParam(value = "CreateSecurityGroupReq", required = true, name = "CreateSecurityGroupReq") @RequestBody @Valid SecurityGroupCreateReqVo request,
                                         @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                         @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("create security group, cloud:{}, request:{}", cloudId, request);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return securityGroupService.createSecurityGroup(cloudId, request, bpId, userId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add security group failed: {}", e.getMessage());

            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/sgs", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get security groups", response = Object.class)
    public ResponseEntity<SecurityGroupsRspVo> getSgs(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                      @ApiParam(name = "name") @RequestParam(required = false) String name,
                                                      @ApiParam(name = "sg_id") @RequestParam(required = false, value = "sg_id") String sgId,
                                                      @ApiParam(name = "page_size") @RequestParam(required = false, value = "page_size") Integer pageSize,
                                                      @ApiParam(name = "page_num") @RequestParam(required = false, value = "page_num") Integer pageNum,
                                                      @ApiParam(name = "user_id") @RequestParam(required = false, value = "user_id") String userId,
                                                      @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                      @RequestHeader(name = UserHeadInfo.USERID, required = false) String operUserId)
    {
        try
        {
            LOGGER.debug("get security group list, cloud:{}", cloudId);

            String filterUserId = operUserId;
            if (isAdmin() || cloudService.isOwner(cloudId, operUserId))
            {
                filterUserId = userId;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            SecurityGroupSearchCritical pageSearchCritical = new SecurityGroupSearchCritical();
            if(null != pageSize) pageSearchCritical.setPageSize(pageSize);
            if(null != pageNum) pageSearchCritical.setPageNum(pageNum);
            if(null != name) pageSearchCritical.setName(name);
            if(null != sgId) pageSearchCritical.setSgId(sgId);

            SecurityGroupsRspVo getSecurityGroupsRsp = securityGroupService.getSecurityGroups(cloudId, pageSearchCritical, bpId, filterUserId);
            return ResponseEntity.ok(getSecurityGroupsRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get security groups failed: {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/sgs/{sgId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get sg detail info", response = Object.class)
    public ResponseEntity<GetSecurityGroupDetailInfoRsp> getSecurityGroupDetailInfo(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                                    @ApiParam(value = "sgId", required = true, name = "sgId") @PathVariable("sgId") String sgId,
                                                                                    @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.info("get security group, cloud:{}, sgId:{}, userId:{}", cloudId, sgId, userId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            GetSecurityGroupDetailInfoRsp getSecurityGroupDetailInfoRsp = securityGroupService.getSecurityGroup(cloudId, sgId, operUserId);
            return ResponseEntity.ok(getSecurityGroupDetailInfoRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get security group error: {}, sgId: {}",e.getMessage(), sgId);
            throw  throwWebException(e);
        }
    }

    @PutMapping(value = "/sgs/{sgId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update security group", response = Object.class)
    @LNJoyAuditLog(value = "update security group",
            resourceMapperId = CmpResources.NS_SECURITY_GROUP,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.UPDATE_SECURITY_GROUP,
            associatedResourceCnName = "安全组",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "sgId", resourcePrimaryKeyClass = TblCmpSecurityGroupKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpSecurityGroupKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> updateSg(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                           @ApiParam(value = "sgId", required = true, name = "sgId") @PathVariable("sgId") String sgId,
                                           @ApiParam(value = "CommonReq", required = true, name = "CommonReq") @RequestBody @Valid CommonReq request,
                                           @RequestHeader(name = "X-UserId", required = false) String userId)throws WebSystemException
    {
        try
        {
            LOGGER.debug("remove security group: {}, userId: {}", sgId, userId);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
                securityGroupService.checkSgUser(cloudId, sgId, userId);
            }

            return cloudService.sendHttpRequestToCloud(cloudId);
        }
        catch (Exception e)
        {
            LOGGER.error("remove security group error: {}, vmInstanceId: {}", e.getMessage(), sgId);
            throw  throwWebException(e);
        }
    }

    @DeleteMapping(value = "/sgs/{sgId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete security group", response = Object.class)
    @LNJoyAuditLog(value = "delete security group",
            resourceMapperId = CmpResources.NS_SECURITY_GROUP,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.DELETE_SECURITY_GROUP,
            associatedResourceCnName = "安全组",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "sgId", resourcePrimaryKeyClass = TblCmpSecurityGroupKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpSecurityGroupKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> removeSg(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                           @ApiParam(value = "sgId", required = true, name = "sgId") @PathVariable("sgId") String sgId,
                                           @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.debug("remove security group, cloud:{}, sgId:{}, userId:{}", cloudId, sgId, userId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return securityGroupService.removeSecurityGroup(cloudId, sgId, operUserId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove security group error: {}, vmInstanceId: {}", e.getMessage(), sgId);
            throw  throwWebException(e);
        }
    }

    @PostMapping(value = "/sgs/{sgId}/rules", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "create security groups", response = Object.class)
    @LNJoyAuditLog(value = "create security groups",
            resourceMapperId = CmpResources.NS_SECURITY_GROUP,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.ADD_SECURITY_GROUP_RULE,
            associatedResourceCnName = "安全组",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "sgId", resourcePrimaryKeyClass = TblCmpSecurityGroupKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpSecurityGroupKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> addSgRule(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                  @ApiParam(value = "sgId", required = true, name = "sgId") @PathVariable("sgId") String sgId,
                                                  @ApiParam(value = "CreateUpdateSgRulesReq", required = true, name = "CreateUpdateSgRulesReq") @RequestBody @Valid SgRulesCreateUpdateReqVo request,
                                                  @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                  @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("create security rules, cloud:{}, request:{}", cloudId, request);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
                securityGroupService.checkSgUser(cloudId, sgId, userId);
            }

            return securityGroupService.createSgRule(cloudId, request, sgId, bpId, userId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add  security rule failed: {}", e.getMessage());

            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/sgs/{sgId}/rules/{ruleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "put security rule detail info", response = Object.class)
    @LNJoyAuditLog(value = "put security rule detail info",
            resourceMapperId = CmpResources.NS_SECURITY_GROUP_RULE,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.UPDATE_SECURITY_GROUP_RULE,
            associatedResourceCnName = "安全组",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "ruleId", resourcePrimaryKeyClass = TblCmpSecurityGroupRuleKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpSecurityGroupRuleKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> updateRule(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                             @ApiParam(value = "CreateUpdateSgRuleReq", required = true, name = "CreateUpdateSgRuleReq") @RequestBody SgRuleCreateUpdateReqVo request,
                                             @ApiParam(value = "sgId", required = true, name = "sgId") @PathVariable("sgId") String sgId,
                                             @ApiParam(value = "ruleId", required = true, name = "ruleId") @PathVariable("ruleId") String ruleId,
                                             @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.info("update security rule, request:{}, userId:{}", ruleId, userId);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
                securityGroupService.checkSgUser(cloudId, sgId, userId);
                securityGroupService.checkSgRuleUser(cloudId, ruleId, userId);
            }

            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);

            cloudService.syncSingleData(cloudId, sgId, SyncMsg.NS_SECURITY_GROUP);

            return response;
        }
        catch (Exception e)
        {
            LOGGER.error("update security group error: {}, vmInstanceId: {}", e.getMessage(), ruleId);
            throw  throwWebException(e);
        }
    }

    @DeleteMapping(value = "/rules/{ruleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete security rule", response = Object.class)
    @LNJoyAuditLog(value = "delete security rule",
            resourceMapperId = CmpResources.NS_SECURITY_GROUP_RULE,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.DELETE_SECURITY_GROUP_RULE,
            associatedResourceCnName = "安全组规则",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "ruleId", resourcePrimaryKeyClass = TblCmpSecurityGroupRuleKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpSecurityGroupRuleKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> deleteRule(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                              @ApiParam(value = "ruleId", required = true, name = "ruleId") @PathVariable("ruleId") String ruleId,
                                              @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.info("delete security rule, cloud:{}, ruleId:{}, userId:{}", cloudId, ruleId, userId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return securityGroupService.removeSgRule(cloudId, ruleId, operUserId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("update security group error: {}, vmInstanceId: {}",e.getMessage(), ruleId);
            throw  throwWebException(e);
        }
    }


    @PostMapping(value = "/sgs/{sgId}/bound", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "the security group is applied to the VM", response = Object.class)
    @LNJoyAuditLog(value = "the security group is applied to the VM",
            resourceMapperId = CmpResources.NS_SECURITY_GROUP,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.BOUND_SECURITY_GROUP,
            associatedResourceCnName = "安全组",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "sgId", resourcePrimaryKeyClass = TblCmpSecurityGroupKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpSecurityGroupKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> boundVmInstance(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                        @ApiParam(value = "sgId", required = true, name = "sgId") @PathVariable("sgId") String sgId,
                                                        @ApiParam(value = "BoundOrUnBoundSecurityGroup", required = true, name = "BoundOrUnBoundSecurityGroup") @RequestBody @Valid SecurityGroupBoundUnboundReqVo request,
                                                        @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                        @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("security group is applied to the vm, cloud:{}, request:{}, sgId:{}", cloudId, request, sgId);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
                securityGroupService.checkSgUser(cloudId, sgId, userId);
            }

            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);

            if (request != null && request.getVmInstances() != null)
            {
                request.getVmInstances().forEach(vmInstance -> cloudService.syncSingleData(cloudId, vmInstance, SyncMsg.NS_VM_INSTANCE));
            }

            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("bound security group failed: {}", e.getMessage());

            throw throwWebException(e);
        }
    }

    @PostMapping(value = "/sgs/{sgId}/unbound", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = " VM is bound to the security group", response = Object.class)
    @LNJoyAuditLog(value = " VM is bound to the security group",
            resourceMapperId = CmpResources.NS_SECURITY_GROUP,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.UNBOUND_SECURITY_GROUP,
            associatedResourceCnName = "安全组",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "sgId", resourcePrimaryKeyClass = TblCmpSecurityGroupKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpSecurityGroupKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> unboundVmInstance(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                          @ApiParam(value = "sgId", required = true, name = "sgId") @PathVariable("sgId") String sgId,
                                                          @ApiParam(value = "BoundOrUnBoundSecurityGroup", required = true, name = "BoundOrUnBoundSecurityGroup") @RequestBody @Valid SecurityGroupBoundUnboundReqVo request,
                                                          @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("VM is unbound from the security group, cloud:{}, vmInstanceId:{}, sgId:{}", cloudId, request, sgId);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
                securityGroupService.checkSgUser(cloudId, sgId, userId);
            }

            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);

            if (request != null && request.getVmInstances() != null)
            {
                request.getVmInstances().forEach(vmInstance -> cloudService.syncSingleData(cloudId, vmInstance, SyncMsg.NS_VM_INSTANCE));
            }

            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("unbound security group failed: {}", e.getMessage());

            throw throwWebException(e);
        }
    }
}
