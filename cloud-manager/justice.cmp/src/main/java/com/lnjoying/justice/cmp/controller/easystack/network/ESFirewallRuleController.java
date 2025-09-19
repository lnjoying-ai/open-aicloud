package com.lnjoying.justice.cmp.controller.easystack.network;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpEsFirewallRulesKey;
import com.lnjoying.justice.cmp.domain.dto.request.easystack.neutron.ESFirewallRuleCreateReq;
import com.lnjoying.justice.cmp.domain.dto.request.easystack.neutron.ESFirewallRuleUpdateReq;
import com.lnjoying.justice.cmp.domain.dto.response.easystack.neturon.ESFirewallRuleRsp;
import com.lnjoying.justice.cmp.domain.dto.response.easystack.neturon.ESFirewallRulesRsp;
import com.lnjoying.justice.cmp.entity.search.easystack.ESFirewallRuleSearchCritical;
import com.lnjoying.justice.cmp.handler.actiondescription.i18n.zh_cn.CmpEasystackActionDescriptionTemplate;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.easystack.FirewallRuleService;
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

@RestSchema(schemaId = "esfirewallrule")
@RequestMapping("/cmp/v1/ESK/clouds/{cloud_id}/v2.0/fw")
@Api(value = "EasyStack Firewall Rules Controller",tags = "EasyStack Firewall Rules Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "es_firewall_rule"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "es_firewall_rules"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "OpenStack防火墙规则"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpEsFirewallRules")})})
public class ESFirewallRuleController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private FirewallRuleService firewallRuleService;

    @Autowired
    private CloudService cloudService;

    @GetMapping(value = "/firewall_rules", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List firewall rules", response = Object.class)
    public ResponseEntity<ESFirewallRulesRsp> getFirewallRules(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                               @ApiParam(name = "id") @RequestParam(required = false, value = "id") String id,
                                                               @ApiParam(name = "tenant_id") @RequestParam(required = false, value = "tenant_id") String tenantId,
                                                               @ApiParam(name = "project_id") @RequestParam(required = false, value = "project_id") String projectId,
                                                               @ApiParam(name = "name") @RequestParam(required = false, value = "name") String name,
                                                               @ApiParam(name = "description") @RequestParam(required = false, value = "description") String description,
                                                               @ApiParam(name = "firewall_policy_id") @RequestParam(required = false, value = "firewall_policy_id") String firewallPolicyId,
                                                               @ApiParam(name = "action") @RequestParam(required = false, value = "action") String action,
                                                               @ApiParam(name = "protocol") @RequestParam(required = false, value = "protocol") String protocol,
                                                               @ApiParam(name = "source_ip_address") @RequestParam(required = false, value = "source_ip_address") String sourceIpAddress,
                                                               @ApiParam(name = "source_port") @RequestParam(required = false, value = "source_ports") String sourcePort,
                                                               @ApiParam(name = "destination_ip_address") @RequestParam(required = false, value = "destination_ip_address") String destinationIpAddress,
                                                               @ApiParam(name = "destination_port") @RequestParam(required = false, value = "destination_port") String destinationPort,
                                                               @ApiParam(name = "shared") @RequestParam(required = false, value = "shared") Boolean shared,
                                                               @ApiParam(name = "ip_version") @RequestParam(required = false, value = "ip_version") Integer ipVersion,
                                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get firewall rules. cloud:{}", cloudId);

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

            ESFirewallRuleSearchCritical firewallRuleSearchCritical = new ESFirewallRuleSearchCritical(id, tenantId, projectId,
                    name, description, firewallPolicyId, action, protocol, sourceIpAddress, sourcePort, destinationIpAddress,
                    destinationPort, shared, ipVersion, null, null);

            ESFirewallRulesRsp firewallRulesRsp = firewallRuleService.getFirewallRules(cloudId, firewallRuleSearchCritical, filterUserId);

            return ResponseEntity.ok(firewallRulesRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get firewall rules failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @PostMapping(value = "/firewall_rules", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create firewall rule", response = Object.class)
    @LNJoyAuditLog(value = "Create firewall rule",
            resourceMapperId = CmpResources.ES_FIREWALL_RULE,
            actionDescriptionTemplate = CmpEasystackActionDescriptionTemplate.Descriptions.CREATE_FIREWALL_RULE,
            associatedResourceCnName = "防火墙规则",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                    resourceIdParseSPEL = "#obj?.body?.get('securityGroup')?.get('id')?.toString()", resourcePrimaryKeyClass = TblCmpEsFirewallRulesKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpEsFirewallRulesKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> addFirewallRule(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                  @ApiParam(value = "firewall_rule", required = true, name = "firewall_rule") @RequestBody ESFirewallRuleCreateReq request,
                                                  @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                  @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("add firewall rule, cloud:{}, request:{}", cloudId, request);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return firewallRuleService.addFirewallRule(cloudId, request, bpId, userId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add firewall rule failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/firewall_rules/{firewall_rule_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Show firewall rule", response = Object.class)
    public ResponseEntity<ESFirewallRuleRsp> getFirewallRule(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                             @ApiParam(value = "firewall_rule_id", required = true, name = "firewall_rule_id") @PathVariable("firewall_rule_id") String firewallRuleId,
                                                             @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get firewall rule. cloud:{}, firewall_rule_id:{}", cloudId, firewallRuleId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            ESFirewallRuleRsp firewallRule = firewallRuleService.getFirewallRule(cloudId, firewallRuleId, operUserId);

            return ResponseEntity.ok(firewallRule);
        }
        catch (Exception e)
        {
            LOGGER.error("get firewall rule failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/firewall_rules/{firewall_rule_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update firewall rule", response = Object.class)
    @LNJoyAuditLog(value = "Update firewall rule",
            resourceMapperId = CmpResources.ES_FIREWALL_RULE,
            actionDescriptionTemplate = CmpEasystackActionDescriptionTemplate.Descriptions.UPDATE_FIREWALL_RULE,
            associatedResourceCnName = "防火墙规则",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "securityGroupId", resourcePrimaryKeyClass = TblCmpEsFirewallRulesKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpEsFirewallRulesKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> updateFirewallRule(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                @ApiParam(value = "firewall_rule_id", required = true, name = "firewall_rule_id") @PathVariable(value = "firewall_rule_id") String firewallRuleId,
                                                @ApiParam(value = "firewall_rule", required = true, name = "firewall_rule") @RequestBody ESFirewallRuleUpdateReq request,
                                                @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("update firewall rule, cloud:{}, request:{}", cloudId, request);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return firewallRuleService.updateFirewallRule(cloudId, firewallRuleId, request, operUserId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("update firewall rule failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @DeleteMapping(value = "/firewall_rules/{firewall_rule_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Delete firewall rule", response = Object.class)
    @LNJoyAuditLog(value = "Delete firewall rule",
            resourceMapperId = CmpResources.ES_FIREWALL_RULE,
            actionDescriptionTemplate = CmpEasystackActionDescriptionTemplate.Descriptions.DELETE_FIREWALL_RULE,
            associatedResourceCnName = "防火墙规则",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "securityGroupId", resourcePrimaryKeyClass = TblCmpEsFirewallRulesKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpEsFirewallRulesKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> removeFirewallRule(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                @ApiParam(value = "firewall_rule_id", required = true, name = "firewall_rule_id") @PathVariable("firewall_rule_id")String firewallRuleId,
                                                @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("remove firewall rule, cloud:{}, security_group_id:{}", cloudId, firewallRuleId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return firewallRuleService.removeFirewallRule(cloudId, firewallRuleId, operUserId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove firewall rule failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }
}
