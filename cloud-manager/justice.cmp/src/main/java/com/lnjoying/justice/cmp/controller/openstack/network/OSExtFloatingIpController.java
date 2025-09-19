package com.lnjoying.justice.cmp.controller.openstack.network;

import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.*;
import com.lnjoying.justice.cmp.entity.search.openstack.OSFloatingIPSearchCritical;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.openstack.FloatingIPService;
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

@RestSchema(schemaId = "osextensionfloatingip")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/extension/v2.0/floatingips")
@Api(value = "OpenStack Floating Ip Controller",tags = "OpenStack Floating Ip Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "os_ext_floatingip"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "os_ext_floatingips"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "OpenStack浮动IP"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpOsFloatingips")})})
public class OSExtFloatingIpController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private FloatingIPService floatingIPService;

    @Autowired
    private CloudService cloudService;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List Floating IPs", response = Object.class)
    public ResponseEntity<OSExtFloatingIpsWithDetailsRsp> getFloatingIPs(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                      @ApiParam(name = "id") @RequestParam(required = false, value = "id") String id,
                                                                      @ApiParam(name = "router_id") @RequestParam(required = false, value = "router_id") String routerId,
                                                                      @ApiParam(name = "status") @RequestParam(required = false, value = "status") String status,
                                                                      @ApiParam(name = "tenant_id") @RequestParam(required = false, value = "tenant_id") String tenantId,
                                                                      @ApiParam(name = "project_id") @RequestParam(required = false, value = "project_id") String projectId,
                                                                      @ApiParam(name = "revision_number") @RequestParam(required = false, value = "revision_number") Integer revisionNumber,
                                                                      @ApiParam(name = "description") @RequestParam(required = false, value = "description") String description,
                                                                      @ApiParam(name = "distributed") @RequestParam(required = false, value = "distributed") Boolean distributed,
                                                                      @ApiParam(name = "floating_network_id") @RequestParam(required = false, value = "floating_network_id") String floatingNetworkId,
                                                                      @ApiParam(name = "fixed_ip_address") @RequestParam(required = false, value = "fixed_ip_address") String fixedIpAddress,
                                                                      @ApiParam(name = "floating_ip_address") @RequestParam(required = false, value = "floating_ip_address") String floatingIpAddress,
                                                                      @ApiParam(name = "port_id") @RequestParam(required = false, value = "port_id") String portId,
                                                                      @ApiParam(name = "sort_dir") @RequestParam(required = false, value = "sort_dir") String sortDir,
                                                                      @ApiParam(name = "sort_key") @RequestParam(required = false, value = "sort_key") String sortKey,
                                                                      @ApiParam(name = "tags") @RequestParam(required = false, value = "tags") String tags,
                                                                      @ApiParam(name = "tags-any") @RequestParam(required = false, value = "tags-any") String tagsAny,
                                                                      @ApiParam(name = "not-tags") @RequestParam(required = false, value = "not-tags") String notTags,
                                                                      @ApiParam(name = "not-tags-any") @RequestParam(required = false, value = "not-tags-any") String notTagsAny,
                                                                      @ApiParam(name = "fields") @RequestParam(required = false, value = "fields") String fields,
                                                                      @ApiParam(name = "page_size") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                                      @ApiParam(name = "page_num") @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                                      @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get floating IPs. cloud:{}", cloudId);

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

            OSFloatingIPSearchCritical floatingIPSearchCritical = new OSFloatingIPSearchCritical(id, routerId, status, tenantId,
                    projectId, revisionNumber, description, distributed, floatingNetworkId, fixedIpAddress, floatingIpAddress,
                    portId, sortDir, sortKey, tags, tagsAny, notTags, notTagsAny, fields, pageNum, pageSize);

            OSExtFloatingIpsWithDetailsRsp osFloatingIpsWithDetails = floatingIPService.getFloatingIPsPage(cloudId, floatingIPSearchCritical, filterUserId);

            return ResponseEntity.ok(osFloatingIpsWithDetails);
        }
        catch (Exception e)
        {
            LOGGER.error("get floating IPs failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/{floatingip_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Show Floating IP details", response = Object.class)
    public ResponseEntity<OSExtFloatingIpWithDetailsRsp> getFloatingIPDetails(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                           @ApiParam(value = "floatingip_id", required = true, name = "floatingip_id") @PathVariable("floatingip_id") String floatingIpId,
                                                                              @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get floating IP info. cloud:{}, floatingip_id:{}", cloudId, floatingIpId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            OSExtFloatingIpWithDetailsRsp floatingIpWithDetails = floatingIPService.getExtFloatingIPDetails(cloudId, floatingIpId, operUserId);

            return ResponseEntity.ok(floatingIpWithDetails);
        }
        catch (Exception e)
        {
            LOGGER.error("get floating IP failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }
}
