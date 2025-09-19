package com.lnjoying.justice.cmp.controller.openstack.network;

import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSExtNetworksWithDetailsRsp;
import com.lnjoying.justice.cmp.entity.search.openstack.OSNetworkSearchCritical;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.openstack.NetworkService;
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

@RestSchema(schemaId = "osextensionnetwork")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/extension/v2.0/networks")
@Api(value = "OpenStack Network Controller",tags = "OpenStack Network Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "os_ext_network"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "os_ext_networks"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "OpenStack网络"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpOsNetworks")})})
public class OSExtNetworkController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private NetworkService networkService;

    @Autowired
    private CloudService cloudService;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List Networks", response = Object.class)
    public ResponseEntity<OSExtNetworksWithDetailsRsp> getNetworks(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                   @ApiParam(name = "admin_state_up") @RequestParam(required = false, value = "admin_state_up") Boolean adminStateUp,
                                                                   @ApiParam(name = "id") @RequestParam(required = false, value = "id") String id,
                                                                   @ApiParam(name = "mtu") @RequestParam(required = false, value = "mtu") Integer mtu,
                                                                   @ApiParam(name = "name") @RequestParam(required = false, value = "name") String name,
                                                                   @ApiParam(name = "project_id") @RequestParam(required = false, value = "project_id") String projectId,
                                                                   @ApiParam(name = "provider:network_type") @RequestParam(required = false, value = "provider:network_type") String networkType,
                                                                   @ApiParam(name = "provider:physical_network") @RequestParam(required = false, value = "provider:physical_network") String physicalNetwork,
                                                                   @ApiParam(name = "provider:segmentation_id") @RequestParam(required = false, value = "provider:segmentation_id") Integer segmentationId,
                                                                   @ApiParam(name = "revision_number") @RequestParam(required = false, value = "revision_number") Integer revisionNumber,
                                                                   @ApiParam(name = "router:external") @RequestParam(required = false, value = "router:external") Boolean external,
                                                                   @ApiParam(name = "shared") @RequestParam(required = false, value = "shared") Boolean shared,
                                                                   @ApiParam(name = "status") @RequestParam(required = false, value = "status") String status,
                                                                   @ApiParam(name = "tenant_id") @RequestParam(required = false, value = "tenant_id") String tenantId,
                                                                   @ApiParam(name = "vlan_transparent") @RequestParam(required = false, value = "vlan_transparent") Boolean vlanTransparent,
                                                                   @ApiParam(name = "description") @RequestParam(required = false, value = "description") String description,
                                                                   @ApiParam(name = "is_default") @RequestParam(required = false, value = "is_default") Boolean isDefault,
                                                                   @ApiParam(name = "tags") @RequestParam(required = false, value = "tags") String tags,
                                                                   @ApiParam(name = "tags-any") @RequestParam(required = false, value = "tags-any") String tagsAny,
                                                                   @ApiParam(name = "not-tags") @RequestParam(required = false, value = "not-tags") String notTags,
                                                                   @ApiParam(name = "not-tags-any") @RequestParam(required = false, value = "not-tags-any") String notTagsAny,
                                                                   @ApiParam(name = "sort_dir") @RequestParam(required = false, value = "sort_dir") String sortDir,
                                                                   @ApiParam(name = "sort_key") @RequestParam(required = false, value = "sort_key") String sortKey,
                                                                   @ApiParam(name = "fields") @RequestParam(required = false, value = "fields") String fields,
                                                                   @ApiParam(name = "page_size") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                                   @ApiParam(name = "page_num") @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                                   @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get networks. cloud:{}", cloudId);

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

            OSNetworkSearchCritical networkSearchCritical = new OSNetworkSearchCritical(adminStateUp, id, mtu, name, projectId,
                    networkType, physicalNetwork, segmentationId, revisionNumber, external, shared, status, tenantId, vlanTransparent,
                    description, isDefault, tags, tagsAny, notTags, notTagsAny, sortDir, sortKey, fields, pageNum, pageSize);

            OSExtNetworksWithDetailsRsp networksWithDetails = networkService.getNetworksPage(cloudId, networkSearchCritical, filterUserId);

            return ResponseEntity.ok(networksWithDetails);
        }
        catch (Exception e)
        {
            LOGGER.error("get networks failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }
}
