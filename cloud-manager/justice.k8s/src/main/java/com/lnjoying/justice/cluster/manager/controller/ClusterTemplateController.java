package com.lnjoying.justice.cluster.manager.controller;


import com.lnjoying.justice.cluster.manager.common.ClsResources;
import com.lnjoying.justice.cluster.manager.domain.dto.model.template.*;
import com.lnjoying.justice.cluster.manager.domain.dto.rsp.cluster.ClusterTemplatesRspDto;
import com.lnjoying.justice.cluster.manager.handler.actiondescription.i18n.zh_cn.ClusterTemplateActionDescriptionTemplate;
import com.lnjoying.justice.cluster.manager.service.rpc.CombRpcService;
import com.lnjoying.justice.cluster.manager.service.template.ClusterTemplateService;
import com.lnjoying.justice.cluster.manager.util.ClsUtils;
import com.lnjoying.justice.commonweb.controller.RestWebController;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.schema.constant.ResourceOperationTypeConstants;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import com.lnjoying.justice.schema.service.ums.UmsService;
import com.micro.core.common.Pair;
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

import java.util.Objects;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

@RestSchema(schemaId = "templates")
@RequestMapping("/cls/v1/templates")
@Api(value = "cluster template",tags = "cluster template")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "template"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "templates"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "集群模板"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblClusterTemplateInfo")})})
public class ClusterTemplateController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();
    
    @Autowired
    private ClusterTemplateService clusterTemplateService;

    @Autowired
    private CombRpcService combRpcService;
    
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "post templates")
    @LNJoyAuditLog(value = "post templates",
            tags = ResourceOperationTypeConstants.RESOURCE_CREATE,
            actionDescriptionTemplate = ClusterTemplateActionDescriptionTemplate.Descriptions.ADD_TEMPLATE,
            isResourceInstanceNameFromArgs = true,
            resourceInstanceNameFromArgsParseSpEl = "#obj?.get('name')?.toString()",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                            resourceIdParseSPEL = "#obj?.body?.toString()"))
    public ResponseEntity<String> addClusterTemplate(@ApiParam(value = "", required = true, name = "req") @RequestBody AddTemplateInfoReq templateInfoReq,
                                                         @RequestHeader(name = UserHeadInfo.USERID,      required = false) String userId,
                                                         @RequestHeader(name = UserHeadInfo.USERNAME, required = false) String userName,
                                                         @RequestHeader(name = UserHeadInfo.BPID,        required = false) String bpId,
                                                         @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities,
                                                         @RequestHeader(name = UserHeadInfo.USERKIND,    required = false) String userKind)
    {
        
        LOGGER.info("add cluster templates. {}", templateInfoReq);

        String owner = templateInfoReq.getOwner();
        if (StringUtils.isBlank(owner))
        {
            templateInfoReq.setOwner(userId);
            templateInfoReq.setBp(bpId);
        }
        else
        {
            setBpId(templateInfoReq, owner);
        }

        templateInfoReq.setCreator(userId);

        return status(CREATED).body(clusterTemplateService.createTemplate(templateInfoReq));
    }


    @RequestMapping(value = "/{template_id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update templates")
    @LNJoyAuditLog(value = "update templates",
            resourceMapperId = ClsResources.CLUSTER_TEMPLATE,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "templateId"))
    public ResponseEntity<String> updateClusterTemplate(@ApiParam(value = "template_id") @PathVariable("template_id")       String templateId,
                                                        @ApiParam(value = "", required = true, name = "req") @RequestBody UpdateTemplateInfoReq templateInfoReq,
                                                        @RequestHeader(name = UserHeadInfo.USERID,      required = false) String userId,
                                                        @RequestHeader(name = UserHeadInfo.BPID,        required = false) String bpId,
                                                        @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities,
                                                        @RequestHeader(name = UserHeadInfo.USERKIND,    required = false) String userKind)
    {
        
        LOGGER.info("update cluster templates. {}", templateInfoReq);

        Pair<String,String> operUserInfo = ClsUtils.getOperUserInfo(userId, bpId, userKind);
        templateInfoReq.setId(templateId);
        templateInfoReq.setCreator(operUserInfo.getLeft());
        templateInfoReq.setBp(operUserInfo.getRight());
        clusterTemplateService.updateTemplate(templateInfoReq);
        return status(OK).body(null);
    }

    @RequestMapping(value = "/{template_id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete templates")
    @LNJoyAuditLog(value = "delete templates",
            resourceMapperId = ClsResources.CLUSTER_TEMPLATE,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "templateId"))
    public ResponseEntity<String> deleteClusterTemplate(@ApiParam(value = "template_id") @PathVariable("template_id")       String templateId,
                                                        @RequestHeader(name = UserHeadInfo.USERID,      required = false) String userId,
                                                        @RequestHeader(name = UserHeadInfo.BPID,        required = false) String bpId,
                                                        @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities,
                                                        @RequestHeader(name = UserHeadInfo.USERKIND,    required = false) String userKind)
    {
        
        LOGGER.info("delete cluster templates, template id. {}", templateId);

        Pair<String,String> operUserInfo = ClsUtils.getOperUserInfo(userId, bpId, userKind);
        clusterTemplateService.deleteTemplate(templateId, operUserInfo.getRight(), operUserInfo.getLeft());
        return status(OK).body(null);
    }

    @RequestMapping(value = "/versions", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "post template version")
    @LNJoyAuditLog(value = "post template version",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                            resourceIdParseSPEL = "#obj?.body?.toString()"))
    public ResponseEntity<String> addClusterTemplateVersions(@ApiParam(value = "", required = true, name = "versionReq") @RequestBody @Valid AddTemplateVersionReq versionReq,
                                                             @RequestHeader(name = UserHeadInfo.USERID,      required = false) String userId,
                                                             @RequestHeader(name = UserHeadInfo.USERNAME, required = false) String userName,
                                                             @RequestHeader(name = UserHeadInfo.BPID,        required = false) String bpId,
                                                             @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities,
                                                             @RequestHeader(name = UserHeadInfo.USERKIND,    required = false) String userKind)
    {
        
        LOGGER.info("add cluster template versions: {}", versionReq);

        String owner = versionReq.getOwner();
        if (StringUtils.isBlank(owner))
        {
            versionReq.setOwner(userId);
            versionReq.setBp(bpId);
        }
        else
        {
            setBpId(versionReq, owner);
        }
        versionReq.setCreator(userId);

        return status(CREATED).body(clusterTemplateService.createTemplateVersions(versionReq));
    }

    @RequestMapping(value = "/versions/{version_id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update cluster template version")
    @LNJoyAuditLog(value = "update cluster template version",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "versionId"))
    public ResponseEntity<String> updateClusterTemplateVersions(@ApiParam(value = "version_id") @PathVariable("version_id")       String versionId,
                                                                @ApiParam(value = "", required = true, name = "req") @RequestBody UpdateTemplateVersionReq versionReq,
                                                                @RequestHeader(name = UserHeadInfo.USERID,      required = false) String userId,
                                                                @RequestHeader(name = UserHeadInfo.BPID,        required = false) String bpId,
                                                                @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities,
                                                                @RequestHeader(name = UserHeadInfo.USERKIND,    required = false) String userKind)
    {
        
        LOGGER.info("update cluster template versions: {}", versionReq);

        Pair<String,String> operUserInfo = ClsUtils.getOperUserInfo(userId, bpId, userKind);
        versionReq.setCreator(operUserInfo.getLeft());
        versionReq.setBp(operUserInfo.getRight());
        versionReq.setVersionId(versionId);
        clusterTemplateService.updateTemplateVersions(versionReq);
        return status(OK).body(null);
    }

    @RequestMapping(value = "/versions/{version_id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete template version")
    @LNJoyAuditLog(value = "delete template version",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "versionId"))
    public ResponseEntity<String> deleteClusterTemplateVersions(@ApiParam(value = "version_id") @PathVariable("version_id")       String versionId,
                                                                @RequestHeader(name = UserHeadInfo.USERID,      required = false) String userId,
                                                                @RequestHeader(name = UserHeadInfo.BPID,        required = false) String bpId,
                                                                @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities,
                                                                @RequestHeader(name = UserHeadInfo.USERKIND,    required = false) String userKind)
    {
        
        LOGGER.info("delete cluster template version, template id. {}", versionId);

        clusterTemplateService.deleteTemplateVersions(versionId);
        return status(OK).body(null);
    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get templates")
    public ResponseEntity<ClusterTemplatesRspDto> getClusterTemplates(@ApiParam(name = "name") @RequestParam(value = "name", required = false) String name,
                                                                      @ApiParam(name = "cluster_id") @RequestParam(value = "cluster_id", required = false) String clusterId,
                                                                      @ApiParam(name = "cluster_type") @RequestParam(value = "cluster_type", required = false) String clusterType,
                                                                      @ApiParam(name = "owner") @RequestParam(value = "owner", required = false) String owner,
                                                                      @ApiParam(name = "page_size") @RequestParam(value = "page_size", required = false, defaultValue = Integer.MAX_VALUE + "") Integer pageSize,
                                                                      @ApiParam(name = "page_num") @RequestParam(value = "page_num", required = false, defaultValue = "1") Integer pageNum,
                                                                      @RequestHeader(name = UserHeadInfo.USERID,      required = false) String userId,
                                                                      @RequestHeader(name = UserHeadInfo.BPID,        required = false) String bpId,
                                                                      @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities,
                                                                      @RequestHeader(name = UserHeadInfo.USERKIND,    required = false) String userKind)
    {
        
        LOGGER.info("get cluster templates.");

        Pair<String,String> operUserInfo = ClsUtils.getOperUserInfo(userId, bpId, userKind);
        if (StringUtils.isBlank(operUserInfo.getLeft()) && StringUtils.isNotBlank(owner))
        {
            operUserInfo.setKey(owner);
        }
        return status(OK).body(clusterTemplateService.getClusterTemplates(name, clusterId, clusterType, operUserInfo.getRight(), operUserInfo.getLeft(), pageNum, pageSize));
    }

    @RequestMapping(value = "{template_id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get template")
    @LNJoyAuditLog(value = "get template",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "templateId"))
    public ResponseEntity<ClusterTemplateInfo> getClusterTemplate(@ApiParam(value = "template_id") @PathVariable("template_id")       String templateId,
                                                                  @RequestHeader(name = UserHeadInfo.USERID,      required = false) String userId,
                                                                  @RequestHeader(name = UserHeadInfo.BPID,        required = false) String bpId,
                                                                  @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities,
                                                                  @RequestHeader(name = UserHeadInfo.USERKIND,    required = false) String userKind)
    {
        
        LOGGER.info("get cluster template, templateId:{}.", templateId);

        Pair<String,String> operUserInfo = ClsUtils.getOperUserInfo(userId, bpId, userKind);
        return status(OK).body(clusterTemplateService.getClusterTemplate(templateId, operUserInfo.getRight(), operUserInfo.getLeft()));
    }

    @RequestMapping(value = "/versions/{version_id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get template version")
    @LNJoyAuditLog(value = "get template version",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "versionId"))
    public ResponseEntity<ClusterTemplateVersionInfo> getClusterTemplateVersion(@ApiParam(value = "version_id") @PathVariable("version_id")       String versionId,
                                                                                @RequestHeader(name = UserHeadInfo.USERID,      required = false) String userId,
                                                                                @RequestHeader(name = UserHeadInfo.BPID,        required = false) String bpId,
                                                                                @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities,
                                                                                @RequestHeader(name = UserHeadInfo.USERKIND,    required = false) String userKind)
    {
        
        LOGGER.info("get cluster template version, versionId:{}.", versionId);

        ClusterTemplateVersionInfo templateVersions = clusterTemplateService.getTemplateVersion(versionId);
        return status(OK).body(templateVersions);
    }

    private void setBpId(AddTemplateInfoReq templateInfoReq, String owner)
    {
        try
        {
            UmsService.UserInfo userInfo = combRpcService.getUmsService().getUserInfoByUseId(owner);
            if (Objects.nonNull(userInfo))
            {
                templateInfoReq.setBp(userInfo.getBpId());
            }
        }
        catch (Exception e)
        {
            LOGGER.error("get user info error: {}", e);
        }
    }

    private void setBpId(AddTemplateVersionReq versionReq, String owner)
    {
        try
        {
            UmsService.UserInfo userInfo = combRpcService.getUmsService().getUserInfoByUseId(owner);
            if (Objects.nonNull(userInfo))
            {
                versionReq.setBp(userInfo.getBpId());
            }
        }
        catch (Exception e)
        {
            LOGGER.error("get user info error: {}", e);
        }
    }

}
