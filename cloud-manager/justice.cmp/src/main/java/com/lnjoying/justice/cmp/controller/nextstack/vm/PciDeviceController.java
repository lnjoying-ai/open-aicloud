package com.lnjoying.justice.cmp.controller.nextstack.vm;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpPciDeviceKey;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.vm.PciDeviceAttachReq;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.vm.PciDeviceDetailInfo;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm.HypervisorNodeInfo;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm.PciDeviceInfo;
import com.lnjoying.justice.cmp.entity.search.nextstack.vm.PciDeviceSearchCritical;
import com.lnjoying.justice.cmp.handler.actiondescription.i18n.zh_cn.CmpNextStackActionDescriptionTemplate;
import com.lnjoying.justice.cmp.service.nextstack.vm.PciDeviceServiceBiz;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.commonweb.controller.RestWebController;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import com.lnjoying.justice.schema.entity.search.PageSearchCritical;
import io.micrometer.core.instrument.util.StringUtils;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isAdmin;

@RestSchema(schemaId = "pciDevice")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/api/vm/v1/")
@Api(value = "PCI Device Controller",tags = {"PCI Device Controller"})
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "ns_pci_device"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "ns_pci_devices"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "PCI设备"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpPciDevice")})})
public class PciDeviceController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private PciDeviceServiceBiz pciDeviceServiceBiz;

    @Autowired
    private CloudService cloudService;

    @GetMapping(value = "/pci_devices", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get device", response = Object.class)
    public ResponseEntity<List<PciDeviceDetailInfo>> getDevices(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                @ApiParam(name = "page_size") @RequestParam(required = false,value = "page_size")  Integer pageSize,
                                                                @ApiParam(name = "page_num") @RequestParam(required = false,value = "page_num")  Integer pageNum,
                                                                @ApiParam(name = "device_name") @RequestParam(required = false,value = "device_name") String pciDeviceName,
                                                                @ApiParam(name = "node_id") @RequestParam(required = true,value = "node_id") String nodeId,
                                                                @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.debug("get pci device list");
            PciDeviceSearchCritical pageSearchCritical = new PciDeviceSearchCritical();
            if (!StringUtils.isBlank(pciDeviceName))pageSearchCritical.setPciDeviceName(pciDeviceName);
            if (pageNum != null) pageSearchCritical.setPageNum(pageNum);
            if (pageSize != null) pageSearchCritical.setPageSize(pageSize);

            List<PciDeviceDetailInfo> pciDeviceInfos  = pciDeviceServiceBiz.getPciDevices(cloudId, pageSearchCritical, nodeId);
            return ResponseEntity.ok(pciDeviceInfos);
        }
        catch (Exception e)
        {
            LOGGER.error("get devices error: {}",e.getMessage());
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/pci_devices/{device_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get device", response = Object.class)
    public ResponseEntity<PciDeviceDetailInfo> getDevice(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                         @ApiParam(value = "device_id", required = true, name = "device_id") @PathVariable("device_id")String deviceId,
                                                         @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.info("get pci device detail, cloud_id: {}, device_id: {}", cloudId, deviceId);
            PciDeviceDetailInfo pciDeviceDetailInfo = pciDeviceServiceBiz.getPciDeviceDetailInfo(cloudId, deviceId);
            return ResponseEntity.ok(pciDeviceDetailInfo);
        }
        catch (Exception e)
        {
            LOGGER.error("get device error: {}",e.getMessage());
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/pci_devices/{device_id}/attach", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "attach pci device")
    @LNJoyAuditLog(value = "attach pci device",
            resourceMapperId = CmpResources.NS_PCI_DEVICE,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.ATTACH_PCI_DEVICE,
            associatedResourceCnName = "PCI设备",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "deviceId", resourcePrimaryKeyClass = TblCmpPciDeviceKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpPciDeviceKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> attachDevice(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                               @ApiParam(value = "device_id", required = true, name = "device_id") @PathVariable("device_id")String deviceId,
                                               @ApiParam(value = "pciDeviceAttachReq", required = true, name = "pciDeviceAttachReq")@RequestBody @Valid PciDeviceAttachReq pciDeviceAttachReq,
                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {

        try
        {
            LOGGER.info("attach pci device: deviceId:{} vmId:{}", deviceId, pciDeviceAttachReq.getVmInstanceId());

            return cloudService.sendHttpRequestToCloud(cloudId);
        }
        catch (Exception e)
        {
            LOGGER.error("attach devices error: {}",e.getMessage());
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/pci_devices/{device_id}/detach", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "detach pci device")
    @LNJoyAuditLog(value = "detach pci device",
            resourceMapperId = CmpResources.NS_PCI_DEVICE,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.DETACH_PCI_DEVICE,
            associatedResourceCnName = "PCI设备",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "deviceId", resourcePrimaryKeyClass = TblCmpPciDeviceKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpPciDeviceKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> detachVolume(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                               @ApiParam(value = "device_id", required = true, name = "device_id") @PathVariable("device_id")String deviceId)
    {

        try
        {
            LOGGER.info("detach pci device: deviceId:{} ", deviceId);

            return cloudService.sendHttpRequestToCloud(cloudId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("detach device error: {}",e.getMessage());
            throw throwWebException(e);
        }

    }

    @GetMapping(value = "/pci_devices/available_nodes", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get device", response = Object.class)
    public ResponseEntity<List<HypervisorNodeInfo>> getAvailableNodes(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                      @ApiParam(name = "is_gpu") @RequestParam(required = true,value = "is_gpu") Boolean isGpu,
                                                                      @ApiParam(name = "vm_id") @RequestParam(required = false,value = "vm_id") String vmId,
                                                                      @ApiParam(name = "page_size") @RequestParam(required = false,value = "page_size")  Integer pageSize,
                                                                      @ApiParam(name = "page_num") @RequestParam(required = false,value = "page_num")  Integer pageNum,
                                                                      @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            String filterUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                filterUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            PageSearchCritical pageSearchCritical = new PciDeviceSearchCritical();
            if (pageNum != null) pageSearchCritical.setPageNum(pageNum);
            if (pageSize != null) pageSearchCritical.setPageSize(pageSize);

            return ResponseEntity.ok(pciDeviceServiceBiz.getAvailableNode(cloudId, pageSearchCritical,vmId, filterUserId, isGpu));
        }
        catch (Exception e)
        {
            LOGGER.error("get devices error: {}",e.getMessage());
            throw throwWebException(e);
        }
    }


    @GetMapping(value = "/pci_devices/available_devices", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get device", response = Object.class)
    public ResponseEntity<List<PciDeviceInfo>> getAvailableDevices(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                   @ApiParam(name = "vm_id") @RequestParam(required = false,value = "vm_id") String vmId,
                                                                   @ApiParam(name = "node_id") @RequestParam(required = true,value = "node_id") String nodeId,
                                                                   @ApiParam(name = "page_size") @RequestParam(required = false,value = "page_size")  Integer pageSize,
                                                                   @ApiParam(name = "page_num") @RequestParam(required = false,value = "page_num")  Integer pageNum,
                                                                   @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            String filterUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                filterUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            PageSearchCritical pageSearchCritical = new PciDeviceSearchCritical();
            if (pageNum != null) pageSearchCritical.setPageNum(pageNum);
            if (pageSize != null) pageSearchCritical.setPageSize(pageSize);

            return ResponseEntity.ok(pciDeviceServiceBiz.getAvailableDeviceInfos(cloudId, pageSearchCritical, nodeId, vmId, filterUserId));
        }
        catch (Exception e)
        {
            LOGGER.error("get devices error: {}",e.getMessage());
            throw throwWebException(e);
        }
    }
}
