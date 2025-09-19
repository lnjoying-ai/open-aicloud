package com.lnjoying.justice.iam.controller;

import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.iam.domain.dto.request.resource.AddCommonResourceReq;
import com.lnjoying.justice.iam.domain.dto.request.resource.UpdateCommonResourceReq;
import com.lnjoying.justice.iam.domain.dto.request.service.UpdateServiceReq;
import com.lnjoying.justice.iam.domain.dto.response.resource.ResourceRsp;
import com.lnjoying.justice.iam.service.CommonResourceService;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.constant.ResourceOperationTypeConstants;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Map;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.iam.utils.ServiceCombRequestUtils.*;
import static com.lnjoying.justice.iam.utils.ServiceCombRequestUtils.getUserAttributes;
import static com.lnjoying.justice.schema.common.ErrorCode.IAM_ILLEGAL_OPERATION;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/16 19:04
 */

@RestSchema(schemaId = "common-resource-manager")
@RequestMapping("/api/iam/v1/schema/common-resources")
@Controller
@Api(value = "common resource Controller",tags = {"common resource Controller"})
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "common_resource"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "common_resources"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "普通资源"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "tbl_iam_common_resource")})})
@Slf4j
public class CommonResourceController extends IamRestWebController
{

    @Autowired
    private CommonResourceService commonResourceService;

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "add common resource")
    @LNJoyAuditLog(value = "add common resource",
            tags = ResourceOperationTypeConstants.RESOURCE_CREATE,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                            resourceIdParseSPEL = "#obj?.body?.toString()"))
    public ResponseEntity<Object> addCommonResource(@ApiParam(required = true, name = "commonResourceReq") @RequestBody @Valid AddCommonResourceReq req)
    {
        String bpId = getBpId();
        String userId = getUserId();
        log.info("add common resource. req: {}, userId: {}", req, userId);

        setBaseReq(req, bpId, userId);
        return status(CREATED).body(commonResourceService.addCommonResource(req));
    }

    @PutMapping(value = "/{resource_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update resource")
    @LNJoyAuditLog(value = "update resource",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "resourceId"))
    public ResponseEntity<Map<String, String>> updateResource(@ApiParam(required = true, name = "resource_id") @PathVariable String resourceId,
                                                             @ApiParam(required = true, name = "req") @RequestBody @Valid UpdateCommonResourceReq req)
    {
        String bpId = getBpId();
        String userId = getUserId();
        log.info("update resource. req: {}, userId: {}", req, userId);

        // update userId and bpId
        setBaseReq(req, bpId, userId);
        commonResourceService.updateResource(resourceId, req);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get common resource list")
    public ResponseEntity<Object> getCommonResourceList(@ApiParam(name = "name") @RequestParam(value = "name", required = false) String name,
                                                 @ApiParam(name = "page_size") @RequestParam(value = "page_size", required = false, defaultValue = Integer.MAX_VALUE + "") Integer pageSize,
                                                 @ApiParam(name = "page_num") @RequestParam(value = "page_num", required = false, defaultValue = "1") Integer pageNum,
                                                 @ApiParam(name = "status") @RequestParam(value = "status", required = false) Integer status)
    {

        ResourceRsp resourceRsp = commonResourceService.getCommonResourceList(name, status, pageSize, pageNum);

        return status(OK).body(resourceRsp);
    }


    @GetMapping(value = "/{resource_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get resource info")
    @LNJoyAuditLog(value = "get resource info",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "resourceId"))
    public ResponseEntity<Object> getCommonResource(@ApiParam(required = true, name = "resource_id") @PathVariable String resourceId)
    {
        String queryBpId = "";
        if (!isAdmin())
        {
            queryBpId = getUserAttributes().getLeft();
        }
        return status(OK).body(commonResourceService.getCommonResource(resourceId, queryBpId));

    }

    @DeleteMapping(value = "/{resource_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete policy")
    @LNJoyAuditLog(value = "delete policy",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "resourceId"))
    public ResponseEntity<String> deleteCommonResource(@ApiParam(required = true, name = "resource_id") @PathVariable String resourceId)
    {

        log.info("delete common resource: {}, userId: {}", resourceId, getUserId());

        String queryBpId = "";
        if (!isAdmin())
        {
          throw new WebSystemException(IAM_ILLEGAL_OPERATION, ErrorLevel.ERROR);
        }
        commonResourceService.deleteCommonResource(resourceId, queryBpId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
