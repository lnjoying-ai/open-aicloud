package com.lnjoying.justice.cmp.controller.openstack.network;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpOsPortsKey;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron.OSPortCreateReq;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron.OSPortUpdateReq;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSPortWithDetailsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSPortsWithDetailsRsp;
import com.lnjoying.justice.cmp.entity.search.openstack.OSPortSearchCritical;
import com.lnjoying.justice.cmp.handler.actiondescription.i18n.zh_cn.CmpOpenstackActionDescriptionTemplate;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.openstack.PortService;
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

import java.util.List;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isAdmin;

@RestSchema(schemaId = "osport")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/v2.0/ports")
@Api(value = "OpenStack Port Controller",tags = "OpenStack Port Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "os_port"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "os_ports"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "OpenStack端口"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpOsPorts")})})
public class OSPortController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private PortService portService;

    @Autowired
    private CloudService cloudService;

    @GetMapping(value = "/{port_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Show Port Details", response = Object.class)
    public ResponseEntity<OSPortWithDetailsRsp> getPortDetails(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                               @ApiParam(value = "port_id", required = true, name = "port_id") @PathVariable("port_id") String portId,
                                                               @ApiParam(name = "fields") @RequestParam(required = false, value = "fields") String fields,
                                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get port details. cloud:{}, port_id:{}", cloudId, portId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            OSPortWithDetailsRsp portWithDetails = portService.getPortDetails(cloudId, portId, fields, operUserId);
            return ResponseEntity.ok(portWithDetails);
        }
        catch (Exception e)
        {
            LOGGER.error("get port details failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/{port_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update Port", response = Object.class)
    @LNJoyAuditLog(value = "Update Port",
            resourceMapperId = CmpResources.OS_PORT,
            actionDescriptionTemplate = CmpOpenstackActionDescriptionTemplate.Descriptions.UPDATE_PORT,
            associatedResourceCnName = "端口",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "portId", resourcePrimaryKeyClass = TblCmpOsPortsKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpOsPortsKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> updatePort(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                @ApiParam(value = "port_id", required = true, name = "port_id") @PathVariable(value = "port_id") String portId,
                                                @ApiParam(value = "port", required = true, name = "port") @RequestBody OSPortUpdateReq request,
                                                @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("update port, cloud:{}, request:{}", cloudId, request);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return portService.updatePort(cloudId, portId, request, bpId, operUserId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("update port failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @DeleteMapping(value = "/{port_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Delete Port", response = Object.class)
    @LNJoyAuditLog(value = "Delete Port",
            resourceMapperId = CmpResources.OS_PORT,
            actionDescriptionTemplate = CmpOpenstackActionDescriptionTemplate.Descriptions.DELETE_PORT,
            associatedResourceCnName = "端口",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "portId", resourcePrimaryKeyClass = TblCmpOsPortsKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpOsPortsKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> removePort(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                @ApiParam(value = "port_id", required = true, name = "port_id") @PathVariable("port_id")String portId,
                                                @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("remove port, cloud:{}, port_id:{}", cloudId, portId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return portService.removePort(cloudId, portId, operUserId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove port failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List Ports", response = Object.class)
    public ResponseEntity<OSPortsWithDetailsRsp> getPorts(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                 @ApiParam(name = "admin_state_up") @RequestParam(required = false, value = "admin_state_up") Boolean adminStateUp,
                                                 @ApiParam(name = "binding:host_id") @RequestParam(required = false, value = "binding:host_id") String hostId,
                                                 @ApiParam(name = "description") @RequestParam(required = false, value = "description") String description,
                                                 @ApiParam(name = "device_id") @RequestParam(required = false, value = "device_id") String deviceId,
                                                 @ApiParam(name = "device_owner") @RequestParam(required = false, value = "device_owner") String deviceOwner,
                                                 @ApiParam(name = "fixed_ips") @RequestParam(required = false, value = "fixed_ips") List<String> fixedIps,
                                                 @ApiParam(name = "id") @RequestParam(required = false, value = "id") String id,
                                                 @ApiParam(name = "ip_allocation") @RequestParam(required = false, value = "ip_allocation") String ipAllocation,
                                                 @ApiParam(name = "mac_address") @RequestParam(required = false, value = "mac_address") String macAddress,
                                                 @ApiParam(name = "name") @RequestParam(required = false, value = "name") String name,
                                                 @ApiParam(name = "network_id") @RequestParam(required = false, value = "network_id") String networkId,
                                                 @ApiParam(name = "project_id") @RequestParam(required = false, value = "project_id") String projectId,
                                                 @ApiParam(name = "revision_number") @RequestParam(required = false, value = "revision_number") Integer revisionNumber,
                                                 @ApiParam(name = "sort_dir") @RequestParam(required = false, value = "sort_dir") String sortDir,
                                                 @ApiParam(name = "sort_key") @RequestParam(required = false, value = "sort_key") String sortKey,
                                                 @ApiParam(name = "status") @RequestParam(required = false, value = "status") String status,
                                                 @ApiParam(name = "tenant_id") @RequestParam(required = false, value = "tenant_id") String tenantId,
                                                 @ApiParam(name = "tags") @RequestParam(required = false, value = "tags") String tags,
                                                 @ApiParam(name = "tags-any") @RequestParam(required = false, value = "tags-any") String tagsAny,
                                                 @ApiParam(name = "not-tags") @RequestParam(required = false, value = "not-tags") String notTags,
                                                 @ApiParam(name = "not-tags-any") @RequestParam(required = false, value = "not-tags-any") String notTagsAny,
                                                 @ApiParam(name = "fields") @RequestParam(required = false, value = "fields") String fields,
                                                 @ApiParam(name = "mac_learning_enabled") @RequestParam(required = false, value = "mac_learning_enabled") Boolean macLearningEnabled,
                                                 @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get ports. cloud:{}", cloudId);

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

            OSPortSearchCritical portSearchCritical = new OSPortSearchCritical(adminStateUp, hostId, description,
                    deviceId, deviceOwner, fixedIps, id, ipAllocation, macAddress, name, networkId, projectId, revisionNumber,
                    sortDir, sortKey, status, tenantId, tags, tagsAny, notTags, notTagsAny, fields, macLearningEnabled,
                    null, null);

            OSPortsWithDetailsRsp portsWithDetails = portService.getPorts(cloudId, portSearchCritical, filterUserId);

            return ResponseEntity.ok(portsWithDetails);
        }
        catch (Exception e)
        {
            LOGGER.error("get ports failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create Port", response = Object.class)
    @LNJoyAuditLog(value = "Create Port",
            resourceMapperId = CmpResources.OS_PORT,
            actionDescriptionTemplate = CmpOpenstackActionDescriptionTemplate.Descriptions.CREATE_PORT,
            associatedResourceCnName = "端口",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                    resourceIdParseSPEL = "#obj?.body?.get('port')?.get('id')?.toString()", resourcePrimaryKeyClass = TblCmpOsPortsKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpOsPortsKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> addPort(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                        @ApiParam(value = "port", required = true, name = "port") @RequestBody OSPortCreateReq request,
                                                        @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                        @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("add port, cloud:{}, request:{}", cloudId, request);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return portService.addPort(cloudId, request, bpId, userId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add port failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }
}
