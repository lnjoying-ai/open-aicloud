package com.lnjoying.justice.cmp.controller.openstack.network;

import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSExtSubnetsWithDetailsRsp;
import com.lnjoying.justice.cmp.entity.search.openstack.OSSubnetSearchCritical;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.openstack.SubnetService;
import com.lnjoying.justice.commonweb.controller.RestWebController;
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

@RestSchema(schemaId = "osextensionsubnet")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/extension/v2.0/subnets")
@Api(value = "OpenStack Subnet Controller",tags = "OpenStack Network Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "os_ext_subnet"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "os_ext_subnets"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "OpenStack子网"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpOsSubnets")})})
public class OSExtSubnetController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private SubnetService subnetService;

    @Autowired
    private CloudService cloudService;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List Subnets", response = Object.class)
    public ResponseEntity<OSExtSubnetsWithDetailsRsp> getSubnets(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                 @ApiParam(name = "id") @RequestParam(required = false, value = "id") String id,
                                                                 @ApiParam(name = "tenant_id") @RequestParam(required = false, value = "tenant_id") String tenantId,
                                                                 @ApiParam(name = "project_id") @RequestParam(required = false, value = "project_id") String projectId,
                                                                 @ApiParam(name = "name") @RequestParam(required = false, value = "name") String name,
                                                                 @ApiParam(name = "enable_dhcp") @RequestParam(required = false, value = "enable_dhcp") Boolean enableDhcp,
                                                                 @ApiParam(name = "network_id") @RequestParam(required = false, value = "network_id") String networkId,
                                                                 @ApiParam(name = "ip_version") @RequestParam(required = false, value = "ip_version") Integer ipVersion,
                                                                 @ApiParam(name = "gateway_ip") @RequestParam(required = false, value = "gateway_ip") String gatewayIp,
                                                                 @ApiParam(name = "cidr") @RequestParam(required = false, value = "cidr") String cidr,
                                                                 @ApiParam(name = "description") @RequestParam(required = false, value = "description") String description,
                                                                 @ApiParam(name = "ipv6_address_mode") @RequestParam(required = false, value = "ipv6_address_mode") String ipv6AddressMode,
                                                                 @ApiParam(name = "ipv6_ra_mode") @RequestParam(required = false, value = "ipv6_ra_mode") String ipv6RaMode,
                                                                 @ApiParam(name = "revision_number") @RequestParam(required = false, value = "revision_number") Integer revisionNumber,
                                                                 @ApiParam(name = "segment_id") @RequestParam(required = false, value = "segment_id") String segmentId,
                                                                 @ApiParam(name = "shared") @RequestParam(required = false, value = "shared") Boolean shared,
                                                                 @ApiParam(name = "sort_dir") @RequestParam(required = false, value = "sort_dir") String sortDir,
                                                                 @ApiParam(name = "sort_key") @RequestParam(required = false, value = "sort_key") String sortKey,
                                                                 @ApiParam(name = "subnetpool_id") @RequestParam(required = false, value = "subnetpool_id") String subnetpoolId,
                                                                 @ApiParam(name = "tags") @RequestParam(required = false, value = "tags") String tags,
                                                                 @ApiParam(name = "tags-any") @RequestParam(required = false, value = "tags-any") String tagsAny,
                                                                 @ApiParam(name = "not-tags") @RequestParam(required = false, value = "not-tags") String notTags,
                                                                 @ApiParam(name = "not-tags-any") @RequestParam(required = false, value = "not-tags-any") String notTagsAny,
                                                                 @ApiParam(name = "dns_publish_fixed_ip") @RequestParam(required = false, value = "dns_publish_fixed_ip") Boolean dnsPublishFixedIp,
                                                                 @ApiParam(name = "fields") @RequestParam(required = false, value = "fields") String fields,
                                                                 @ApiParam(name = "page_size") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                                 @ApiParam(name = "page_num") @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                                 @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get subnets. cloud:{}", cloudId);

            String filterUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                filterUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            OSSubnetSearchCritical subnetSearchCritical = new OSSubnetSearchCritical(id, tenantId, projectId, name, enableDhcp,
                    networkId, ipVersion, gatewayIp, cidr, description, ipv6AddressMode, ipv6RaMode, revisionNumber, segmentId,
                    shared, sortDir, sortKey, subnetpoolId, tags, tagsAny, notTags, notTagsAny, dnsPublishFixedIp, fields,
                    pageNum, pageSize);

            OSExtSubnetsWithDetailsRsp osSubnetsWithDetails = subnetService.getSubnetsPage(cloudId, subnetSearchCritical, filterUserId);

            return ResponseEntity.ok(osSubnetsWithDetails);
        }
        catch (Exception e)
        {
            LOGGER.error("get subnets failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }
}
