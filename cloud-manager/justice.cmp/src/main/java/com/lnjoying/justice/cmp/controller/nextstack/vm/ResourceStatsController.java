package com.lnjoying.justice.cmp.controller.nextstack.vm;

import com.lnjoying.justice.cmp.domain.dto.response.nextstack.baremetal.InstanceStatsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm.ResourceStats;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm.StorageInfoRsp;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.nextstack.vm.StatisticsServiceBiz;
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

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author George
 * @since 2023-02-07
 */
@RestSchema(schemaId = "resourceStats")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/api/vm/v1")
@Api(value = "Resource Stats Controller",tags = {"Virtual Machine Controller"})
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "ns_resource_stat"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "ns_resource_stats"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "资源统计"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpResourceStats")})})
public class ResourceStatsController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private StatisticsServiceBiz vmComputeService;

    @Autowired
    private CloudService cloudService;

    @GetMapping(value = "/all_storage_stats", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get storage stats", response = Object.class)
    public ResponseEntity<StorageInfoRsp> getStorageStats(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId)
    {
        try
        {
            return cloudService.sendHttpRequestToCloud(cloudId);
        }
        catch (Exception e)
        {
            LOGGER.error("get all storage size failed: {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/user_storage_stats", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get user storage stats", response = Object.class)
    public ResponseEntity<StorageInfoRsp> getUserStorageStats(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                              @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.debug("get user storage stats, cloud:{}, userId:{}", cloudId, userId);

            StorageInfoRsp getStorageInfoRsp = new StorageInfoRsp();
            getStorageInfoRsp.setUnit("GB");
            getStorageInfoRsp.setUsed((float)vmComputeService.getUserStorageSize(cloudId, userId));
            return ResponseEntity.ok(getStorageInfoRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get user storage size failed: {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/resource_stats", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get user storage stats", response = Object.class)
    public ResponseEntity<List<ResourceStats>> getResourceStats(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                      @ApiParam(name = "name") @RequestParam(required = true) String name,
                                                                      @ApiParam(name = "days") @RequestParam(required = true) Integer days,
                                                                      @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.debug("get user storage stats, cloud:{}, userId:{}", cloudId, userId);

            return ResponseEntity.ok(vmComputeService.getResourceStats(cloudId, userId, name, days));
        }
        catch (Exception e)
        {
            LOGGER.error("get resource stats failed: {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/vm_stats", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get user vm stats", response = Object.class)
    public ResponseEntity<InstanceStatsRsp> getVmStats(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                       @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.debug("get user vm stats, cloud:{}, userId:{}", cloudId, userId);

            return ResponseEntity.ok(vmComputeService.getInstanceStats(cloudId, userId));
        }
        catch (Exception e)
        {
            LOGGER.error("get vm stats failed: {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/vm_count", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get user vm count", response = Object.class)
    public ResponseEntity<Long> getVmCount(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                           @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.debug("get user vm count, cloud:{}, userId:{}", cloudId, userId);

            return ResponseEntity.ok(vmComputeService.getVmCount(cloudId, userId));
        }
        catch (Exception e)
        {
            LOGGER.error("get vm count failed: {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/cpu_stats", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get user vm cpu stats", response = Object.class)
    public ResponseEntity<ResourceStats> getCpuStats(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                     @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            return cloudService.sendHttpRequestToCloud(cloudId);
        }
        catch (Exception e)
        {
            LOGGER.error("get vm cpu stats failed: {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/mem_stats", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get user vm memory stats", response = Object.class)
    public ResponseEntity<ResourceStats> getMemStats(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                     @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            return cloudService.sendHttpRequestToCloud(cloudId);
        }
        catch (Exception e)
        {
            LOGGER.error("get vm memory stats failed: {}", e.getMessage());
            throw throwWebException(e);
        }
    }
}
