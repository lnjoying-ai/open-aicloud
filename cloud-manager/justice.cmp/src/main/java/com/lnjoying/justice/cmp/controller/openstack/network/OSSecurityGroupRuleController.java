package com.lnjoying.justice.cmp.controller.openstack.network;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpOsSecuritygrouprulesKey;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron.OSSecurityGroupRuleCreateReq;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSSecurityGroupRuleWithDetailsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSSecurityGroupRulesWithDetailsRsp;
import com.lnjoying.justice.cmp.entity.search.openstack.OSSecurityGroupRuleSearchCritical;
import com.lnjoying.justice.cmp.handler.actiondescription.i18n.zh_cn.CmpOpenstackActionDescriptionTemplate;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.openstack.SecurityGroupRuleService;
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

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isAdmin;

@RestSchema(schemaId = "ossecuritygrouprule")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/v2.0/security-group-rules")
@Api(value = "OpenStack Security Group Rules Controller",tags = "OpenStack Security Group Rules Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "os_securitygroup_rule"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "os_securitygroup_rules"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "OpenStack安全组规则"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpOsSecuritygrouprules")})})
public class OSSecurityGroupRuleController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private SecurityGroupRuleService securityGroupRuleService;

    @Autowired
    private CloudService cloudService;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List security group rules", response = Object.class)
    public ResponseEntity<OSSecurityGroupRulesWithDetailsRsp> getSecurityGroupRules(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                                    @ApiParam(name = "remote_group_id") @RequestParam(required = false, value = "remote_group_id") String remoteGroupId,
                                                                                    @ApiParam(name = "direction") @RequestParam(required = false, value = "direction") String direction,
                                                                                    @ApiParam(name = "protocol") @RequestParam(required = false, value = "protocol") String protocol,
                                                                                    @ApiParam(name = "ethertype") @RequestParam(required = false, value = "ethertype") String ethertype,
                                                                                    @ApiParam(name = "port_range_max") @RequestParam(required = false, value = "port_range_max") Integer portRangeMax,
                                                                                    @ApiParam(name = "security_group_id") @RequestParam(required = false, value = "security_group_id") String securityGroupId,
                                                                                    @ApiParam(name = "tenant_id") @RequestParam(required = false, value = "tenant_id") String tenantId,
                                                                                    @ApiParam(name = "project_id") @RequestParam(required = false, value = "project_id") String projectId,
                                                                                    @ApiParam(name = "port_range_min") @RequestParam(required = false, value = "port_range_min") Integer portRangeMin,
                                                                                    @ApiParam(name = "remote_ip_prefix") @RequestParam(required = false, value = "remote_ip_prefix") String remoteIpPrefix,
                                                                                    @ApiParam(name = "revision_number") @RequestParam(required = false, value = "revision_number") Integer revisionNumber,
                                                                                    @ApiParam(name = "id") @RequestParam(required = false, value = "id") String id,
                                                                                    @ApiParam(name = "description") @RequestParam(required = false, value = "description") String description,
                                                                                    @ApiParam(name = "sort_dir") @RequestParam(required = false, value = "sort_dir") String sortDir,
                                                                                    @ApiParam(name = "sort_key") @RequestParam(required = false, value = "sort_key") String sortKey,
                                                                                    @ApiParam(name = "fields") @RequestParam(required = false, value = "fields") String fields,
                                                                                    @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get security group rules. cloud:{}", cloudId);

            String filterUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                filterUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            OSSecurityGroupRuleSearchCritical securityGroupRuleSearchCritical = new OSSecurityGroupRuleSearchCritical(remoteGroupId,
                    direction, protocol, ethertype, portRangeMax, securityGroupId, tenantId, projectId, portRangeMin, remoteIpPrefix,
                    revisionNumber, id, description, sortDir, sortKey, fields, null, null);

            OSSecurityGroupRulesWithDetailsRsp securityGroupRulesWithDetailsRsp = securityGroupRuleService.getSecurityGroupRules(cloudId, securityGroupRuleSearchCritical, filterUserId);

            return ResponseEntity.ok(securityGroupRulesWithDetailsRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get security group rules failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create security group rule", response = Object.class)
    @LNJoyAuditLog(value = "Create security group rule",
            resourceMapperId = CmpResources.OS_SECURITYGROUP_RULE,
            actionDescriptionTemplate = CmpOpenstackActionDescriptionTemplate.Descriptions.CREATE_SECURITYGROUPRULE,
            associatedResourceCnName = "安全组规则",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                    resourceIdParseSPEL = "#obj?.body?.get('securityGroupRule')?.get('id')?.toString()", resourcePrimaryKeyClass = TblCmpOsSecuritygrouprulesKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpOsSecuritygrouprulesKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> addSecurityGroupRule(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                             @ApiParam(value = "security_group", required = true, name = "security_group") @RequestBody OSSecurityGroupRuleCreateReq request,
                                             @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                             @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("add security group rule, cloud:{}, request:{}", cloudId, request);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
            }


            return securityGroupRuleService.addSecurityGroupRule(cloudId, request, bpId, userId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add security group rule failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/{security_group_rule_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Show security group rule", response = Object.class)
    public ResponseEntity<OSSecurityGroupRuleWithDetailsRsp> getSecurityGroupRuleDetails(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                                         @ApiParam(value = "security_group_rule_id", required = true, name = "security_group_rule_id") @PathVariable("security_group_rule_id") String securityGroupRuleId,
                                                                                         @ApiParam(name = "verbose") @RequestParam(required = false, value = "verbose") Boolean verbose,
                                                                                         @ApiParam(name = "fields") @RequestParam(required = false, value = "fields") String fields,
                                                                                         @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get security group rule details. cloud:{}, security_group_rule_id:{}", cloudId, securityGroupRuleId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            OSSecurityGroupRuleWithDetailsRsp networkWithDetails = securityGroupRuleService.getSecurityGroupRuleDetails(cloudId, securityGroupRuleId, fields, operUserId);

            return ResponseEntity.ok(networkWithDetails);
        }
        catch (Exception e)
        {
            LOGGER.error("get security group rule failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @DeleteMapping(value = "/{security_group_rule_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Delete security group rule", response = Object.class)
    @LNJoyAuditLog(value = "Delete security group rule",
            resourceMapperId = CmpResources.OS_SECURITYGROUP_RULE,
            actionDescriptionTemplate = CmpOpenstackActionDescriptionTemplate.Descriptions.DELETE_SECURITYGROUPRULE,
            associatedResourceCnName = "安全组规则",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "securityGroupRuleId", resourcePrimaryKeyClass = TblCmpOsSecuritygrouprulesKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpOsSecuritygrouprulesKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> removeSecurityGroupRule(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                @ApiParam(value = "security_group_rule_id", required = true, name = "security_group_rule_id") @PathVariable("security_group_rule_id")String securityGroupRuleId,
                                                @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("remove security group rule, cloud:{}, security_group_rule_id:{}", cloudId, securityGroupRuleId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return securityGroupRuleService.removeSecurityGroupRule(cloudId, securityGroupRuleId, operUserId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove security group rule failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }
}
