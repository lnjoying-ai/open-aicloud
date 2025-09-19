package com.lnjoying.justice.cmp.controller.nextstack.vm;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpHypervisorNodeKey;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.baremetal.CommonReq;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm.HypervisorNodeAllocationInfo;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm.NodeAllocationInfosRsp;
import com.lnjoying.justice.cmp.entity.search.nextstack.vm.HypervisorNodeSearchCritical;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.vm.HypervisorNodeAddReq;
import com.lnjoying.justice.cmp.handler.actiondescription.i18n.zh_cn.CmpNextStackActionDescriptionTemplate;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.nextstack.vm.HypervisorNodeServiceBiz;
import com.lnjoying.justice.cmp.service.nextstack.vm.PciDeviceServiceBiz;
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

@RestSchema(schemaId = "hypervisornode")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/api/vm/v1")
@Api(value = "Hypervisor Node Controller",tags = "Hypervisor Node Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
                properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "ns_hypervisor_node"),
                        @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "ns_hypervisor_nodes"),
                        @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "计算节点"),
                        @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpHypervisorNode")})})
public class HypervisorNodeController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private HypervisorNodeServiceBiz vmComputeService;

    @Autowired
    private CloudService cloudService;

    @Autowired
    private PciDeviceServiceBiz pciDeviceService;

    @PostMapping(value = "/hypervisor_nodes", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "add hypervisor node", response = Object.class)
    @LNJoyAuditLog(value = "add hypervisor node",
            resourceMapperId = CmpResources.NS_HYPERVISOR_NODE,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.ADD_HYPERVISOR_NODE,
            associatedResourceCnName = "计算节点",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                    resourceIdParseSPEL = "#obj?.body?.get('nodeId')?.toString()", resourcePrimaryKeyClass = TblCmpHypervisorNodeKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpHypervisorNodeKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> addHypervisorNode(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                    @ApiParam(value = "HypervisorNodeAddReq", required = true, name = "HypervisorNodeAddReq") @RequestBody HypervisorNodeAddReq request,
                                                    @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                    @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("add hypervisor node, cloud:{}, request:{}", cloudId, request);

            return vmComputeService.addHypervisorNode(cloudId, request, bpId, userId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add  hypervisor node failed: {}", e.getMessage());

            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/hypervisor_nodes/{nodeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update hypervisor node", response = Object.class)
    @LNJoyAuditLog(value = "update hypervisor node",
            resourceMapperId = CmpResources.NS_HYPERVISOR_NODE,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.UPDATE_HYPERVISOR_NODE,
            associatedResourceCnName = "计算节点",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "nodeId", resourcePrimaryKeyClass = TblCmpHypervisorNodeKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpHypervisorNodeKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> updateHypervisorNode(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                       @ApiParam(value = "nodeId", required = true, name = "nodeId") @PathVariable("nodeId")String nodeId,
                                                       @ApiParam(value = "CommonReq", required = true, name = "CommonReq") @RequestBody @Valid CommonReq request)
    {
        try
        {
            LOGGER.info("update hypervisor node: hypervisorNodeId:{} request:{} {}", nodeId, request.getName(), request.getDescription());

            return cloudService.sendHttpRequestToCloud(cloudId);
        }
        catch (Exception e)
        {
            LOGGER.error("update hypervisor node failed: {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @DeleteMapping(value = "/hypervisor_nodes/{nodeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "remove hypervisor node", response = Object.class)
    @LNJoyAuditLog(value = "remove hypervisor node",
            resourceMapperId = CmpResources.NS_HYPERVISOR_NODE,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.DELETE_HYPERVISOR_NODE,
            associatedResourceCnName = "计算节点",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "nodeId", resourcePrimaryKeyClass = TblCmpHypervisorNodeKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpHypervisorNodeKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> removeHypervisorNode(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                       @ApiParam(value = "nodeId", required = true, name = "nodeId") @PathVariable("nodeId")String nodeId,
                                                       @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("remove hypervisor node, cloud:{}, nodeId:{}", cloudId, nodeId);

            return vmComputeService.removeHypervisorNode(cloudId, nodeId, userId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove  hypervisor node failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/hypervisor_nodes/{nodeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get hypervisor node info", response = Object.class)
    public ResponseEntity<HypervisorNodeAllocationInfo> getHypervisorNode(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                          @ApiParam(value = "nodeId", required = true, name = "nodeId") @PathVariable("nodeId")String nodeId,
                                                                          @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get hypervisor node info, cloud:{}, nodeId:{}", cloudId, nodeId);

            HypervisorNodeAllocationInfo allocationInfo = pciDeviceService.resourceAllocation(cloudId, nodeId);
            if (null != allocationInfo)
            {
                if (null == allocationInfo.getMemSum())
                {
                    allocationInfo.setMemSum(0);
                }
                if (null == allocationInfo.getCpuSum())
                {
                    allocationInfo.setCpuSum(0);
                }
            }
            return ResponseEntity.ok(allocationInfo);
        }
        catch (Exception e)
        {
            LOGGER.error("get hypervisor node failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/hypervisor_nodes", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get hypervisor nodes", response = Object.class)
    public ResponseEntity<NodeAllocationInfosRsp> getHypervisorNodes(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                     @ApiParam(name = "name") @RequestParam(required = false) String name,
                                                                     @ApiParam(name = "is_healthy") @RequestParam(required = false) Boolean isHealthy,
                                                                     @ApiParam(name = "page_size") @RequestParam(required = false, value = "page_size") Integer pageSize,
                                                                     @ApiParam(name = "page_num") @RequestParam(required = false, value = "page_num") Integer pageNum)
    {
        try
        {
            LOGGER.debug("get hypervisor node list, cloud:{}", cloudId);

            HypervisorNodeSearchCritical pageSearchCritical = new HypervisorNodeSearchCritical();
            if (null != name) pageSearchCritical.setName(name);
            if (null != pageNum) pageSearchCritical.setPageNum(pageNum);
            if (null != pageSize) pageSearchCritical.setPageSize(pageSize);
            if (null != isHealthy) pageSearchCritical.setIsHealthy(isHealthy);
            NodeAllocationInfosRsp getHypervisorNodesRsp= vmComputeService.getHypervisorNodes(cloudId, pageSearchCritical);
            return ResponseEntity.ok(getHypervisorNodesRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get hypervisor node list failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/hypervisor_nodes/allocation", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get hypervisor nodes", response = Object.class)
    public ResponseEntity<Object> getResourceAllocation(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                        @ApiParam(name = "flavor_id") @RequestParam(required = true, value = "flavor_id") String flavorId,
                                                        @ApiParam(name = "page_size") @RequestParam(required = false, value = "page_size") Integer pageSize,
                                                        @ApiParam(name = "page_num") @RequestParam(required = false,value = "page_num") Integer pageNum )
    {
        try
        {
            return cloudService.sendHttpRequestToCloud(cloudId);
        }
        catch (Exception e)
        {
            LOGGER.error("get hypervisor node list failed: {}", e.getMessage());
            throw throwWebException(e);
        }
    }
}
