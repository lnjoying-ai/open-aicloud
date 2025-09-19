package com.lnjoying.justice.iam.controller;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.iam.common.constant.IamResources;
import com.lnjoying.justice.iam.domain.dto.request.policy.AddPolicyReq;
import com.lnjoying.justice.iam.domain.dto.request.policy.AddPolicyVersionReq;
import com.lnjoying.justice.iam.domain.dto.request.policy.PatchPolicyVersionReq;
import com.lnjoying.justice.iam.domain.dto.request.policy.UpdatePolicyReq;
import com.lnjoying.justice.iam.domain.dto.response.policy.PoliciesRsp;
import com.lnjoying.justice.iam.handler.actiondescription.i18n.zh_cn.PolicyActionDescriptionTemplate;
import com.lnjoying.justice.iam.handler.actiondescription.i18n.zh_cn.PolicyVersionActionDescriptionTemplate;
import com.lnjoying.justice.iam.service.PolicyService;
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
import static com.lnjoying.justice.iam.utils.ServiceCombRequestUtils.getUserId;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/10 15:23
 */

@RestSchema(schemaId = "policies-manager")
@RequestMapping("/api/iam/v1/policies")
@Controller
@Api(value = "policy Controller",tags = {"policy Controller"})
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "policy"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "policies"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "策略"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "tbl_iam_policy")})})
@Slf4j
public class PolicyController  extends IamRestWebController
{

    @Autowired
    private PolicyService policyService;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get policy list")
    public ResponseEntity<Object> getPolicyList(@ApiParam(name = "name") @RequestParam(value = "name", required = false) String name,
                                                @ApiParam(name = "policy_type") @RequestParam(value = "policy_type", required = false) Integer policyType,
                                                 @ApiParam(name = "page_size") @RequestParam(value = "page_size", required = false, defaultValue = Integer.MAX_VALUE + "") Integer pageSize,
                                                 @ApiParam(name = "page_num") @RequestParam(value = "page_num", required = false, defaultValue = "1") Integer pageNum,
                                                 @ApiParam(name = "bp_id") @RequestParam(value = "bp_id", required = false) String queryBpId)
    {

        if (!isAdmin())
        {
            queryBpId = getUserAttributes().getLeft();
        }
        PoliciesRsp policyList = policyService.getPolicyList(name, queryBpId, policyType, pageSize, pageNum);

        return status(OK).body(policyList);
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "add policy")
    @LNJoyAuditLog(value = "add policy",
            tags = ResourceOperationTypeConstants.RESOURCE_CREATE,
            actionDescriptionTemplateClass = PolicyActionDescriptionTemplate.class,
            isResourceInstanceNameFromArgs = true,
            resourceInstanceNameFromArgsParseSpEl = "#obj?.get('displayName')?.toString()",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                    resourceIdParseSPEL = "#obj?.body?.toString()"))
    public ResponseEntity<Object> addPolicy(@ApiParam(required = true, name = "policyReq") @RequestBody @Valid AddPolicyReq req)
    {
        String bpId = getBpId();
        String userId = getUserId();
        log.info("add policy. req: {}, userId: {}", req, userId);

        setBaseReq(req, bpId, userId);
        return status(CREATED).body(policyService.addPolicy(req));
    }

    @DeleteMapping(value = "/{policy_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete policy")
    @LNJoyAuditLog(value = "delete policy",
            resourceMapperId = IamResources.POLICY,
            actionDescriptionTemplateClass = PolicyActionDescriptionTemplate.class,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "policyId"))
    public ResponseEntity<String> deletePolicy(@ApiParam(required = true, name = "policy_id") @PathVariable String policyId)
    {

        log.info("delete policy: {}, userId: {}", policyId, getUserId());

        String queryBpId = "";
        if (!isAdmin())
        {
            queryBpId = getUserAttributes().getLeft();
        }
        policyService.deletePolicy(policyId, queryBpId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }


    @PutMapping(value = "/{policy_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update policy")
    @LNJoyAuditLog(value = "update policy",
            actionDescriptionTemplateClass = PolicyActionDescriptionTemplate.class,
            isResourceInstanceNameFromArgs = true,
            resourceInstanceNameFromArgsParseSpEl = "#obj?.get('displayName')?.toString()",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "policyId"))
    public ResponseEntity<Map<String, String>> updatePolicy(@ApiParam(required = true, name = "policy_id") @PathVariable String policyId,
                                                             @ApiParam(required = true, name = "req") @RequestBody @Valid UpdatePolicyReq req)
    {
        String bpId = getBpId();
        String userId = getUserId();
        log.info("update policy. req: {}, userId: {}", req, userId);

        // update userId and bpId
        setBaseReq(req, bpId, userId);
        policyService.updatePolicy(policyId, req);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(value = "/{policy_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get policy info")
    @LNJoyAuditLog(value = "get policy info",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "policyId"))
    public ResponseEntity<Object> getPolicy(@ApiParam(required = true, name = "policy_id") @PathVariable String policyId)
    {
        String queryBpId = "";
        if (!isAdmin())
        {
            queryBpId = getUserAttributes().getLeft();
        }
        return status(OK).body(policyService.getPolicy(policyId, queryBpId));

    }

    @GetMapping(value = "/{policy_id}/versions", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get policy versions")
    @LNJoyAuditLog(value = "get policy versions",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "policyId"))
    public ResponseEntity<Object> getPolicyVersions(@ApiParam(required = true, name = "policy_id") @PathVariable String policyId)
    {

        return status(OK).body(policyService.getPolicyVersions(policyId));
    }

    @PostMapping(value = "/{policy_id}/versions", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "add policy version")
    @LNJoyAuditLog(value = "add policy version",
            tags = ResourceOperationTypeConstants.RESOURCE_CREATE,
            isResourceInstanceNameFromArgs = true,
            resourceInstanceNameFromArgsParseSpEl = "#obj?.get('versionId')?.toString()",
            actionDescriptionTemplate = PolicyVersionActionDescriptionTemplate.Descriptions.ADD_POLICY_VERSION,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                    resourceIdParseSPEL = "#obj?.body?.toString()"))
    public ResponseEntity<Object> addPolicyVersion(@ApiParam(required = true, name = "policy_id") @PathVariable String policyId,
                                                   @ApiParam(required = true, name = "policyReq") @RequestBody @Valid AddPolicyVersionReq req)
    {
        String bpId = getBpId();
        String userId = getUserId();
        log.info("add policy version. req: {}, userId: {}", req, userId);

        setBaseReq(req, bpId, userId);
        return status(CREATED).body(policyService.addPolicyVersion(policyId, req));
    }


    @GetMapping(value = "/{policy_id}/versions/{version_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get policy version")
    @LNJoyAuditLog(value = "get policy version",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "policyId"))
    public ResponseEntity<Object> getPolicyVersion(@ApiParam(required = true, name = "policy_id") @PathVariable String policyId,
                                                    @ApiParam(required = true, name = "version_id") @PathVariable String versionId)
    {

        return status(OK).body(policyService.getPolicyVersion(policyId, versionId));
    }


    @DeleteMapping(value = "/{policy_id}/versions/{version_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete policy version")
    @LNJoyAuditLog(value = "delete policy version",
            isResourceInstanceNameFromArgs = true,
            resourceInstanceNameFromArgsParseSpEl = "#obj?.get('versionId')?.toString()",
            actionDescriptionTemplate = PolicyVersionActionDescriptionTemplate.Descriptions.DELETE_POLICY_VERSION,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "policyId"))
    public ResponseEntity<Object> deletePolicyVersion(@ApiParam(required = true, name = "policy_id") @PathVariable String policyId,
                                                   @ApiParam(required = true, name = "version_id") @PathVariable String versionId)
    {

        log.info("delete policy version, policyId: {}, versionId: {}, userId: {}", policyId, versionId, getUserId());
        policyService.deletePolicyVersion(policyId, versionId);
        return status(OK).body(null);
    }

    @PatchMapping(value = "/{policy_id}/versions/{version_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "patch policy version")
    @LNJoyAuditLog(value = "patch policy version",
            isResourceInstanceNameFromArgs = true,
            resourceInstanceNameFromArgsParseSpEl = "#obj?.get('versionId')?.toString()",
            actionDescriptionTemplate = PolicyVersionActionDescriptionTemplate.Descriptions.UPDATE_POLICY_VERSION,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "policyId"))
    public ResponseEntity<Object> patchPolicyVersion(@ApiParam(required = true, name = "policy_id") @PathVariable String policyId,
                                                      @ApiParam(required = true, name = "version_id") @PathVariable String versionId,
                                                      @ApiParam(required = true, name = "policyReq") @RequestBody @Valid PatchPolicyVersionReq req)
    {

        log.info("update policy default version, policyId: {}, versionId: {}, userId: {}", policyId, versionId, getUserId());
        policyService.patchPolicyVersion(policyId, versionId, req);
        return status(OK).body(null);
    }


    @GetMapping(value = "/{policy_id}/entities", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get policy entities info")
    @LNJoyAuditLog(value = "get policy entities info",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "policyId"))
    public ResponseEntity<Object> getPolicyEntities(@ApiParam(required = true, name = "policy_id") @PathVariable String policyId)
    {
        String queryBpId = "";
        if (!isAdmin())
        {
            queryBpId = getUserAttributes().getLeft();
        }
        return status(OK).body(policyService.getPolicyEntities(policyId, queryBpId));

    }
}
