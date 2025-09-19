package com.lnjoying.justice.cmp.controller.nextstack.repo;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpStoragePoolKey;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.baremetal.CommonReq;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo.StoragePoolDetailInfoRsp;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo.StoragePoolsRsp;
import com.lnjoying.justice.cmp.entity.search.nextstack.repo.StoragePoolSearchCritical;
import com.lnjoying.justice.cmp.handler.actiondescription.i18n.zh_cn.CmpNextStackActionDescriptionTemplate;
import com.lnjoying.justice.cmp.service.nextstack.repo.StoragePoolServiceBiz;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.commonweb.controller.RestWebController;
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

import javax.validation.Valid;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isAdmin;

@RestSchema(schemaId = "storagePool")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/api/repo/v1")
@Api(value = "StoragePool Controller",tags = {"StoragePool Controller"})
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "ns_storage_pool"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "ns_storage_pools"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "存储池"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpStoragePool")})})
public class StoragePoolController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private StoragePoolServiceBiz storagePoolServiceBiz;

    @Autowired
    private CloudService cloudService;

    @GetMapping(value = "/storage_pools", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get storagePools", response = StoragePoolsRsp.class)
    public ResponseEntity<StoragePoolsRsp> getStoragePools(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                           @ApiParam(name = "name") @RequestParam(required = false) String name,
                                                           @ApiParam(value = "type", name = "type") @RequestParam(required=false,value = "type") Integer poolType,
                                                           @ApiParam(name = "page_size") @RequestParam(required = false,value = "page_size") Integer pageSize,
                                                           @ApiParam(name = "page_num") @RequestParam(required = false,value = "page_num") Integer pageNum,
                                                           @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
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

            StoragePoolSearchCritical storagePoolCritical = new StoragePoolSearchCritical();
            storagePoolCritical.setPoolName(name);
            storagePoolCritical.setPoolType(poolType);
            if (pageNum != null) storagePoolCritical.setPageNum(pageNum);
            if (pageNum != null) storagePoolCritical.setPageSize(pageSize);
            StoragePoolsRsp storagePoolsRsp = storagePoolServiceBiz.getStoragePools(cloudId, storagePoolCritical, filterUserId);

            return ResponseEntity.ok(storagePoolsRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get storagePool failed: {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/storage_pools/{storage_pool_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get storagePool info", response = StoragePoolDetailInfoRsp.class)
    public ResponseEntity<StoragePoolDetailInfoRsp> getStoragePool(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                   @ApiParam(value = "storage_pool_id", required = true, name = "storage_pool_id") @PathVariable("storage_pool_id")String storagePoolId)
    {
        try
        {
            StoragePoolDetailInfoRsp storagePoolDetailInfoRsp = storagePoolServiceBiz.getStoragePool(cloudId, storagePoolId);
            return ResponseEntity.ok(storagePoolDetailInfoRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get storagePool failed: {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/storage_pools/{storage_pool_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update storagePool", response = Object.class)
    @LNJoyAuditLog(value = "update storagePool",
            resourceMapperId = CmpResources.NS_STORAGE_POOL,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.UPDATE_STORAGE_POOL,
            associatedResourceCnName = "存储池",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "storagePoolId", resourcePrimaryKeyClass = TblCmpStoragePoolKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpStoragePoolKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> updateStoragePool(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                    @ApiParam(value = "storage_pool_id", required = true, name = "storage_pool_id") @PathVariable("storage_pool_id")String storagePoolId,
                                                    @ApiParam(value = "commonReq", required = true, name = "commonReq") @RequestBody @Valid CommonReq request)
    {
        try
        {
            LOGGER.info("update storagePool: storagePoolId:{} name:{}",storagePoolId, request.getName());

            return cloudService.sendHttpRequestToCloud(cloudId);
        }
        catch (Exception e)
        {
            LOGGER.error("update  storagePool failed: {}", e.getMessage());
            throw throwWebException(e);
        }
    }
}
