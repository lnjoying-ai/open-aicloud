package com.lnjoying.justice.cmp.controller.easystack.network;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpEsFirewallPoliciesKey;
import com.lnjoying.justice.cmp.domain.dto.request.easystack.neutron.ESFirewallPolicyCreateReq;
import com.lnjoying.justice.cmp.domain.dto.request.easystack.neutron.ESFirewallPolicyUpdateReq;
import com.lnjoying.justice.cmp.domain.dto.request.easystack.neutron.ESPolicyAddRuleReq;
import com.lnjoying.justice.cmp.domain.dto.request.easystack.neutron.ESPolicyRemoveRuleReq;
import com.lnjoying.justice.cmp.domain.dto.response.easystack.neturon.ESFirewallPoliciesRsp;
import com.lnjoying.justice.cmp.domain.dto.response.easystack.neturon.ESFirewallPolicyRsp;
import com.lnjoying.justice.cmp.entity.search.easystack.ESFirewallPoliciesSearchCritical;
import com.lnjoying.justice.cmp.handler.actiondescription.i18n.zh_cn.CmpEasystackActionDescriptionTemplate;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.easystack.FirewallPolicyService;
import com.lnjoying.justice.cmp.utils.osclient.OSClientUtil;
import com.lnjoying.justice.commonweb.controller.RestWebController;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isAdmin;

@RestSchema(schemaId = "esfirewallpolicy")
@RequestMapping("/cmp/v1/ESK/clouds/{cloud_id}/v2.0/fw")
@Api(value = "EasyStack Firewall Policies Controller",tags = "EasyStack Firewall Policies Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "es_firewall_policy"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "es_firewall_policies"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "OpenStack防火墙策略"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpEsFirewallPolicies")})})
public class ESFirewallPolicyController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private FirewallPolicyService firewallPolicyService;

    @Autowired
    private CloudService cloudService;

    @GetMapping(value = "/firewall_policies", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List firewall policies", response = Object.class)
    public ResponseEntity<ESFirewallPoliciesRsp> getFirewallPolicies(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                     @ApiParam(name = "id") @RequestParam(required = false, value = "id") String id,
                                                                     @ApiParam(name = "tenant_id") @RequestParam(required = false, value = "tenant_id") String tenantId,
                                                                     @ApiParam(name = "project_id") @RequestParam(required = false, value = "project_id") String projectId,
                                                                     @ApiParam(name = "name") @RequestParam(required = false, value = "name") String name,
                                                                     @ApiParam(name = "description") @RequestParam(required = false, value = "description") String description,
                                                                     @ApiParam(name = "shared") @RequestParam(required = false, value = "shared") Boolean shared,
                                                                     @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get firewall policies. cloud:{}", cloudId);

            String filterUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                filterUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            if (StringUtils.isEmpty(projectId))
            {
                projectId = OSClientUtil.getOSProject(cloudId);
            }

            ESFirewallPoliciesSearchCritical firewallPoliciesSearchCritical = new ESFirewallPoliciesSearchCritical(id,
                    tenantId, projectId, name, description, shared, null, null);

            ESFirewallPoliciesRsp firewallPoliciesRsp = firewallPolicyService.getFirewallPolicies(cloudId, firewallPoliciesSearchCritical, filterUserId);

            return ResponseEntity.ok(firewallPoliciesRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get firewall policies failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @PostMapping(value = "/firewall_policies", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create firewall policy", response = Object.class)
    @LNJoyAuditLog(value = "Create firewall policy",
            resourceMapperId = CmpResources.ES_FIREWALL_POLICY,
            actionDescriptionTemplate = CmpEasystackActionDescriptionTemplate.Descriptions.CREATE_FIREWALL_POLICY,
            associatedResourceCnName = "防火墙策略",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                    resourceIdParseSPEL = "#obj?.body?.get('securityGroup')?.get('id')?.toString()", resourcePrimaryKeyClass = TblCmpEsFirewallPoliciesKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpEsFirewallPoliciesKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> addFirewallPolicy(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                             @ApiParam(value = "firewall_policy", required = true, name = "firewall_policy") @RequestBody ESFirewallPolicyCreateReq request,
                                             @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                             @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("add firewall policy, cloud:{}, request:{}", cloudId, request);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return firewallPolicyService.addFirewallPolicy(cloudId, request, bpId, userId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add firewall policy failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/firewall_policies/{firewall_policy_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Show firewall policy", response = Object.class)
    public ResponseEntity<ESFirewallPolicyRsp> getFirewallPolicy(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                 @ApiParam(value = "firewall_policy_id", required = true, name = "firewall_policy_id") @PathVariable("firewall_policy_id") String firewallPolicyId,
                                                                 @ApiParam(name = "fields") @RequestParam(required = false, value = "fields") String fields,
                                                                 @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get firewall policy. cloud:{}, security_group_id:{}", cloudId, firewallPolicyId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            ESFirewallPolicyRsp firewallPolicyRsp = firewallPolicyService.getFirewallPolicy(cloudId, firewallPolicyId, operUserId);

            return ResponseEntity.ok(firewallPolicyRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get firewall policy failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/firewall_policies/{firewall_policy_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update firewall policy", response = Object.class)
    @LNJoyAuditLog(value = "Update firewall policy",
            resourceMapperId = CmpResources.ES_FIREWALL_POLICY,
            actionDescriptionTemplate = CmpEasystackActionDescriptionTemplate.Descriptions.UPDATE_FIREWALL_POLICY,
            associatedResourceCnName = "防火墙策略",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "firewallPolicyId", resourcePrimaryKeyClass = TblCmpEsFirewallPoliciesKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpEsFirewallPoliciesKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> updateFirewallPolicy(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                @ApiParam(value = "firewall_policy_id", required = true, name = "firewall_policy_id") @PathVariable(value = "firewall_policy_id") String firewallPolicyId,
                                                @ApiParam(value = "firewall_policy", required = true, name = "firewall_policy") @RequestBody ESFirewallPolicyUpdateReq request,
                                                @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("update firewall policy, cloud:{}, request:{}", cloudId, request);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return firewallPolicyService.updateFirewallPolicy(cloudId, firewallPolicyId, request, operUserId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("update firewall policy failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @DeleteMapping(value = "/firewall_policies/{firewall_policy_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Delete firewall policy", response = Object.class)
    @LNJoyAuditLog(value = "Delete firewall policy",
            resourceMapperId = CmpResources.ES_FIREWALL_POLICY,
            actionDescriptionTemplate = CmpEasystackActionDescriptionTemplate.Descriptions.DELETE_FIREWALL_POLICY,
            associatedResourceCnName = "防火墙策略",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "firewallPolicyId", resourcePrimaryKeyClass = TblCmpEsFirewallPoliciesKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpEsFirewallPoliciesKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> removeFirewallPolicy(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                @ApiParam(value = "firewall_policy_id", required = true, name = "firewall_policy_id") @PathVariable("firewall_policy_id")String firewallPolicyId,
                                                @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("remove firewall policy, cloud:{}, firewall_policy_id:{}", cloudId, firewallPolicyId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return firewallPolicyService.removeFirewallPolicy(cloudId, firewallPolicyId, operUserId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove firewall policy failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/firewall_policies/{firewall_policy_id}/insert_rule", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Add firewall rule", response = Object.class)
    @LNJoyAuditLog(value = "Add firewall rule",
            resourceMapperId = CmpResources.ES_FIREWALL_POLICY,
            actionDescriptionTemplate = CmpEasystackActionDescriptionTemplate.Descriptions.ADD_FIREWALL_RULE_TO_POLICY,
            associatedResourceCnName = "防火墙策略",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "firewallPolicyId", resourcePrimaryKeyClass = TblCmpEsFirewallPoliciesKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpEsFirewallPoliciesKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> addFirewallRuleToPolicy(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                      @ApiParam(value = "firewall_policy_id", required = true, name = "firewall_policy_id") @PathVariable(value = "firewall_policy_id") String firewallPolicyId,
                                                      @ApiParam(value = "security_group", required = true, name = "security_group") @RequestBody ESPolicyAddRuleReq request,
                                                      @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                      @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("update firewall policy, cloud:{}, request:{}", cloudId, request);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return firewallPolicyService.addFirewallRuleToPolicy(cloudId, firewallPolicyId, request, operUserId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("update firewall policy failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/firewall_policies/{firewall_policy_id}/remove_rule", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Remove firewall rule", response = Object.class)
    @LNJoyAuditLog(value = "Remove firewall rule",
            resourceMapperId = CmpResources.ES_FIREWALL_POLICY,
            actionDescriptionTemplate = CmpEasystackActionDescriptionTemplate.Descriptions.DELETE_FIREWALL_RULE_FROM_POLICY,
            associatedResourceCnName = "防火墙策略",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "firewallPolicyId", resourcePrimaryKeyClass = TblCmpEsFirewallPoliciesKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpEsFirewallPoliciesKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> removeFirewallRuleFromPolicy(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                          @ApiParam(value = "firewall_policy_id", required = true, name = "firewall_policy_id") @PathVariable(value = "firewall_policy_id") String firewallPolicyId,
                                                          @ApiParam(value = "security_group", required = true, name = "security_group") @RequestBody ESPolicyRemoveRuleReq request,
                                                          @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                          @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("update firewall policy, cloud:{}, request:{}", cloudId, request);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return firewallPolicyService.removeFirewallRuleFromPolicy(cloudId, firewallPolicyId, request, operUserId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("update firewall policy failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }
}
