package com.lnjoying.justice.cmp.controller.openstack.network;

import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSExtPortWithDetailsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSExtPortsWithDetailsRsp;
import com.lnjoying.justice.cmp.entity.search.openstack.OSPortSearchCritical;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.openstack.PortService;
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

import java.util.List;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isAdmin;

@RestSchema(schemaId = "osextensionport")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/extension/v2.0/ports")
@Api(value = "OpenStack Port Controller",tags = "OpenStack Port Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "os_ext_port"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "os_ext_ports"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "OpenStack端口"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpOsPorts")})})
public class OSExtPortController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private PortService portService;

    @Autowired
    private CloudService cloudService;

    @GetMapping(value = "/{port_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Show Port Details", response = Object.class)
    public ResponseEntity<OSExtPortWithDetailsRsp> getPortDetails(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
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

            OSExtPortWithDetailsRsp portWithDetails = portService.getExtPortDetails(cloudId, portId, fields, operUserId);
            return ResponseEntity.ok(portWithDetails);
        }
        catch (Exception e)
        {
            LOGGER.error("get port details failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List Ports", response = Object.class)
    public ResponseEntity<OSExtPortsWithDetailsRsp> getPorts(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
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
                                                          @ApiParam(name = "page_size") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                          @ApiParam(name = "page_num") @RequestParam(value = "pageNum", required = false) Integer pageNum,
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

            OSPortSearchCritical portSearchCritical = new OSPortSearchCritical(adminStateUp, hostId, description,
                    deviceId, deviceOwner, fixedIps, id, ipAllocation, macAddress, name, networkId, projectId, revisionNumber,
                    sortDir, sortKey, status, tenantId, tags, tagsAny, notTags, notTagsAny, fields, macLearningEnabled,
                    pageNum, pageSize);

            OSExtPortsWithDetailsRsp portsWithDetails = portService.getPortsPage(cloudId, portSearchCritical, filterUserId);

            return ResponseEntity.ok(portsWithDetails);
        }
        catch (Exception e)
        {
            LOGGER.error("get ports failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }
}
