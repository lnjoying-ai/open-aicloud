package com.lnjoying.justice.cmp.controller.nextstack.vm;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpVmInstanceKey;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.baremetal.CommonReq;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.vm.NfsCreateReq;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm.NfsInfoRsp;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm.NfsInfosRsp;
import com.lnjoying.justice.cmp.entity.search.nextstack.vm.NfsSearchCritical;
import com.lnjoying.justice.cmp.handler.actiondescription.i18n.zh_cn.CmpNextStackActionDescriptionTemplate;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.nextstack.vm.NfsServiceBiz;
import com.lnjoying.justice.commonweb.controller.RestWebController;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.schema.constant.ResourceOperationTypeConstants;
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

import javax.validation.Valid;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isAdmin;

@RestSchema(schemaId = "nfs")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/api/vm/v1/")
@Api(value = "NFS Controller",tags = {"NFS Controller"})
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "ns_nfs_server"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "ns_nfs_servers"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "nfs server"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpNfs")})})
public class NfsController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private NfsServiceBiz nfsService;

    @Autowired
    private CloudService cloudService;

    @PostMapping(value = "/nfs", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "create nfs server", response = Object.class)
    @LNJoyAuditLog(value = "create nfs server",
            tags = ResourceOperationTypeConstants.RESOURCE_CREATE,
            resourceMapperId = CmpResources.NS_NFS_SERVER,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.ADD_NFS,
            associatedResourceCnName = "NFS",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                    resourceIdParseSPEL = "#obj?.body.get('nfsId')?.toString()", resourcePrimaryKeyClass = TblCmpVmInstanceKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpNfsKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> createNfs(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                @ApiParam(value = "NfsCreateReq", required = true, name = "NfsCreateReq") @RequestBody @Valid NfsCreateReq request,
                                                @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)throws WebSystemException
    {
        try
        {
            LOGGER.info("create nfs server, cloud:{} request:{}, userId:{}", cloudId, request, userId);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return nfsService.createNfs(cloudId, request, bpId, userId);
        }
        catch (Exception e)
        {
            LOGGER.error("create nfs server error: {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/nfs/{nfsId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get nfs detail info", response = Object.class)
    public NfsInfoRsp getNfs(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                             @ApiParam(value = "nfsId", required = true, name = "nfsId") @PathVariable("nfsId") String nfsId,
                             @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.info("get nfs, request:{}, userId:{}", nfsId, userId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            return nfsService.getNfs(cloudId, nfsId, operUserId);
        }
        catch (Exception e)
        {
            LOGGER.error("get nfs error: {}, nfsId: {}", e.getMessage(), nfsId);
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/nfs", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get nfs servers", response = Object.class)
    public NfsInfosRsp getNfsServers(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                     @ApiParam(name = "page_size") @RequestParam(required = false, value = "page_size") Integer pageSize,
                                     @ApiParam(name = "page_num") @RequestParam(required = false, value = "page_num") Integer pageNum,
                                     @ApiParam(name = "name") @RequestParam(required = false) String name,
                                     @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get nfs servers");

            String filterUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                filterUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            NfsSearchCritical nfsSearchCritical = new NfsSearchCritical();
            if (StringUtils.isNotBlank(name))
            {
                LOGGER.info("get nfs servers by name:{}", name);
                nfsSearchCritical.setName(name);
            }
            if (pageSize != null && pageNum != null)
            {
                nfsSearchCritical.setPageSize(pageSize);
                nfsSearchCritical.setPageNum(pageNum);
            }

            return nfsService.listNfs(cloudId, nfsSearchCritical, filterUserId);
        }
        catch (Exception e)
        {
            LOGGER.error("get nfs servers error: {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/nfs/{nfsId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update nfs", response = Object.class)
    @LNJoyAuditLog(value = "update nfs",
            resourceMapperId = CmpResources.NS_NFS_SERVER,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.UPDATE_NFS,
            associatedResourceCnName = "NFS",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "instanceId", resourcePrimaryKeyClass = TblCmpVmInstanceKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpNfsKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> updateInstance(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                     @ApiParam(value = "nfsId", required = true, name = "nfsId") @PathVariable("nfsId") String nfsId,
                                                     @ApiParam(value = "CommonReq", required = true, name = "CommonReq") @RequestBody @Valid CommonReq request,
                                                     @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.info("update nfs, request:{}, userId:{}", request, userId);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
                nfsService.checkNfsUser(cloudId, nfsId, userId);
            }

            return cloudService.sendHttpRequestToCloud(cloudId);
        }
        catch (Exception e)
        {
            LOGGER.error("update nfs error: {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @DeleteMapping(value = "/nfs/{nfsId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "remove nfs server", response = Object.class)
    @LNJoyAuditLog(value = "remove nfs server",
            resourceMapperId = CmpResources.NS_NFS_SERVER,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.DELETE_NFS,
            associatedResourceCnName = "NFS",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "instanceId", resourcePrimaryKeyClass = TblCmpVmInstanceKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpNfsKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> removeNfs(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                @ApiParam(value = "nfsId", required = true, name = "nfsId") @PathVariable("nfsId") String nfsId,
                                                @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("remove nfs server, cloud:{}, nfsId:{}, userId:{}", cloudId, nfsId, userId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return nfsService.removeNfs(cloudId, nfsId, operUserId);
        }
        catch (Exception e)
        {
            LOGGER.error("delete nfs server error: {}", e.getMessage());
            throw throwWebException(e);
        }
    }
}
