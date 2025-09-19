package com.lnjoying.justice.cmp.controller.nextstack.baremetal;

import com.lnjoying.justice.cmp.common.BaremetalDeviceStatus;
import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.config.DescriptionConfig;
import com.lnjoying.justice.cmp.db.model.TblCmpBaremetalDeviceKey;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.baremetal.CommonReq;
import com.lnjoying.justice.cmp.entity.search.nextstack.baremetal.BaremetalDeviceSearchCritical;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.baremetal.BaremetalDeviceAddReq;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.baremetal.BaremetalDeviceDetailInfoRsp;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.baremetal.BaremetalDevicesConfigsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.baremetal.BaremetalDevicesRsp;
import com.lnjoying.justice.cmp.handler.actiondescription.i18n.zh_cn.CmpNextStackActionDescriptionTemplate;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.nextstack.baremetal.BaremetalComputeService;
import com.lnjoying.justice.commonweb.controller.RestWebController;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isAdmin;

@RestSchema(schemaId = "baremetaldevice")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/api/bm/v1")
@Api(value = "Baremetal Device Controller",tags = "Baremetal Device Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "ns_baremetal_device"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "ns_baremetal_devices"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "裸金属设备"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpBaremetalDevice")})})
public class BaremetalDeviceController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private BaremetalComputeService baremetalComputeService;

    @Autowired
    private CloudService cloudService;

    @PostMapping(value = "/baremetal_devices", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "add baremetal device", response = Object.class, notes = DescriptionConfig.ADD_BAREMETAL_DEVICE)
    @LNJoyAuditLog(value = "add baremetal device",
            resourceMapperId = CmpResources.NS_BAREMETAL_DEVICE,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.ADD_BAREMETAL_DEVICE,
            associatedResourceCnName = "裸金属设备",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                    resourceIdParseSPEL = "#obj?.body?.get('deviceId')?.toString()", resourcePrimaryKeyClass = TblCmpBaremetalDeviceKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpBaremetalDeviceKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> addBaremetalDevice(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                     @ApiParam(value = "BaremetalDeviceAddReq", required = true, name = "BaremetalDeviceAddReq") @RequestBody BaremetalDeviceAddReq request,
                                                     @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                     @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("add baremetal device, cloud:{}, req:{}", cloudId, request);

            return baremetalComputeService.addBaremetalDevice(cloudId, request, bpId, userId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add baremetal device failed, message:{}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/baremetal_devices/{deviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update baremetal device", response = Object.class)
    @LNJoyAuditLog(value = "update baremetal device",
            resourceMapperId = CmpResources.NS_BAREMETAL_DEVICE,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.UPDATE_BAREMETAL_DEVICE,
            associatedResourceCnName = "裸金属设备",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "deviceId", resourcePrimaryKeyClass = TblCmpBaremetalDeviceKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpBaremetalDeviceKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> updateInstance(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                 @ApiParam(value = "deviceId", required = true, name = "instanceId") @PathVariable("deviceId") String deviceId,
                                                 @ApiParam(value = "CommonReq", required = true, name = "CommonReq") @RequestBody CommonReq request,
                                                 @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.debug("put baremetal device: {}, userId:{}", request, userId);

            return cloudService.sendHttpRequestToCloud(cloudId);
        }
        catch (Exception e)
        {
            LOGGER.error("update baremetal device error: {}, deviceId: {}",e.getMessage(), deviceId);
            throw  throwWebException(e);
        }
    }

    @GetMapping(value = "/baremetal_available_devices", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get baremetal device detail info", response = Object.class, notes = DescriptionConfig.GET_BAREMETAL_DEVICE)
    public ResponseEntity<BaremetalDevicesRsp> getBaremetalAvailableDevice(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                           @ApiParam(name = "page_size") @RequestParam(required = false, value = "page_size") Integer pageSize,
                                                                           @ApiParam(name = "page_num") @RequestParam(required = false, value = "page_num") Integer pageNum,
                                                                           @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get available baremetal devices, cloud:{}", cloudId);

            BaremetalDeviceSearchCritical critical = new BaremetalDeviceSearchCritical();
            if (null != pageSize) critical.setPageSize(pageSize);
            if (null != pageNum) critical.setPageNum(pageNum);
            critical.setBaremetalDevicePhaseStatus(BaremetalDeviceStatus.DEVICE_INSPECTED_SUCCESS);
            critical.setNicSpecPhaseStatus(BaremetalDeviceStatus.DEVICE_PXE_SLOT_CREATED_SUCCESS);
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                critical.setUserId(null);
            }
            else
            {
                critical.setUserId(userId);
            }
            BaremetalDevicesRsp getBaremetalDevicesRsp = baremetalComputeService.getBaremetalDevices(cloudId, critical);
            if (getBaremetalDevicesRsp.getTotalNum() > 0)
            {
                getBaremetalDevicesRsp.getDevices().forEach(
                        baremetalDeviceInfo ->
                        {
                            baremetalDeviceInfo.setPhaseStatus(null);
                            baremetalDeviceInfo.setIpmiIp(null);
                            baremetalDeviceInfo.setIpmiPort(null);
                            baremetalDeviceInfo.setCreateTime(null);
                            baremetalDeviceInfo.setUpdateTime(null);
                        }
                );
            }
            return ResponseEntity.ok().body(getBaremetalDevicesRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get available baremetal devices failed, message:{}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/baremetal_devices_configs", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get baremetal devices configuration", response = Object.class, notes = DescriptionConfig.GET_BAREMETAL_DEVICES)
    public ResponseEntity<BaremetalDevicesConfigsRsp> getBaremetalDeviceConfigs(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId)
    {
        try
        {
            LOGGER.info("get baremetal devices configs, cloud:{}", cloudId);

            BaremetalDevicesConfigsRsp getBaremetalDevicesConfigsRsp = baremetalComputeService.getBaremetalDevicesConfigsRsp(cloudId);
            return ResponseEntity.ok().body(getBaremetalDevicesConfigsRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get baremetal devices configs failed, messgae:{}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }


    @GetMapping(value = "/baremetal_devices", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get baremetal devices", response = Object.class, notes = DescriptionConfig.GET_BAREMETAL_DEVICES)
    public ResponseEntity<BaremetalDevicesRsp> getBaremetalDevices(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                   @ApiParam(name = "baremetalDevicePhaseStatus") @RequestParam(required = false) Integer baremetalDevicePhaseStatus,
                                                                   @ApiParam(name = "nicSpec_phaseStatus") @RequestParam(required = false, value = "nicSpec_phaseStatus") Integer nicSpecPhaseStatus,
                                                                   @ApiParam(name = "user_id") @RequestParam(required = false, value = "user_id") String userId,
                                                                   @ApiParam(name = "cpu_num") @RequestParam(required = false, value = "cpu_num") Integer cpuNum,
                                                                   @ApiParam(name = "mem_total") @RequestParam(required = false, value = "mem_total") Long memTotal,
                                                                   @ApiParam(name = "page_size") @RequestParam(required = false, value = "page_size") Integer pageSize,
                                                                   @ApiParam(name = "page_num") @RequestParam(required = false, value = "page_num") Integer pageNum,
                                                                   @RequestHeader(name = UserHeadInfo.USERID, required = false) String operUserId)
    {
        try
        {
            LOGGER.info("get baremetal devices, cloud:{}", cloudId);

            BaremetalDeviceSearchCritical critical = new BaremetalDeviceSearchCritical();
            if (null != baremetalDevicePhaseStatus) critical.setBaremetalDevicePhaseStatus(baremetalDevicePhaseStatus);
            if (null != nicSpecPhaseStatus) critical.setNicSpecPhaseStatus(nicSpecPhaseStatus);
            if (isAdmin() || cloudService.isOwner(cloudId, operUserId))
            {
                if (null != userId) critical.setUserId(userId);
            }
            else
            {
                critical.setUserId(operUserId);
            }
            if (null != cpuNum) critical.setCpuNum(cpuNum);
            if (null != memTotal) critical.setMemTotal(memTotal);
            if (null != pageSize) critical.setPageSize(pageSize);
            if (null != pageNum) critical.setPageNum(pageNum);

            return ResponseEntity.ok().body(baremetalComputeService.getBaremetalDevices(cloudId, critical));
        }
        catch (Exception e)
        {
            LOGGER.error("get baremetal devices failed, message:{}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/baremetal_devices/{deviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get baremetal device detail info", response = Object.class, notes = DescriptionConfig.GET_BAREMETAL_DEVICE)
    public ResponseEntity<BaremetalDeviceDetailInfoRsp> getBaremetalDevice(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                           @ApiParam(value = "deviceId", required = true, name = "deviceId") @PathVariable("deviceId") String deviceId) throws IOException
    {
        try
        {
            LOGGER.info("get baremetal device detail info, cloud:{}, device:{}", cloudId, deviceId);

            return ResponseEntity.ok().body(baremetalComputeService.getBaremetalDevice(cloudId, deviceId));
        }
        catch (Exception e)
        {
            LOGGER.error("get baremetal device failed, message:{}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @DeleteMapping(value = "/baremetal_devices/{deviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete baremetal device", response = Object.class, notes = DescriptionConfig.GET_BAREMETAL_DEVICE)
    @LNJoyAuditLog(value = "delete baremetal device",
            resourceMapperId = CmpResources.NS_BAREMETAL_DEVICE,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.DELETE_BAREMETAL_DEVICE,
            associatedResourceCnName = "裸金属设备",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "deviceId", resourcePrimaryKeyClass = TblCmpBaremetalDeviceKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpBaremetalDeviceKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> removeBaremetalDevice(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                        @ApiParam(value = "deviceId", required = true, name = "deviceId") @PathVariable("deviceId") String deviceId,
                                                        @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("delete baremetal device, cloud:{}, device:{}", cloudId, deviceId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, operUserId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return baremetalComputeService.removeBaremetalDevice(cloudId, deviceId, operUserId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove baremetal device failed, message:{}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }
}
