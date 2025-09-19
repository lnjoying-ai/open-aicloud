package com.lnjoying.justice.cmp.controller.openstack.network;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpOsSubnetsKey;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron.OSSubnetCreateReq;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron.OSSubnetUpdateReq;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSSubnetWithDetailsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSSubnetsWithDetailsRsp;
import com.lnjoying.justice.cmp.entity.search.openstack.OSSubnetSearchCritical;
import com.lnjoying.justice.cmp.handler.actiondescription.i18n.zh_cn.CmpOpenstackActionDescriptionTemplate;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.openstack.SubnetService;
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

@RestSchema(schemaId = "ossubnet")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/v2.0/subnets")
@Api(value = "OpenStack Subnet Controller",tags = "OpenStack Network Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "os_subnet"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "os_subnets"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "OpenStack子网"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpOsSubnets")})})
public class OSSubnetController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private SubnetService subnetService;

    @Autowired
    private CloudService cloudService;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List Subnets", response = Object.class)
    public ResponseEntity<OSSubnetsWithDetailsRsp> getSubnets(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
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

            if (StringUtils.isEmpty(projectId))
            {
                projectId = OSClientUtil.getOSProject(cloudId);
            }

            OSSubnetSearchCritical subnetSearchCritical = new OSSubnetSearchCritical(id, tenantId, projectId, name, enableDhcp,
                    networkId, ipVersion, gatewayIp, cidr, description, ipv6AddressMode, ipv6RaMode, revisionNumber, segmentId,
                    shared, sortDir, sortKey, subnetpoolId, tags, tagsAny, notTags, notTagsAny, dnsPublishFixedIp, fields,
                    null, null);

            OSSubnetsWithDetailsRsp osSubnetsWithDetails = subnetService.getSubnets(cloudId, subnetSearchCritical, filterUserId);

            return ResponseEntity.ok(osSubnetsWithDetails);
        }
        catch (Exception e)
        {
            LOGGER.error("get subnets failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create Subnet", response = Object.class)
    @LNJoyAuditLog(value = "Create Subnet",
            resourceMapperId = CmpResources.OS_SUBNET,
            actionDescriptionTemplate = CmpOpenstackActionDescriptionTemplate.Descriptions.CREATE_SUBNET,
            associatedResourceCnName = "子网",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                    resourceIdParseSPEL = "#obj?.body?.get('subnet')?.get('id')?.toString()", resourcePrimaryKeyClass = TblCmpOsSubnetsKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpOsSubnetsKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> addSubnet(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                        @ApiParam(value = "subnet", required = true, name = "subnet") @RequestBody OSSubnetCreateReq request,
                                                        @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                        @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("add subnet, cloud:{}, request:{}", cloudId, request);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return subnetService.addSubnet(cloudId, request, bpId, userId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add subnet failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/{subnet_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Show Subnet Details", response = Object.class)
    public ResponseEntity<OSSubnetWithDetailsRsp> getSubnetDetails(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                   @ApiParam(value = "subnet_id", required = true, name = "subnet_id") @PathVariable("subnet_id") String subnetId,
                                                                   @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get subnet details. cloud:{}, subnet_id:{}", cloudId, subnetId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            OSSubnetWithDetailsRsp subnetWithDetails = subnetService.getSubnetDetails(cloudId, subnetId, operUserId);

            return ResponseEntity.ok(subnetWithDetails);
        }
        catch (Exception e)
        {
            LOGGER.error("get subnet details failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/{subnet_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update Subnet", response = Object.class)
    @LNJoyAuditLog(value = "Update Subnet",
            resourceMapperId = CmpResources.OS_SUBNET,
            actionDescriptionTemplate = CmpOpenstackActionDescriptionTemplate.Descriptions.UPDATE_SUBNET,
            associatedResourceCnName = "子网",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "subnetId", resourcePrimaryKeyClass = TblCmpOsSubnetsKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpOsSubnetsKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> updateSubnet(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                               @ApiParam(value = "subnet_id", required = true, name = "subnet_id") @PathVariable(value = "subnet_id") String subnetId,
                                                        @ApiParam(value = "subnet", required = true, name = "subnet") @RequestBody OSSubnetUpdateReq request,
                                                        @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                        @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("update subnet, cloud:{}, request:{}", cloudId, request);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return subnetService.updateSubnet(cloudId, subnetId, request, bpId, operUserId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("update subnet failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @DeleteMapping(value = "/{subnet_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Delete Subnet", response = Object.class)
    @LNJoyAuditLog(value = "Delete Subnet",
            resourceMapperId = CmpResources.OS_SUBNET,
            actionDescriptionTemplate = CmpOpenstackActionDescriptionTemplate.Descriptions.DELETE_SUBNET,
            associatedResourceCnName = "子网",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "subnetId", resourcePrimaryKeyClass = TblCmpOsSubnetsKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpOsSubnetsKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> removeSubnet(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                               @ApiParam(value = "subnet_id", required = true, name = "subnet_id") @PathVariable("subnet_id")String subnetId,
                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("remove subnet, cloud:{}, subnet_id:{}", cloudId, subnetId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return subnetService.removeSubnet(cloudId, subnetId, operUserId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove subnet failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }
}
