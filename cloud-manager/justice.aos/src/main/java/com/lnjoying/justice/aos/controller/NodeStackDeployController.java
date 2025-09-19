package com.lnjoying.justice.aos.controller;

import com.lnjoying.justice.aos.config.DescriptionConfig;
import com.lnjoying.justice.aos.domain.dto.req.AddNodeStackReq;
import com.lnjoying.justice.aos.domain.dto.rsp.NodeStackRsp;
import com.lnjoying.justice.aos.facade.NodeStackFacade;
import com.lnjoying.justice.commonweb.controller.RestWebController;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import com.micro.core.common.Pair;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.apache.servicecomb.swagger.invocation.exception.InvocationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/4/13 17:15
 */

@Controller
@RestSchema(schemaId = "NodeStackDeployController")
@RequestMapping("/aos/v1/nodes/stacks")
@Api(value = "node stack deployments Controller",tags = {"node stack deployments Controller"})
@Slf4j
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "node_stack_deploy"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "node_stack_deploys"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "节点应用部署"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "")})})
public class NodeStackDeployController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private NodeStackFacade nodeStackFacade;

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "add nodes stack",notes = DescriptionConfig.ADD_STACK_MSG)
    @LNJoyAuditLog(value = "add nodes stack",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.REQUEST_BODY,
                            resourceIdParseSPEL = "#obj?.nodeId"))
    public ResponseEntity<Object> addNodeStack(@ApiParam(required = true, name = "nodeStackReq") @RequestBody @Valid AddNodeStackReq nodeStackReq,
                               @RequestHeader(name = UserHeadInfo.USERID,      required = false) String userId,
                               @RequestHeader(name = UserHeadInfo.BPID,        required = false) String bpId,
                               @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        Pair<NodeStackRsp.TtyStackDeployStatus, String> res = null;

        String nodeId = nodeStackReq.getNodeId();
        String type = nodeStackReq.getType();
        LOGGER.info("add node stack. node_id: {};type: {}", nodeId, type);

        switch (type)
        {
            case "tty":
                res = nodeStackFacade.addNodeTtyStack(nodeId, type);
                break;
            default:
                break;
        }

        InvocationException state = null;
        if (Objects.nonNull(res))
        {
            switch (res.getLeft())
            {
                case DEPLOYED:
                    state = new InvocationException(javax.ws.rs.core.Response.Status.CONFLICT, res.getRight());
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(state);
                case NEW:
                case BUSY:
                    return ResponseEntity.status(HttpStatus.OK).body(res.getRight());
                case ERROR:
                default:
                    log.error("add node tty stack error");
            }
        }

        state = new InvocationException(javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR, "");
        return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(state);
    }


    @GetMapping(value = "/{stack_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get stack by stack id",notes = DescriptionConfig.TEMPLATE_INFO_MSG)
    @LNJoyAuditLog(value = "get stack by stack id",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "stackId"))
    public ResponseEntity<NodeStackRsp>  getNodeStack(@ApiParam(value = "", required = true, name = "stack_id")@PathVariable(value = "stack_id") String stackId,
                                                  @RequestHeader(name = UserHeadInfo.USERID,      required = false) String userId,
                                                  @RequestHeader(name = UserHeadInfo.BPID,        required = false) String bpId,
                                                  @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        
        NodeStackRsp nodeStack = nodeStackFacade.getNodeStack(stackId);
        return ResponseEntity.status(HttpStatus.OK).body(nodeStack);
    }

    @DeleteMapping(value = "/{stack_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete stack by stack id",notes = DescriptionConfig.TEMPLATE_INFO_MSG)
    @LNJoyAuditLog(value = "delete stack by stack id",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "stackId"))
    public ResponseEntity<String>  deleteNodeStack(@ApiParam(value = "", required = true, name = "stack_id")@PathVariable(value = "stack_id") String stackId,
                                                      @RequestHeader(name = UserHeadInfo.USERID,      required = false) String userId,
                                                      @RequestHeader(name = UserHeadInfo.BPID,        required = false) String bpId,
                                                      @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        LOGGER.info("delete node stack. stack_id: {}", stackId);
        nodeStackFacade.toBeDeleted(stackId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
