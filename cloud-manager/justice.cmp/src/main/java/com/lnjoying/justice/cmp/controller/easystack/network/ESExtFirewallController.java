package com.lnjoying.justice.cmp.controller.easystack.network;

import com.lnjoying.justice.cmp.domain.dto.response.easystack.neturon.ESExtFirewallsRsp;;
import com.lnjoying.justice.cmp.entity.search.easystack.ESFirewallSearchCritical;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.easystack.FirewallService;
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

@RestSchema(schemaId = "esextensionfirewall")
@RequestMapping("/cmp/v1/ESK/clouds/{cloud_id}/extension/v2.0/fw")
@Api(value = "EasyStack Firewalls Controller",tags = "EasyStack Firewalls Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "es_ext_firewall"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "es_ext_firewalls"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "OpenStack防火墙"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpEsFirewalls")})})
public class ESExtFirewallController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private FirewallService firewallService;

    @Autowired
    private CloudService cloudService;

    @GetMapping(value = "/firewalls", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List firewalls", response = Object.class)
    public ResponseEntity<ESExtFirewallsRsp> getFirewalls(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                          @ApiParam(name = "id") @RequestParam(required = false, value = "id") String id,
                                                          @ApiParam(name = "name") @RequestParam(required = false, value = "name") String name,
                                                          @ApiParam(name = "description") @RequestParam(required = false, value = "description") String description,
                                                          @ApiParam(name = "tenant_id") @RequestParam(required = false, value = "tenant_id") String tenantId,
                                                          @ApiParam(name = "project_id") @RequestParam(required = false, value = "project_id") String projectId,
                                                          @ApiParam(name = "firewall_policy_id") @RequestParam(required = false, value = "firewall_policy_id") String firewallPolicyId,
                                                          @ApiParam(name = "router_id") @RequestParam(required = false, value = "router_id") String routerId,
                                                          @ApiParam(name = "status") @RequestParam(required = false, value = "status") String status,
                                                          @ApiParam(name = "page_size") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                          @ApiParam(name = "page_num") @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                          @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get firewalls. cloud:{}", cloudId);

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

            ESFirewallSearchCritical firewallSearchCritical = new ESFirewallSearchCritical(id, tenantId, projectId,
                    name, description, firewallPolicyId, routerId, status, pageNum, pageSize);

            ESExtFirewallsRsp firewallsRsp = firewallService.getFirewallsPage(cloudId, firewallSearchCritical, filterUserId);

            return ResponseEntity.ok(firewallsRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get firewalls failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }
}
