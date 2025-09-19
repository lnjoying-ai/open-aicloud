package com.lnjoying.justice.cmp.controller.easystack.network;

import com.lnjoying.justice.cmp.domain.dto.response.easystack.neturon.ESExtFirewallPoliciesRsp;
import com.lnjoying.justice.cmp.entity.search.easystack.ESFirewallPoliciesSearchCritical;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.easystack.FirewallPolicyService;
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

@RestSchema(schemaId = "esextensionfirewallpolicy")
@RequestMapping("/cmp/v1/ESK/clouds/{cloud_id}/extension/v2.0/fw")
@Api(value = "EasyStack Firewall Policies Controller",tags = "EasyStack Firewall Policies Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "es_ext_firewall_policy"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "es_ext_firewall_policies"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "OpenStack防火墙策略"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpEsFirewallPolicies")})})
public class ESExtFirewallPolicyController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private FirewallPolicyService firewallPolicyService;

    @Autowired
    private CloudService cloudService;

    @GetMapping(value = "/firewall_policies", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List firewall policies", response = Object.class)
    public ResponseEntity<ESExtFirewallPoliciesRsp> getFirewallPolicies(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                        @ApiParam(name = "id") @RequestParam(required = false, value = "id") String id,
                                                                        @ApiParam(name = "tenant_id") @RequestParam(required = false, value = "tenant_id") String tenantId,
                                                                        @ApiParam(name = "project_id") @RequestParam(required = false, value = "project_id") String projectId,
                                                                        @ApiParam(name = "name") @RequestParam(required = false, value = "name") String name,
                                                                        @ApiParam(name = "description") @RequestParam(required = false, value = "description") String description,
                                                                        @ApiParam(name = "shared") @RequestParam(required = false, value = "shared") Boolean shared,
                                                                        @ApiParam(name = "page_size") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                                        @ApiParam(name = "page_num") @RequestParam(value = "pageNum", required = false) Integer pageNum,
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
                    tenantId, projectId, name, description, shared, pageNum, pageSize);

            ESExtFirewallPoliciesRsp firewallPoliciesRsp = firewallPolicyService.getFirewallPoliciesPage(cloudId, firewallPoliciesSearchCritical, filterUserId);

            return ResponseEntity.ok(firewallPoliciesRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get firewall policies failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }
}
