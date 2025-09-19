package com.lnjoying.justice.cmp.controller.nextstack.baremetal;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.config.DescriptionConfig;
import com.lnjoying.justice.cmp.db.model.TblCmpPubkeyKey;
import com.lnjoying.justice.cmp.entity.search.nextstack.baremetal.PubkeySearchCritical;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.baremetal.CommonReq;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.baremetal.UploadPubkeyReq;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.baremetal.PubkeysRsp;
import com.lnjoying.justice.cmp.domain.info.baremetal.PubkeyDetailInfo;
import com.lnjoying.justice.cmp.handler.actiondescription.i18n.zh_cn.CmpNextStackActionDescriptionTemplate;
import com.lnjoying.justice.cmp.service.nextstack.baremetal.PubkeyServiceBiz;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
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

import javax.validation.Valid;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isAdmin;

@RestSchema(schemaId = "pubkey")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/api/bm/v1")
@Api(value = "Pubkey Controller",tags = "Pubkey Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "ns_pubkey"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "ns_pubkeys"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "公钥"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpPubkey")})})
public class PubkeyController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private PubkeyServiceBiz pubkeyServiceBiz;

    @Autowired
    private CloudService cloudService;

    @PostMapping(value = "/pubkeys", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "create key pair", response = Object.class, notes = DescriptionConfig.ADD_PUBKEY)
    @LNJoyAuditLog(value = "create key pair",
            resourceMapperId = CmpResources.NS_PUBKEY,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.ADD_PUBKEY,
            associatedResourceCnName = "密钥",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                    resourceIdParseSPEL = "#obj?.body?.get('pubkeyId')?.toString()", resourcePrimaryKeyClass = TblCmpPubkeyKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpPubkeyKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> createKeyPair(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                @ApiParam(value = "CommonReq", required = true, name = "CommonReq") @RequestBody CommonReq request,
                                                @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)  throws WebSystemException
    {
        try
        {
            LOGGER.info("create keyPair, cloud:{}, request:{}, userId:{}", cloudId, request, userId);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return pubkeyServiceBiz.createKeyPair(cloudId, request, bpId, userId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("create keyPair error: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @PostMapping(value = "/pubkeys/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "upload pubkey", response = Object.class, notes = DescriptionConfig.UPLOAD_PUBKEY)
    @LNJoyAuditLog(value = "upload pubkey",
            resourceMapperId = CmpResources.NS_PUBKEY,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.UPLOAD_PUBKEY,
            associatedResourceCnName = "密钥",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                    resourceIdParseSPEL = "#obj?.body?.get('pubkeyId')?.toString()", resourcePrimaryKeyClass = TblCmpPubkeyKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpPubkeyKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> uploadPubkey(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                               @ApiParam(value = "UploadPubkeyReq", required = true, name = "UploadPubkeyReq") @RequestBody @Valid UploadPubkeyReq request,
                                               @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.info("upload pubkey, cloud:{}, request:{}, userId:{}", cloudId, request, userId);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return pubkeyServiceBiz.uploadPubkey(cloudId, request, bpId, userId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("upload pubkey error: {}",e.getMessage());
            e.printStackTrace();
            throw  throwWebException(e);
        }
    }

    @GetMapping(value = "/pubkeys/{pubkeyId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get pubkey detail info", response = Object.class, notes = DescriptionConfig.GET_PUBKEY)
    public ResponseEntity<PubkeyDetailInfo> getPubkey(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                      @ApiParam(value = "pubkeyId", required = true, name = "pubkeyId") @PathVariable("pubkeyId") String pubkeyId,
                                                      @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.info("get pubkey, cloud:{}, request:{}, userId:{}", cloudId, pubkeyId, userId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            PubkeyDetailInfo pubkeyDetailInfo = pubkeyServiceBiz.getPubkey(cloudId, pubkeyId, operUserId);

            return ResponseEntity.ok(pubkeyDetailInfo);
        }
        catch (Exception e)
        {
            LOGGER.error("get pubkey error: {}, pubkeyId: {}",e.getMessage(), pubkeyId);
            e.printStackTrace();
            throw  throwWebException(e);
        }
    }

    @GetMapping(value = "/pubkeys", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get pubkeys", response = Object.class, notes = DescriptionConfig.GET_PUBKEYS)
    public ResponseEntity<PubkeysRsp> getPubkeys(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                 @ApiParam(name = "name") @RequestParam(required = false) String name,
                                                 @ApiParam(name = "page_size") @RequestParam(required = false, value = "page_size") Integer pageSize,
                                                 @ApiParam(name = "page_num") @RequestParam(required = false, value = "page_num") Integer pageNum,
                                                 @ApiParam(name = "ee_bp") @RequestParam(required = false, value = "ee_bp") String eeBp,
                                                 @ApiParam(name = "ee_user") @RequestParam(required = false, value = "ee_user") String eeUser,
                                                 @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.info("get pubkey list, cloud:{}", cloudId);

            String filterUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                filterUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            PubkeySearchCritical pageSearchCritical = new PubkeySearchCritical();
            pageSearchCritical.setName(name);
            if (pageNum != null) pageSearchCritical.setPageNum(pageNum);
            if (pageSize != null) pageSearchCritical.setPageSize(pageSize);

            PubkeysRsp getPubkeysRsp = pubkeyServiceBiz.getPubkeys(cloudId, pageSearchCritical, filterUserId);

            return ResponseEntity.ok(getPubkeysRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get pubkeys error: {}",e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/pubkeys/{pubkeyId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update pubkey", response = Object.class, notes = DescriptionConfig.UPDATE_PUBKEY)
    @LNJoyAuditLog(value = "update pubkey",
            resourceMapperId = CmpResources.NS_PUBKEY,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.UPDATE_PUBKEY,
            associatedResourceCnName = "密钥",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "pubkeyId", resourcePrimaryKeyClass = TblCmpPubkeyKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpPubkeyKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> updatePubkey(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                               @ApiParam(value = "pubkeyId", required = true, name = "pubkeyId") @PathVariable("pubkeyId") String pubkeyId,
                                               @ApiParam(value = "CommonReq", required = true, name = "CommonReq") @RequestBody CommonReq request,
                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.debug("put pubkey: {}, userId:{}", request, userId);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
                pubkeyServiceBiz.checkPubkeyUser(cloudId, pubkeyId, userId);
            }

            return cloudService.sendHttpRequestToCloud(cloudId);
        }
        catch (Exception e)
        {
            LOGGER.error("update pubkey error: {}, pubkeyId: {}",e.getMessage(), pubkeyId);
            e.printStackTrace();
            throw  throwWebException(e);
        }
    }

    @DeleteMapping(value = "/pubkeys/{pubkeyId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete pubkey", response = Object.class, notes = DescriptionConfig.DEL_PUBKEY)
    @LNJoyAuditLog(value = "delete pubkey",
            resourceMapperId = CmpResources.NS_PUBKEY,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.DELETE_PUBKEY,
            associatedResourceCnName = "密钥",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "pubkeyId", resourcePrimaryKeyClass = TblCmpPubkeyKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpPubkeyKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> removePubkey(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                @ApiParam(value = "pubkeyId", required = true, name = "pubkeyId") @PathVariable("pubkeyId") String pubkeyId,
                                                @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)throws WebSystemException
    {
        try
        {
            LOGGER.info("delete pubkey, cloud:{}, pubkey:{}, userId:{}", cloudId, pubkeyId, userId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return pubkeyServiceBiz.removePubkey(cloudId, pubkeyId, operUserId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("delete pubkey error: {}, pubkeyId: {}",e.getMessage(), pubkeyId);
            e.printStackTrace();
            throw  throwWebException(e);
        }
    }
}
