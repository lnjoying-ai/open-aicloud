package com.lnjoying.justice.cmp.controller.openstack.network;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpOsFloatingipsKey;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron.OSFloatingIPCreateReq;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron.OSFloatingIPUpdateReq;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron.OSPortForwardingCreateReq;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron.OSPortForwardingUpdateReq;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.*;
import com.lnjoying.justice.cmp.entity.search.openstack.OSFloatingIPSearchCritical;
import com.lnjoying.justice.cmp.entity.search.openstack.OSPortForwardingSearchCritical;
import com.lnjoying.justice.cmp.handler.actiondescription.i18n.zh_cn.CmpOpenstackActionDescriptionTemplate;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.openstack.FloatingIPService;
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

@RestSchema(schemaId = "osfloatingip")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/v2.0/floatingips")
@Api(value = "OpenStack Floating Ip Controller",tags = "OpenStack Floating Ip Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "os_floatingip"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "os_floatingips"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "OpenStack浮动IP"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpOsFloatingips")})})
public class OSFloatingIpController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private FloatingIPService floatingIPService;

    @Autowired
    private CloudService cloudService;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List Floating IPs", response = Object.class)
    public ResponseEntity<OSFloatingIpsWithDetailsRsp> getFloatingIPs(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
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
                    portId, sortDir, sortKey, tags, tagsAny, notTags, notTagsAny, fields, null, null);

            OSFloatingIpsWithDetailsRsp osFloatingIpsWithDetails = floatingIPService.getFloatingIPs(cloudId, floatingIPSearchCritical, filterUserId);

            return ResponseEntity.ok(osFloatingIpsWithDetails);
        }
        catch (Exception e)
        {
            LOGGER.error("get floating IPs failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create Floating IP", response = Object.class)
    @LNJoyAuditLog(value = "Create Floating IP",
            resourceMapperId = CmpResources.OS_FLOATINGIP,
            actionDescriptionTemplate = CmpOpenstackActionDescriptionTemplate.Descriptions.CREATE_FLOATINGIP,
            associatedResourceCnName = "浮动IP",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                    resourceIdParseSPEL = "#obj?.body?.get('floatingip')?.get('id')?.toString()", resourcePrimaryKeyClass = TblCmpOsFloatingipsKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpOsFloatingipsKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> addFloatingIP(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                          @ApiParam(value = "floatingip", required = true, name = "floatingip") @RequestBody OSFloatingIPCreateReq request,
                                          @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                          @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("add floating IP, cloud:{}, request:{}", cloudId, request);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return floatingIPService.addFloatingIP(cloudId, request, bpId, userId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add floating IP failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/{floatingip_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Show Floating IP details", response = Object.class)
    public ResponseEntity<OSFloatingIpWithDetailsRsp> getFloatingIPDetails(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
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

            OSFloatingIpWithDetailsRsp floatingIpWithDetails = floatingIPService.getFloatingIPDetails(cloudId, floatingIpId, operUserId);

            return ResponseEntity.ok(floatingIpWithDetails);
        }
        catch (Exception e)
        {
            LOGGER.error("get floating IP failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/{floatingip_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update Floating IP", response = Object.class)
    @LNJoyAuditLog(value = "Update Floating IP",
            resourceMapperId = CmpResources.OS_FLOATINGIP,
            actionDescriptionTemplate = CmpOpenstackActionDescriptionTemplate.Descriptions.UPDATE_FLOATINGIP,
            associatedResourceCnName = "浮动IP",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "floatingIpId", resourcePrimaryKeyClass = TblCmpOsFloatingipsKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpOsFloatingipsKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> updateFloatingIP(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                             @ApiParam(value = "floatingip_id", required = true, name = "floatingip_id") @PathVariable(value = "floatingip_id") String floatingIpId,
                                             @ApiParam(value = "floatingip", required = true, name = "floatingip") @RequestBody OSFloatingIPUpdateReq request,
                                             @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                             @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("update floating IP, cloud:{}, request:{}", cloudId, request);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return floatingIPService.updateFloatingIP(cloudId, floatingIpId, request, bpId, operUserId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add floating IP failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @DeleteMapping(value = "/{floatingip_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Delete Floating IP", response = Object.class)
    @LNJoyAuditLog(value = "Delete Floating IP",
            resourceMapperId = CmpResources.OS_FLOATINGIP,
            actionDescriptionTemplate = CmpOpenstackActionDescriptionTemplate.Descriptions.DELETE_FLOATINGIP,
            associatedResourceCnName = "浮动IP",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "floatingIpId", resourcePrimaryKeyClass = TblCmpOsFloatingipsKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpOsFloatingipsKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> removeFloatingIP(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                             @ApiParam(value = "floatingip_id", required = true, name = "floatingip_id") @PathVariable("floatingip_id")String floatingIpId,
                                             @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("remove floating IP, cloud:{}, floatingip_id:{}", cloudId, floatingIpId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return floatingIPService.removeFloatingIP(cloudId, floatingIpId, operUserId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove floating IP failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/{floatingip_id}/port_forwardings/{port_forwarding_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Show Port Forwarding", response = Object.class)
    public ResponseEntity<OSPortForwardingWithDetailsRsp> getPortForwarding(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                            @ApiParam(value = "floatingip_id", required = true, name = "floatingip_id") @PathVariable("floatingip_id") String floatingIpId,
                                                                            @ApiParam(value = "port_forwarding_id", required = true, name = "port_forwarding_id") @PathVariable("port_forwarding_id") String portForwardingId,
                                                                            @ApiParam(name = "fields") @RequestParam(required = false, value = "fields") String fields,
                                                                            @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get port forwarding. cloud:{}, floatingip_id:{}", cloudId, floatingIpId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            OSPortForwardingWithDetailsRsp tblCmpFlavor = floatingIPService.getPortForwarding(cloudId, floatingIpId, portForwardingId, fields, operUserId);
            return ResponseEntity.ok(tblCmpFlavor);
        }
        catch (Exception e)
        {
            LOGGER.error("get port forwarding failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/{floatingip_id}/port_forwardings/{port_forwarding_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update A Port Forwarding", response = Object.class)
    @LNJoyAuditLog(value = "Update A Port Forwarding",
            resourceMapperId = CmpResources.OS_FLOATINGIP,
            actionDescriptionTemplate = CmpOpenstackActionDescriptionTemplate.Descriptions.UPDATE_PORTFORWARDING,
            associatedResourceCnName = "浮动IP",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "floatingipId", resourcePrimaryKeyClass = TblCmpOsFloatingipsKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpOsFloatingipsKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> updatePortForwarding(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                @ApiParam(value = "floatingip_id", required = true, name = "floatingip_id") @PathVariable(value = "floatingip_id") String floatingipId,
                                                @ApiParam(value = "port_forwarding_id", required = true, name = "port_forwarding_id") @PathVariable(value = "port_forwarding_id") String portForwardingId,
                                                @ApiParam(value = "port_forwarding", required = true, name = "port_forwarding") @RequestBody OSPortForwardingUpdateReq request,
                                                @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("update port forwarding, cloud:{}, request:{}", cloudId, request);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return floatingIPService.updatePortForwarding(cloudId, floatingipId, portForwardingId, request, operUserId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("update port forwarding failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @DeleteMapping(value = "/{floatingip_id}/port_forwardings/{port_forwarding_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Delete A Floating IP Port Forwarding", response = Object.class)
    @LNJoyAuditLog(value = "Delete A Floating IP Port Forwarding",
            resourceMapperId = CmpResources.OS_FLOATINGIP,
            actionDescriptionTemplate = CmpOpenstackActionDescriptionTemplate.Descriptions.DELETE_PORTFORWARDING,
            associatedResourceCnName = "浮动IP",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "floatingipId", resourcePrimaryKeyClass = TblCmpOsFloatingipsKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpOsFloatingipsKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> removePortForwarding(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                @ApiParam(value = "floatingip_id", required = true, name = "floatingip_id") @PathVariable("floatingip_id")String floatingipId,
                                                @ApiParam(value = "port_forwarding_id", required = true, name = "port_forwarding_id") @PathVariable("port_forwarding_id")String portForwardingId,
                                                @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("remove port forwarding, cloud:{}, floatingip_id:{}", cloudId, floatingipId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return floatingIPService.removePortForwarding(cloudId, floatingipId, portForwardingId, operUserId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove port forwarding failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/{floatingip_id}/port_forwardings", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List Floating IP Port Forwardings", response = Object.class)
    public ResponseEntity<OSPortForwardingsWithDetailsRsp> getPortForwardings(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                              @ApiParam(value = "floatingip_id", required = true, name = "floatingip_id") @PathVariable("floatingip_id")String floatingipId,
                                                                              @ApiParam(name = "id") @RequestParam(required = false, value = "id") String id,
                                                                              @ApiParam(name = "internal_port_id") @RequestParam(required = false, value = "internal_port_id") String internalPortId,
                                                                              @ApiParam(name = "external_port") @RequestParam(required = false, value = "external_port") Integer externalPort,
                                                                              @ApiParam(name = "external_port_range") @RequestParam(required = false, value = "external_port_range") String externalPortRange,
                                                                              @ApiParam(name = "protocol") @RequestParam(required = false, value = "protocol") String protocol,
                                                                              @ApiParam(name = "sort_key") @RequestParam(required = false, value = "sort_key") String sortKey,
                                                                              @ApiParam(name = "sort_dir") @RequestParam(required = false, value = "sort_dir") String sortDir,
                                                                              @ApiParam(name = "fields") @RequestParam(required = false, value = "fields") String fields,
                                                                              @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get port forwardings. cloud:{}", cloudId);

            String filterUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                filterUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            OSPortForwardingSearchCritical portForwardingSearchCritical = new OSPortForwardingSearchCritical(id, internalPortId,
                    externalPort, externalPortRange, protocol, sortDir, sortKey, fields);

            OSPortForwardingsWithDetailsRsp osPortForwardingsWithDetails = floatingIPService.getPortForwardings(cloudId, portForwardingSearchCritical, filterUserId);

            return ResponseEntity.ok(osPortForwardingsWithDetails);
        }
        catch (Exception e)
        {
            LOGGER.error("get port forwardings failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @PostMapping(value = "/{floatingip_id}/port_forwardings", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create Port Forwarding", response = Object.class)
    @LNJoyAuditLog(value = "Create Port Forwarding",
            resourceMapperId = CmpResources.OS_FLOATINGIP,
            actionDescriptionTemplate = CmpOpenstackActionDescriptionTemplate.Descriptions.CREATE_PORTFORWARDING,
            associatedResourceCnName = "浮动IP",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "floatingipId", resourcePrimaryKeyClass = TblCmpOsFloatingipsKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpOsFloatingipsKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> addPortForwarding(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                    @ApiParam(value = "floatingip_id", required = true, name = "floatingip_id") @PathVariable("floatingip_id")String floatingipId,
                                                        @ApiParam(value = "port_forwarding", required = true, name = "port_forwarding") @RequestBody OSPortForwardingCreateReq request,
                                                        @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                        @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("add port forwarding, cloud:{}, request:{}", cloudId, request);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return floatingIPService.addPortForwarding(cloudId, floatingipId, request, bpId, userId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add port forwarding failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }
}
