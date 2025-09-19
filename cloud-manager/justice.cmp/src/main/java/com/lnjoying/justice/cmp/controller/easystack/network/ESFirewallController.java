package com.lnjoying.justice.cmp.controller.easystack.network;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpEsFirewallsKey;
import com.lnjoying.justice.cmp.domain.dto.request.easystack.neutron.ESFirewallCreateReq;
import com.lnjoying.justice.cmp.domain.dto.request.easystack.neutron.ESFirewallUpdateReq;
import com.lnjoying.justice.cmp.domain.dto.response.easystack.neturon.ESFirewallRsp;
import com.lnjoying.justice.cmp.domain.dto.response.easystack.neturon.ESFirewallsRsp;
import com.lnjoying.justice.cmp.entity.search.easystack.ESFirewallSearchCritical;
import com.lnjoying.justice.cmp.handler.actiondescription.i18n.zh_cn.CmpEasystackActionDescriptionTemplate;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.easystack.FirewallService;
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

@RestSchema(schemaId = "esfirewall")
@RequestMapping("/cmp/v1/ESK/clouds/{cloud_id}/v2.0/fw")
@Api(value = "EasyStack Firewalls Controller",tags = "EasyStack Firewalls Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "es_firewall"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "es_firewalls"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "OpenStack防火墙"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpEsFirewalls")})})
public class ESFirewallController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private FirewallService firewallService;

    @Autowired
    private CloudService cloudService;

    @GetMapping(value = "/firewalls", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List firewalls", response = Object.class)
    public ResponseEntity<ESFirewallsRsp> getFirewalls(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                       @ApiParam(name = "id") @RequestParam(required = false, value = "id") String id,
                                                       @ApiParam(name = "name") @RequestParam(required = false, value = "name") String name,
                                                       @ApiParam(name = "description") @RequestParam(required = false, value = "description") String description,
                                                       @ApiParam(name = "tenant_id") @RequestParam(required = false, value = "tenant_id") String tenantId,
                                                       @ApiParam(name = "project_id") @RequestParam(required = false, value = "project_id") String projectId,
                                                       @ApiParam(name = "firewall_policy_id") @RequestParam(required = false, value = "firewall_policy_id") String firewallPolicyId,
                                                       @ApiParam(name = "router_id") @RequestParam(required = false, value = "router_id") String routerId,
                                                       @ApiParam(name = "status") @RequestParam(required = false, value = "status") String status,
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
                    name, description, firewallPolicyId, routerId, status, null, null);

            ESFirewallsRsp firewallsRsp = firewallService.getFirewalls(cloudId, firewallSearchCritical, filterUserId);

            return ResponseEntity.ok(firewallsRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get firewalls failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @PostMapping(value = "/firewalls", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create firewall", response = Object.class)
    @LNJoyAuditLog(value = "Create firewall",
            resourceMapperId = CmpResources.ES_FIREWALL,
            actionDescriptionTemplate = CmpEasystackActionDescriptionTemplate.Descriptions.CREATE_FIREWALL,
            associatedResourceCnName = "防火墙",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                    resourceIdParseSPEL = "#obj?.body?.get('securityGroup')?.get('id')?.toString()", resourcePrimaryKeyClass = TblCmpEsFirewallsKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpEsFirewallsKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> addFirewall(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                             @ApiParam(value = "firewall", required = true, name = "firewall") @RequestBody ESFirewallCreateReq request,
                                             @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                             @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("add firewall, cloud:{}, request:{}", cloudId, request);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return firewallService.addFirewall(cloudId, request, bpId, userId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add firewall failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/firewalls/{firewall_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Show firewall", response = Object.class)
    public ResponseEntity<ESFirewallRsp> getFirewall(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                     @ApiParam(value = "firewall_id", required = true, name = "firewall_id") @PathVariable("firewall_id") String firewallId,
                                                     @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get firewall. cloud:{}, firewall_id:{}", cloudId, firewallId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            ESFirewallRsp firewallRsp = firewallService.getFirewall(cloudId, firewallId, operUserId);

            return ResponseEntity.ok(firewallRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get firewall failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/firewalls/{firewall_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update firewall", response = Object.class)
    @LNJoyAuditLog(value = "Update firewall",
            resourceMapperId = CmpResources.ES_FIREWALL,
            actionDescriptionTemplate = CmpEasystackActionDescriptionTemplate.Descriptions.UPDATE_FIREWALL,
            associatedResourceCnName = "防火墙",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "firewallId", resourcePrimaryKeyClass = TblCmpEsFirewallsKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpEsFirewallsKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> updateFirewall(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                @ApiParam(value = "firewall_id", required = true, name = "firewall_id") @PathVariable(value = "firewall_id") String firewallId,
                                                @ApiParam(value = "firewall", required = true, name = "firewall") @RequestBody ESFirewallUpdateReq request,
                                                @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("update firewall, cloud:{}, request:{}", cloudId, request);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return firewallService.updateFirewall(cloudId, firewallId, request, operUserId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("update firewall failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @DeleteMapping(value = "/firewalls/{firewall_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Delete firewall", response = Object.class)
    @LNJoyAuditLog(value = "Delete firewall",
            resourceMapperId = CmpResources.ES_FIREWALL,
            actionDescriptionTemplate = CmpEasystackActionDescriptionTemplate.Descriptions.DELETE_FIREWALL,
            associatedResourceCnName = "防火墙",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "firewallId", resourcePrimaryKeyClass = TblCmpEsFirewallsKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpEsFirewallsKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> removeFirewall(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                 @ApiParam(value = "firewall_id", required = true, name = "firewall_id") @PathVariable("firewall_id")String firewallId,
                                                 @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("remove firewall, cloud:{}, firewall_id:{}", cloudId, firewallId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return firewallService.removeFirewall(cloudId, firewallId, operUserId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove firewall failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }
}
