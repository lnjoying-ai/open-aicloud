package com.lnjoying.justice.ecrm.controller;

import com.lnjoying.justice.commonweb.controller.RestWebController;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.ecrm.common.constant.EcrmResources;
import com.lnjoying.justice.ecrm.config.DescriptionConfig;
import com.lnjoying.justice.ecrm.domain.dto.model.ImageInfo;
import com.lnjoying.justice.ecrm.domain.dto.model.VersionInfo;
import com.lnjoying.justice.ecrm.domain.dto.request.*;
import com.lnjoying.justice.ecrm.domain.dto.response.*;
import com.lnjoying.justice.ecrm.domain.dto.model.EdgeNodeInfo;
import com.lnjoying.justice.ecrm.domain.model.search.EdgeSearchCritical;
import com.lnjoying.justice.ecrm.facade.EdgeServiceFacade;
import com.lnjoying.justice.ecrm.handler.actiondescription.i18n.zh_cn.EdgeActionDescriptionTemplate;
import com.lnjoying.justice.schema.constant.ResourceOperationTypeConstants;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;

@RestSchema(schemaId = "edge")
@RequestMapping("/ecrm/v1/edges")
@Api(value = "Edge Controller",tags = "Edge Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "edge"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "edges"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "节点"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblEdgeComputeInfo")})})
public class EdgeController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    EdgeServiceFacade edgeNodeServiceFacade;

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "add node", notes = DescriptionConfig.ADD_EDGE_NODE_MSG)
    @LNJoyAuditLog(value = "add node", notes = DescriptionConfig.ADD_EDGE_NODE_MSG,
            tags = ResourceOperationTypeConstants.RESOURCE_CREATE,
            actionDescriptionTemplate = EdgeActionDescriptionTemplate.Descriptions.ADD_NODE)
    public ResponseEntity<Map<String,String>> genAddCmd(@ApiParam(value = "", required = true, name = "edgeInput")@RequestBody @Valid EdgeInputReq edgeInputReq,
                                                        @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                        @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        try
        {

            return ResponseEntity.status(HttpStatus.CREATED).body(edgeNodeServiceFacade.genAddCmd(edgeInputReq));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("gen cmd error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @PatchMapping(value = "/{node_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "patch node",notes = DescriptionConfig.UPDATE_EDGE_NODE_MSG)
    @LNJoyAuditLog(value = "patch node", notes = DescriptionConfig.UPDATE_EDGE_NODE_MSG,
            resourceMapperId = EcrmResources.EDGE_COMPUTE,
            associatedResourceCnName = "节点",
            actionDescriptionTemplate = EdgeActionDescriptionTemplate.Descriptions.UPDATE_NODE_LABEL,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH, bindParameterName = "node_id"))
    public ResponseEntity<String>  patchNode(@ApiParam(value = "", required = true, name = "node_id") @PathVariable(value = "node_id") String node_id,
                                              @ApiParam(value = "", required = true, name = "configEdgeReq") @RequestBody PatchEdgeReq patchEdgeReq,
                                              @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                              @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        try
        {

            edgeNodeServiceFacade.patchNode(node_id, patchEdgeReq);
            return ResponseEntity.ok(null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("patch node error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/{node_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update node",notes = DescriptionConfig.UPDATE_EDGE_NODE_MSG)
    @LNJoyAuditLog(value = "update node", notes = DescriptionConfig.UPDATE_EDGE_NODE_MSG,
            resourceMapperId = EcrmResources.EDGE_COMPUTE,
            associatedResourceCnName = "节点",
            actionDescriptionTemplate = EdgeActionDescriptionTemplate.Descriptions.UPDATE_NODE,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH, bindParameterName = "node_id"))
    public ResponseEntity<String>  updateNode(@ApiParam(value = "", required = true, name = "node_id") @PathVariable(value = "node_id") String node_id,
                                              @ApiParam(value = "", required = true, name = "configEdgeReq") @RequestBody ConfigEdgeReq configEdgeReq,
                                              @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                              @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        try
        {

            edgeNodeServiceFacade.updateNode(node_id, configEdgeReq);
            return ResponseEntity.ok(null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("update node error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @DeleteMapping(value = "/{node_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete node",notes = DescriptionConfig.DELETE_EDGE_NODE_MSG)
    @LNJoyAuditLog(value = "delete node", notes = DescriptionConfig.DELETE_EDGE_NODE_MSG,
            resourceMapperId = EcrmResources.EDGE_COMPUTE,
            actionDescriptionTemplate = EdgeActionDescriptionTemplate.Descriptions.DELETE_NODE,
            associatedResourceCnName = "节点",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH, bindParameterName = "node_id"))
    public ResponseEntity<String> deleteNode(@ApiParam(value = "", required = true, name = "node_id")@PathVariable(value = "node_id") String node_id,
                                             @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                             @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        try
        {

            edgeNodeServiceFacade.deleteNode(node_id);
            return ResponseEntity.ok(null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("delete node error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @PostMapping(value = "/{node_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "action node",notes = DescriptionConfig.ACTION_EDGE_NODE_MSG)
    @LNJoyAuditLog(value = "action node", notes = DescriptionConfig.ACTION_EDGE_NODE_MSG,
            resourceMapperId = EcrmResources.EDGE_COMPUTE,
            associatedResourceCnName = "节点",
            actionDescriptionTemplateClass = EdgeActionDescriptionTemplate.class,
            actionDescriptionField = "action", actionDescriptionFormatString = "control node: %s",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH, bindParameterName = "node_id"))
    public ResponseEntity<String>  ctrlNode(@ApiParam(value = "", required = true, name = "node_id")@PathVariable(value = "node_id") String node_id,
                                            @ApiParam(value = "", required = true, name = "action")@RequestParam(value = "action") String action,
                                            @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                            @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        try
        {

            edgeNodeServiceFacade.ctrlNode(node_id, action);
            return ResponseEntity.ok(null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("{} node error. {}", action, e.getMessage());
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/{node_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get node by id", notes = DescriptionConfig.EDGE_NODE_INFO_MSG)
    @LNJoyAuditLog(value = "get node by id",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH, bindParameterName = "node_id"))
    public ResponseEntity<EdgeNodeInfo> getNodeByNodeId(@ApiParam(value = "", required = true, name = "node_id")@PathVariable String node_id,
                                                        @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                        @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        try
        {
            
            return ResponseEntity.ok().body(edgeNodeServiceFacade.getNodeById(node_id));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("get node error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get node list",notes = DescriptionConfig.EDGE_NODE_LIST_MSG)
    public ResponseEntity<QueryEdgeRsp> getNodes(@ApiParam(name = "nation") @RequestParam(value = "nation", required = false) String nation,
                                                 @ApiParam(name = "adcode") @RequestParam(value = "adcode", required = false) String adcode,
                                                 @ApiParam(name = "site") @RequestParam(value = "site", required = false) String site,
                                                 @ApiParam(name = "region") @RequestParam(value = "region", required = false) String region,
                                                 @ApiParam(name = "active_status") @RequestParam(value = "activeStatus", required = false) Integer activeStatus,
                                                 @ApiParam(name = "online_status") @RequestParam(value = "onlineStatus", required = false) Integer onlineStatus,
                                                 @ApiParam(name = "orchestration") @RequestParam(value = "orchestration", required = false) String orchestration,
                                                 @ApiParam(name = "gpu") @RequestParam(value = "gpu", required = false) String gpu,
                                                 @ApiParam(name = "name") @RequestParam(value = "name", required = false) String name,
                                                 @ApiParam(name = "page_size") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                 @ApiParam(name = "page_num") @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                 @ApiParam(name = "vendor") @RequestParam(value = "vendor", required = false) String vendor,
                                                 @ApiParam(name = "uuid") @RequestParam(value = "uuid", required = false) String uuid,
                                                 @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                 @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        try
        {
            EdgeSearchCritical edgeSearchCritical = new EdgeSearchCritical();
            edgeSearchCritical.setNation(nation);
            edgeSearchCritical.setAdcode(adcode);
            edgeSearchCritical.setRegion(region);
            edgeSearchCritical.setSite(site);
            edgeSearchCritical.setActiveStatus(activeStatus);
            edgeSearchCritical.setOnlineStatus(onlineStatus);
            edgeSearchCritical.setName(name);
            edgeSearchCritical.setVendor(vendor);
            edgeSearchCritical.setUuid(uuid);
            if (pageNum != null) edgeSearchCritical.setPageNum(pageNum);
            if (pageSize != null) edgeSearchCritical.setPageSize(pageSize);
            if (orchestration != null) edgeSearchCritical.setOrchestration(orchestration);
            if (gpu != null) edgeSearchCritical.setGpu(gpu);
            return ResponseEntity.ok().body(edgeNodeServiceFacade.getNodes(edgeSearchCritical));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("get nodes error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/{node_id}/images", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get node images",notes = DescriptionConfig.EDGE_NODE_IMAGES_MSG)
    @LNJoyAuditLog(value = "get node images",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH, bindParameterName = "node_id"))
    public ResponseEntity<List<ImageInfo>> getNodeImage(@ApiParam(value = "", required = true, name = "node_id")@PathVariable String node_id,
                                                        @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                        @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        
        LOGGER.info("get images {}", node_id);

        try
        {
            return ResponseEntity.ok().body(edgeNodeServiceFacade.getNodeImages(node_id));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("get node error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/versions", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get node images",notes = DescriptionConfig.EDGE_NODE_IMAGES_MSG)
    public ResponseEntity<List<VersionInfo>> getVersions(@RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                          @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        try
        {
            return ResponseEntity.ok().body(edgeNodeServiceFacade.getVersions());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("get node error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @PostMapping(value = "/{node_id}/upgrade", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "upgrade node",notes = DescriptionConfig.EDGE_NODE_IMAGES_MSG)
    @LNJoyAuditLog(value = "upgrade node", notes = DescriptionConfig.EDGE_NODE_IMAGES_MSG,
            resourceMapperId = EcrmResources.EDGE_COMPUTE,
            actionDescriptionTemplate = EdgeActionDescriptionTemplate.Descriptions.UPGRADE_NODE,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH, bindParameterName = "node_id"))
    public ResponseEntity<String> upgrade(@ApiParam(value = "", required = true, name = "node_id")@PathVariable String node_id,
                                                     @ApiParam(name = "new_version", required = true) @RequestParam(value = "newVersion", required = false) String newVersion,
                                                     @ApiParam(name = "need_confirm") @RequestParam(value = "needConfirm", required = false) boolean needConfirm,
                                                     @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                     @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        try
        {
            edgeNodeServiceFacade.agentUpgrade(node_id, newVersion, needConfirm);
            return ResponseEntity.ok().body(null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("upgrade node error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/{node_id}/upgrade-log", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "upgrade node",notes = DescriptionConfig.EDGE_NODE_IMAGES_MSG)
    @LNJoyAuditLog(value = "upgrade node",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH, bindParameterName = "node_id"))
    public ResponseEntity<UpgradeLog> getUpgradeLog(@ApiParam(value = "", required = true, name = "node_id")@PathVariable String node_id,
                                                    @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                    @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        try
        {
            return ResponseEntity.ok().body(edgeNodeServiceFacade.getUpgradeLog(node_id));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("upgrade node error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @PostMapping(value = "/{node_id}/confirm", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "upgrade node",notes = DescriptionConfig.EDGE_NODE_IMAGES_MSG)
    @LNJoyAuditLog(value = "upgrade node", notes = DescriptionConfig.EDGE_NODE_IMAGES_MSG,
            resourceMapperId = EcrmResources.EDGE_COMPUTE,
            actionDescriptionTemplateClass = EdgeActionDescriptionTemplate.class,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH, bindParameterName = "node_id"),
            actionDescriptionField = "action", actionDescriptionFormatString = "upgrade node: %s")
    public ResponseEntity<List<VersionInfo>> confirm(@ApiParam(value = "", required = true, name = "node_id")@PathVariable String node_id,
                                                     @ApiParam(name = "action", required = true) @RequestParam(value = "action", required = false) String action,
                                                     @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                     @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        try
        {
            edgeNodeServiceFacade.confirm(node_id, action);
            return ResponseEntity.ok().body(null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("confirm node error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }


    @GetMapping(value = "/qrcodes/{node_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get qrcodes",notes = DescriptionConfig.EDGE_QRCODES_MSG)
    @LNJoyAuditLog(value = "get qrcodes",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH, bindParameterName = "node_id"))
    public ResponseEntity<GetQRCodeRsp> getQrcodes(@ApiParam(value = "", required = true, name = "node_id")@PathVariable String node_id,
                                                   @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                   @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        try
        {
            return ResponseEntity.ok().body(edgeNodeServiceFacade.getQrcodes(node_id));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("get node qrcode error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @PostMapping(value = "/binds", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "bind node",notes = DescriptionConfig.BIND_EDGE_MSG)
    @LNJoyAuditLog(value = "bind node", notes = DescriptionConfig.BIND_EDGE_MSG,
            actionDescriptionTemplate = EdgeActionDescriptionTemplate.Descriptions.BIND_NODE)
    public ResponseEntity<EdgeBindsRsp> bindNodes(@ApiParam(value = "", required = true, name = "edgeBindsReq")@RequestBody EdgeBindsReq edgeBindsReq,
                                                  @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                  @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        try
        {
            return ResponseEntity.ok().body(edgeNodeServiceFacade.bindNodes(edgeBindsReq));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("bind node error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @PostMapping(value = "/unbinds", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "unbind nodes",notes = DescriptionConfig.BIND_EDGE_MSG)
    @LNJoyAuditLog(value = "unbind nodes", notes = DescriptionConfig.BIND_EDGE_MSG,
            actionDescriptionTemplate = EdgeActionDescriptionTemplate.Descriptions.UNBIND_NODE)
    public ResponseEntity<EdgeUnbindsRsp> unbindNodes(@ApiParam(value = "", required = true, name = "edgeBindsReq")@RequestBody EdgeUnbindsReq edgeUnbindsReq,
                                                      @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                      @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        try
        {
            return ResponseEntity.ok().body(edgeNodeServiceFacade.unbindNodes(edgeUnbindsReq));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("bind node error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }
}
