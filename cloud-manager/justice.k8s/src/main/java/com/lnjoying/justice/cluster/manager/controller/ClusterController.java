package com.lnjoying.justice.cluster.manager.controller;

import com.lnjoying.justice.cluster.manager.common.ClsResources;
import com.lnjoying.justice.cluster.manager.common.DescriptionConfig;
import com.lnjoying.justice.cluster.manager.domain.UserTest;
import com.lnjoying.justice.cluster.manager.domain.dto.model.cluster.CoreServiceMetrics;
import com.lnjoying.justice.cluster.manager.domain.dto.req.cluster.AddClusterInfoReqDto;
import com.lnjoying.justice.cluster.manager.domain.dto.req.cluster.ExportClusterTemplateReqDto;
import com.lnjoying.justice.cluster.manager.domain.dto.req.cluster.UpdateClusterInfoReqDto;
import com.lnjoying.justice.cluster.manager.domain.dto.req.cluster.RepairClusterReqDto;
import com.lnjoying.justice.cluster.manager.domain.dto.rsp.cluster.*;
import com.lnjoying.justice.cluster.manager.domain.model.UserBasicInfo;
import com.lnjoying.justice.cluster.manager.domain.search.ClusterNodeSearchCritical;
import com.lnjoying.justice.cluster.manager.domain.search.ClusterSearchCritical;
import com.lnjoying.justice.cluster.manager.handler.actiondescription.i18n.zh_cn.ClusterInfoActionDescriptionTemplate;
import com.lnjoying.justice.cluster.manager.service.cluster.ClusterService;
import com.lnjoying.justice.cluster.manager.service.data.ClusterDataService;
import com.lnjoying.justice.cluster.manager.service.template.ClusterTemplateService;
import com.lnjoying.justice.cluster.manager.util.ClsUtils;
import com.lnjoying.justice.commonweb.controller.RestWebController;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.constant.ResourceOperationTypeConstants;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import com.micro.core.common.Pair;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lnjoying.justice.cluster.manager.common.CLusterAction.EXPORT_TEMPLATE;
import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;

/**
 * @description desc k8s multi cluster manager
 * @author Regulus
 * @date 11/27/21 3:23 PM
 */
@RestSchema(schemaId = "clusters")
@RequestMapping("/cls/v1/clusters")
@Api(value = "cluster Controller",tags = "cluster Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "cluster"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "clusters"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "集群"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblClusterInfo")})})
public class ClusterController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private ClusterService clusterService;

    @Autowired
    private ClusterTemplateService clusterTemplateService;
    
    @Autowired
    private ClusterDataService clusterDataService;
    
    /**
     * @description  add new cluster
     * @author Regulus
     * @date 11/15/21 7:12 PM
     * @param clusterInfoReq
     * @param userId
     * @param bpId
     * @param authorities 
     * @return org.springframework.http.ResponseEntity<java.util.Map<java.lang.String,java.lang.String>>
     */
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "post clusters",notes = DescriptionConfig.ADD_CLUSTER_MSG)
    @LNJoyAuditLog(value = "post clusters",
            tags = ResourceOperationTypeConstants.RESOURCE_CREATE,
            resourceMapperId = ClsResources.CLUSTER_INFO,
            actionDescriptionTemplate = ClusterInfoActionDescriptionTemplate.Descriptions.ADD_CLUSTER,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                            resourceIdParseSPEL = "#obj?.body?.get('id')?.toString()"))
    public ResponseEntity<Map<String,String>>  addCluster(@ApiParam(value = "", required = true, name = "body")  @Valid @RequestBody AddClusterInfoReqDto clusterInfoReq,
                                                          @RequestHeader(name = UserHeadInfo.USERID,      required = false) String userId,
                                                          @RequestHeader(name = UserHeadInfo.BPID,        required = false) String bpId,
                                                          @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities,
                                                          @RequestHeader(name = UserHeadInfo.USERKIND,    required = false) String userKind) throws WebSystemException
    {
        LOGGER.debug("add cluster. {}", clusterInfoReq);
        
        String clusterId = clusterService.createCluster(clusterInfoReq, userId, bpId);
        Map<String,String> retMap = new HashMap<>();
        retMap.put("id",clusterId);
        return ResponseEntity.status(HttpStatus.CREATED).body(retMap);
    }
    
    @RequestMapping(value = "/tesst/ust", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "post clusters",
            tags = ResourceOperationTypeConstants.RESOURCE_CREATE,
            notes = DescriptionConfig.ADD_CLUSTER_MSG)
    public ResponseEntity<UserTest>  testUser(@ApiParam(value = "", required = true, name = "add cluster info") @RequestBody UserTest userTest) throws WebSystemException
    {
        LOGGER.info(userTest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userTest);
    }
    
    /**
     * @description delete cluster
     * @author Regulus
     * @date 11/15/21 7:44 PM
     * @param clusterId
     * @param userId
     * @param bpId
     * @param authorities
     * @return org.springframework.http.ResponseEntity<java.lang.String>
     */
    @RequestMapping(value = "/{cluster_id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete clusters",notes = DescriptionConfig.DELETE_CLUSTER_MSG)
    @LNJoyAuditLog(value = "delete clusters",
            resourceMapperId = ClsResources.CLUSTER_INFO,
            actionDescriptionTemplate = ClusterInfoActionDescriptionTemplate.Descriptions.DELETE_CLUSTER,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "clusterId"))
    public ResponseEntity<String>  deleteCluster(@ApiParam(value = "cluster id") @PathVariable("cluster_id")       String   clusterId,
                                                 @ApiParam(name = "force") @RequestParam(value = "force", required = false, defaultValue = "false") boolean force,
                                                 @RequestHeader(name = UserHeadInfo.USERID,      required = false) String      userId,
                                                 @RequestHeader(name = UserHeadInfo.BPID,        required = false) String        bpId,
                                                 @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities,
                                                 @RequestHeader(name = UserHeadInfo.USERKIND,    required = false) String userKind) throws WebSystemException
    {
    
        UserBasicInfo userBasicInfo = UserBasicInfo.builder().userId(userId).bpId(bpId).userKind(userKind).build();
        clusterService.removeCluster(clusterId, userBasicInfo, force);
        return ResponseEntity.ok().body(null);
    }
    
    @RequestMapping(value = "/{cluster_id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update clusters",notes = DescriptionConfig.UPDATE_CLUSTER_MSG)
    @LNJoyAuditLog(value = "update clusters",
            resourceMapperId = ClsResources.CLUSTER_INFO,
            actionDescriptionTemplate = ClusterInfoActionDescriptionTemplate.Descriptions.UPDATE_CLUSTER,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "clusterId"))
    public ResponseEntity<String>  updateCluster(@ApiParam(value = "cluster id") @PathVariable("cluster_id")       String clusterId,
                                                 @ApiParam(value = "", required = true, name = "reqbody") @RequestBody UpdateClusterInfoReqDto updateClusterInfoReq,
                                                 @RequestHeader(name = UserHeadInfo.USERID,      required = false) String userId,
                                                 @RequestHeader(name = UserHeadInfo.BPID,        required = false) String bpId,
                                                 @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities,
                                                 @RequestHeader(name = UserHeadInfo.USERKIND,    required = false) String userKind) throws WebSystemException
    {
        
        UserBasicInfo userBasicInfo = UserBasicInfo.builder().bpId(bpId).userKind(userKind).userId(userId).build();
        clusterService.updateCluster(clusterId, updateClusterInfoReq,userBasicInfo);
        return ResponseEntity.ok().body(null);
    }
    
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get clusters",notes = DescriptionConfig.UPDATE_CLUSTER_MSG)
    public ResponseEntity<ClusterListInfoRspDto>  getClusterList(@ApiParam(name = "name") @RequestParam(value = "name", required = false) String name,
                                                                @ApiParam(name = "page_size") @RequestParam(value = "page_size", required = false, defaultValue = Integer.MAX_VALUE + "")  Integer pageSize,
                                                                 @ApiParam(name = "page_num") @RequestParam(value = "page_num", required = false, defaultValue = "1")  Integer pageNum,
                                                                 @ApiParam(name = "access_user_id") @RequestParam(value = "shared_user", required = false, defaultValue = "")  String sharedUser,
                                                                 @ApiParam(name = "owner") @RequestParam(value = "owner", required = false, defaultValue = "")  String owner,
                                                                 @RequestHeader(name = UserHeadInfo.USERID,      required = false) String userId,
                                                                 @RequestHeader(name = UserHeadInfo.BPID,        required = false) String bpId,
                                                                 @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities,
                                                                 @RequestHeader(name = UserHeadInfo.USERKIND,    required = false) String userKind) throws WebSystemException
    {

        Pair<String,String> operUserInfo = ClsUtils.getOperUserInfo(userId, bpId, userKind);
    
        ClusterSearchCritical critical = new ClusterSearchCritical();
        critical.setName(name);
        critical.setPageNum(pageNum);
        critical.setPageSize(pageSize);
        critical.setOwner(owner);
        critical.setOperUserId(operUserInfo.getLeft());
        critical.setOperBpId(operUserInfo.getRight());
        critical.setSharedUser(sharedUser);
        
        return ResponseEntity.ok().body(clusterService.getClusterList(critical));
    }
    
    @RequestMapping(value = "/{cluster_id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get cluster by cluster_id",notes = DescriptionConfig.UPDATE_CLUSTER_MSG)
    @LNJoyAuditLog(value = "get cluster by cluster_id",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "clusterId"))
    public ResponseEntity<ClusterInfoRspDto>  getCluster(@ApiParam(value = "cluster id") @PathVariable("cluster_id")           String clusterId,
                                                         @RequestHeader(name = UserHeadInfo.USERID,      required = false)     String userId,
                                                         @RequestHeader(name = UserHeadInfo.BPID,        required = false)     String bpId,
                                                         @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false)     String authorities,
                                                         @RequestHeader(name = UserHeadInfo.USERKIND,    required = false) String userKind) throws WebSystemException
    {

        UserBasicInfo userBasicInfo = UserBasicInfo.builder().bpId(bpId).userKind(userKind).userId(userId).build();
        return ResponseEntity.ok().body(clusterService.getClusterInfo(clusterId, userBasicInfo));
    }
    
    
    @RequestMapping(value = "/{cluster_id}/import-cmds", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get cluster import cmd by cluster_id",notes = DescriptionConfig.GET_IMPORT_CLUSTER_CMD_MSG)
    @LNJoyAuditLog(value = "get cluster import cmd by cluster_id",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "clusterId"))
    public ResponseEntity<ClusterImportCmdRspDto>  getImportClusterCmd(@ApiParam(value = "cluster id") @PathVariable("cluster_id")        String clusterId,
                                                                       @RequestHeader(name = UserHeadInfo.USERID,      required = false)  String userId,
                                                                       @RequestHeader(name = UserHeadInfo.BPID,        required = false)  String bpId,
                                                                       @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false)  String authorities,
                                                                       @RequestHeader(name = UserHeadInfo.USERKIND,    required = false) String userKind) throws WebSystemException
    {

        return ResponseEntity.ok().body(clusterService.getImportClusterCmd(clusterId));
    }
    
    @RequestMapping(value = "/{cluster_id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "operator cluster by action",notes = DescriptionConfig.EXPORT_CLUSTER_MSG)
    @LNJoyAuditLog(value = "operator cluster by action",
            resourceMapperId = ClsResources.CLUSTER_INFO,
            actionDescriptionTemplateClass = ClusterInfoActionDescriptionTemplate.class,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "clusterId"),
            actionDescriptionField = "action",
            actionDescriptionFormatString = "operator cluster action: %s")
    public ResponseEntity<String> exportCluster2Template(@ApiParam(value = "cluster id") @PathVariable("cluster_id")       String clusterId,
                                                         @ApiParam(value = "action") @RequestParam("action")      @Valid @NotBlank String action,
                                                         @ApiParam(value = "", required = true, name = "template params for export cluster") @Valid @RequestBody ExportClusterTemplateReqDto exportClusterTemplateReq,
                                                         @RequestHeader(name = UserHeadInfo.USERID,      required = false) String userId,
                                                         @RequestHeader(name = UserHeadInfo.BPID,        required = false) String bpId,
                                                         @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities) throws WebSystemException
    {

        if (StringUtils.isBlank(exportClusterTemplateReq.getOwner()))
        {
            exportClusterTemplateReq.setOwner(userId);
        }
        exportClusterTemplateReq.setCreator(userId);
        exportClusterTemplateReq.setBp(bpId);
        exportClusterTemplateReq.setClusterId(clusterId);
        if (EXPORT_TEMPLATE.getName().equalsIgnoreCase(action))
        {
            clusterTemplateService.exportClusterTemplateFromCluster(exportClusterTemplateReq);
        }
        return ResponseEntity.ok().body(null);
    }
    
    @RequestMapping(value = "/{cluster_id}/nodes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get cluster cluster deploy nodes",notes = DescriptionConfig.GET_IMPORT_CLUSTER_CMD_MSG)
    @LNJoyAuditLog(value = "get cluster cluster deploy nodes",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "clusterId"))
    public ResponseEntity<ClusterNodesRspDto>  getClusterNodes(@ApiParam(value = "cluster id") @PathVariable("cluster_id")        String clusterId,
                                                               @RequestHeader(name = UserHeadInfo.USERID,      required = false)  String userId,
                                                               @RequestHeader(name = UserHeadInfo.BPID,        required = false)  String bpId,
                                                               @ApiParam(name = "page_size") @RequestParam(value = "page_size", required = false, defaultValue = Integer.MAX_VALUE + "") Integer pageSize,
                                                               @ApiParam(name = "page_num") @RequestParam(value = "page_num", required = false, defaultValue = "1")  Integer pageNum,
                                                               @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false)  String authorities,
                                                               @RequestHeader(name = UserHeadInfo.USERKIND,    required = false) String userKind) throws WebSystemException
    {


        Pair<String, String> operuserInfo = ClsUtils.getOperUserInfo(userId, bpId, userKind);
    
        ClusterNodeSearchCritical critical =  ClusterNodeSearchCritical.builder().operBpId(operuserInfo.getRight())
                                              .operUserId(operuserInfo.getLeft()).clusterId(clusterId).build();
        critical.setPageNum(pageNum);
        critical.setPageSize(pageSize);
        return ResponseEntity.ok().body(clusterService.getClusterNodesInfo(critical));
    }
    
    @RequestMapping(value = "/versions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get cluster versions ",notes = DescriptionConfig.GET_IMPORT_CLUSTER_CMD_MSG)
    public ResponseEntity<Map<String, List<String>>>  getClusterVersions(@ApiParam(name = "type") @RequestParam(value = "type", required = false, defaultValue = "k8s") String type) throws WebSystemException
    {

        return ResponseEntity.ok().body(clusterDataService.getClusterVersions(type));
    }

    @RequestMapping(value = "/{cluster_id}/kubeconfig", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get cluster config by cluster_id",notes = DescriptionConfig.GET_IMPORT_CLUSTER_CMD_MSG)
    @LNJoyAuditLog(value = "get cluster config by cluster_id",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "clusterId"))
    public ResponseEntity<String>  getKubeconfig(@ApiParam(value = "cluster id") @PathVariable("cluster_id")        String clusterId,
                                                                       @RequestHeader(name = UserHeadInfo.USERID,      required = false)  String userId,
                                                                       @RequestHeader(name = UserHeadInfo.BPID,        required = false)  String bpId,
                                                                       @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false)  String authorities,
                                                                       @RequestHeader(name = UserHeadInfo.USERKIND,    required = false) String userKind) throws WebSystemException
    {

        Pair<String, String> operUserInfo = ClsUtils.getOperUserInfo(userId, bpId, userKind);
        return ResponseEntity.ok().body(clusterService.getKubeConfig(clusterId, operUserInfo));
    }
    
    @RequestMapping(value = "/{cluster_id}/states", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update clusters service state",notes = DescriptionConfig.UPDATE_CLUSTER_MSG)
    @LNJoyAuditLog(value = "update clusters service state",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "clusterId"))
    public ResponseEntity<String>  updateClusterState(@ApiParam(value = "cluster id") @PathVariable("cluster_id")       String clusterId,
                                                 @ApiParam(value = "", required = true, name = "action") @RequestParam String action,
                                                 @RequestHeader(name = UserHeadInfo.USERID,      required = false) String userId,
                                                 @RequestHeader(name = UserHeadInfo.BPID,        required = false) String bpId,
                                                 @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities,
                                                 @RequestHeader(name = UserHeadInfo.USERKIND,    required = false) String userKind) throws WebSystemException
    {
        
        UserBasicInfo userBasicInfo = UserBasicInfo.builder().bpId(bpId).userKind(userKind).userId(userId).build();
        
        clusterService.updateClsServiceState(clusterId, action, userBasicInfo);
        return ResponseEntity.ok().body(null);
    }

    @RequestMapping(value = "/basic", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get clusters basic info",notes = DescriptionConfig.GET_CLUSTER_BASIC_INFO_LIST_MSG)
    public ResponseEntity<List<ClusterBasicInfoRspDto>>  getClusterBasicInfoList(@ApiParam(name = "name") @RequestParam(value = "name", required = false) String name,
                                                                                 @ApiParam(name = "access_user_id") @RequestParam(value = "shared_user", required = false, defaultValue = "")  String sharedUser,
                                                                                 @ApiParam(name = "owner") @RequestParam(value = "owner", required = false, defaultValue = "")  String owner,
                                                                                 @RequestHeader(name = UserHeadInfo.USERID,      required = false) String userId,
                                                                                 @RequestHeader(name = UserHeadInfo.BPID,        required = false) String bpId,
                                                                                 @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities,
                                                                                 @RequestHeader(name = UserHeadInfo.USERKIND,    required = false) String userKind) throws WebSystemException
    {

        Pair<String,String> operUserInfo = ClsUtils.getOperUserInfo(userId, bpId, userKind);

        if (operUserInfo.getLeft() != null && !operUserInfo.getLeft().equals(owner))
        {
            if (owner == null)
            {
                owner = operUserInfo.getLeft();
            }
            else if (!operUserInfo.getLeft().equals(owner))
            {
                throw new WebSystemException(ErrorCode.CLUSTER_K8S_NOT_GRANTED, ErrorLevel.INFO);
            }
        }

        ClusterSearchCritical critical = new ClusterSearchCritical();
        critical.setName(name);
        critical.setOwner(owner);
        critical.setOperUserId(operUserInfo.getLeft());
        critical.setOperBpId(operUserInfo.getRight());
        critical.setSharedUser(sharedUser);

        return ResponseEntity.ok().body(clusterService.getClusterBasicInfoList(critical));
    }

    @RequestMapping(value = "/{cluster_id}/terminal", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get cluster terminal",notes = DescriptionConfig.GET_CLUSTER_TERMINAL_MSG)
    @LNJoyAuditLog(value = "get cluster terminal",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "clusterId"))
    public ResponseEntity<ClusterTerminalRspDto> getClusterTerminal(@ApiParam(value = "cluster id") @PathVariable("cluster_id")       String clusterId,
                                                                    @RequestHeader(name = UserHeadInfo.USERID,      required = false)  String userId,
                                                                    @RequestHeader(name = UserHeadInfo.BPID,        required = false)  String bpId,
                                                                    @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false)  String authorities,
                                                                    @RequestHeader(name = UserHeadInfo.USERKIND,    required = false) String userKind) throws WebSystemException
    {

        UserBasicInfo userBasicInfo = UserBasicInfo.builder().bpId(bpId).userKind(userKind).userId(userId).build();

        return ResponseEntity.ok().body(clusterService.getClusterTerminal(clusterId, userBasicInfo));
    }

    @RequestMapping(value = "/nodes/{node_id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get cluster node info by node id",notes = DescriptionConfig.GET_CLUSTER_NODE_INFO_MSG)
    @LNJoyAuditLog(value = "get cluster node info by node id",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "nodeId"))
    public ResponseEntity<ClusterNodeRspDto> getClusterNodeInfoByNodeId(@ApiParam(value = "node id") @PathVariable("node_id")        String nodeId,
                                                                        @RequestHeader(name = UserHeadInfo.USERID,      required = false)  String userId,
                                                                        @RequestHeader(name = UserHeadInfo.BPID,        required = false)  String bpId,
                                                                        @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false)  String authorities,
                                                                        @RequestHeader(name = UserHeadInfo.USERKIND,    required = false) String userKind) throws WebSystemException
    {

        return ResponseEntity.ok().body(clusterService.getClusterNodeInfo(nodeId));
    }

    @RequestMapping(value = "/{cluster_id}/core-service-metrics", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get cluster core service metrics",notes = DescriptionConfig.GET_CLUSTER_CORE_SERVICE_METRICS_MSG)
    @LNJoyAuditLog(value = "get cluster core service metrics",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "clusterId"))
    public ResponseEntity<List<CoreServiceMetrics>> getClusterCoreServiceMetrics(@ApiParam(value = "cluster id") @PathVariable("cluster_id")       String clusterId,
                                                                                 @RequestHeader(name = UserHeadInfo.USERID,      required = false)  String userId,
                                                                                 @RequestHeader(name = UserHeadInfo.BPID,        required = false)  String bpId,
                                                                                 @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false)  String authorities,
                                                                                 @RequestHeader(name = UserHeadInfo.USERKIND,    required = false) String userKind) throws WebSystemException
    {


        return ResponseEntity.ok().body(clusterService.getClusterCoreServiceMetrics(clusterId));
    }

    @RequestMapping(value = "/repair/{cluster_id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "repair cluster",notes = DescriptionConfig.REPAIR_CLUSTER_MSG)
    @LNJoyAuditLog(value = "repair cluster",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "clusterId"))
    public ResponseEntity<String> repairCluster(@ApiParam(value = "cluster id") @PathVariable("cluster_id")       String clusterId,
                                                                  @ApiParam(value = "", required = true, name = "body") @Valid @RequestBody RepairClusterReqDto repairClusterReqDto,
                                                                  @RequestHeader(name = UserHeadInfo.USERID,      required = false)  String userId,
                                                                  @RequestHeader(name = UserHeadInfo.BPID,        required = false)  String bpId,
                                                                  @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false)  String authorities,
                                                                  @RequestHeader(name = UserHeadInfo.USERKIND,    required = false) String userKind) throws WebSystemException
    {

        clusterService.repairCluster(clusterId, repairClusterReqDto);
        return ResponseEntity.ok().body(null);
    }
}
