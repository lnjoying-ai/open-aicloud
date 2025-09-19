package com.lnjoying.justice.cmp.controller.easystack.network;

import com.lnjoying.justice.cmp.domain.dto.response.easystack.neturon.ESExtFirewallRulesRsp;
import com.lnjoying.justice.cmp.entity.search.easystack.ESFirewallRuleSearchCritical;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.easystack.FirewallRuleService;
import com.lnjoying.justice.cmp.utils.osclient.OSClientUtil;
import com.lnjoying.justice.commonweb.controller.RestWebController;
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

@RestSchema(schemaId = "esextensionfirewallrule")
@RequestMapping("/cmp/v1/ESK/clouds/{cloud_id}/extension/v2.0/fw")
@Api(value = "EasyStack Firewall Rules Controller",tags = "EasyStack Firewall Rules Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "es_ext_firewall_rule"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "es_ext_firewall_rules"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "OpenStack防火墙规则"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpEsFirewallRules")})})
public class ESExtFirewallRuleController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private FirewallRuleService firewallRuleService;

    @Autowired
    private CloudService cloudService;

    @GetMapping(value = "/firewall_rules", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List firewall rules", response = Object.class)
    public ResponseEntity<ESExtFirewallRulesRsp> getFirewallRules(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
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
                                                                  @ApiParam(name = "page_size") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                                  @ApiParam(name = "page_num") @RequestParam(value = "pageNum", required = false) Integer pageNum,
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
                    destinationPort, shared, ipVersion, pageNum, pageSize);

            ESExtFirewallRulesRsp firewallRulesRsp = firewallRuleService.getFirewallRulesPage(cloudId, firewallRuleSearchCritical, filterUserId);

            return ResponseEntity.ok(firewallRulesRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get firewall rules failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }
}
