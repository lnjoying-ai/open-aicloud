package com.lnjoying.justice.cmp.controller.openstack.network;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpOsSecuritygroupsKey;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron.OSSecurityGroupCreateReq;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron.OSSecurityGroupUpdateReq;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSSecurityGroupWithDetailsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSSecurityGroupsWithDetailsRsp;
import com.lnjoying.justice.cmp.entity.search.openstack.OSSecurityGroupSearchCritical;
import com.lnjoying.justice.cmp.handler.actiondescription.i18n.zh_cn.CmpOpenstackActionDescriptionTemplate;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.openstack.SecurityGroupService;
import com.lnjoying.justice.cmp.utils.osclient.OSClientUtil;
import com.lnjoying.justice.commonweb.controller.RestWebController;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
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

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isAdmin;

@RestSchema(schemaId = "ossecuritygroup")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/v2.0/security-groups")
@Api(value = "OpenStack Security Groups Controller",tags = "OpenStack Security Groups Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "os_securitygroup"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "os_securitygroups"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "OpenStack安全组"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpOsSecuritygroups")})})
public class OSSecurityGroupController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private SecurityGroupService securityGroupService;

    @Autowired
    private CloudService cloudService;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List security groups", response = Object.class)
    public ResponseEntity<OSSecurityGroupsWithDetailsRsp> getSecurityGroups(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                            @ApiParam(name = "id") @RequestParam(required = false, value = "id") String id,
                                                                            @ApiParam(name = "tenant_id") @RequestParam(required = false, value = "tenant_id") String tenantId,
                                                                            @ApiParam(name = "project_id") @RequestParam(required = false, value = "project_id") String projectId,
                                                                            @ApiParam(name = "revision_number") @RequestParam(required = false, value = "revision_number") Integer revisionNumber,
                                                                            @ApiParam(name = "name") @RequestParam(required = false, value = "name") String name,
                                                                            @ApiParam(name = "description") @RequestParam(required = false, value = "description") String description,
                                                                            @ApiParam(name = "sort_dir") @RequestParam(required = false, value = "sort_dir") String sortDir,
                                                                            @ApiParam(name = "sort_key") @RequestParam(required = false, value = "sort_key") String sortKey,
                                                                            @ApiParam(name = "shared") @RequestParam(required = false, value = "shared") Boolean shared,
                                                                            @ApiParam(name = "tags") @RequestParam(required = false, value = "tags") String tags,
                                                                            @ApiParam(name = "tags-any") @RequestParam(required = false, value = "tags-any") String tagsAny,
                                                                            @ApiParam(name = "not-tags") @RequestParam(required = false, value = "not-tags") String notTags,
                                                                            @ApiParam(name = "not-tags-any") @RequestParam(required = false, value = "not-tags-any") String notTagsAny,
                                                                            @ApiParam(name = "fields") @RequestParam(required = false, value = "fields") String fields,
                                                                            @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get security groups. cloud:{}", cloudId);

            String filterUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                filterUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            if (StringUtils.isEmpty(projectId))
            {
                projectId = OSClientUtil.getOSProject(cloudId);
            }

            OSSecurityGroupSearchCritical securityGroupSearchCritical = new OSSecurityGroupSearchCritical(id, tenantId, projectId,
                    revisionNumber, name, description, sortDir, sortKey, shared, tags, tagsAny, notTags, notTagsAny, fields,
                    null, null);

            OSSecurityGroupsWithDetailsRsp securityGroupsWithDetailsRsp = securityGroupService.getSecurityGroups(cloudId, securityGroupSearchCritical, filterUserId);

            return ResponseEntity.ok(securityGroupsWithDetailsRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get security groups failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create security group", response = Object.class)
    @LNJoyAuditLog(value = "Create security group",
            resourceMapperId = CmpResources.OS_SECURITYGROUP,
            actionDescriptionTemplate = CmpOpenstackActionDescriptionTemplate.Descriptions.CREATE_SECURITYGROUP,
            associatedResourceCnName = "安全组",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                    resourceIdParseSPEL = "#obj?.body?.get('securityGroup')?.get('id')?.toString()", resourcePrimaryKeyClass = TblCmpOsSecuritygroupsKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpOsSecuritygroupsKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> addSecurityGroup(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                             @ApiParam(value = "security_group", required = true, name = "security_group") @RequestBody OSSecurityGroupCreateReq request,
                                             @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                             @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("add security group, cloud:{}, request:{}", cloudId, request);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return securityGroupService.addSecurityGroup(cloudId, request, bpId, userId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add security group failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/{security_group_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Show security group", response = Object.class)
    public ResponseEntity<OSSecurityGroupWithDetailsRsp> getSecurityGroupDetails(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                                 @ApiParam(value = "security_group_id", required = true, name = "security_group_id") @PathVariable("security_group_id") String securityGroupId,
                                                                                 @ApiParam(name = "fields") @RequestParam(required = false, value = "fields") String fields,
                                                                                 @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get security group details. cloud:{}, security_group_id:{}", cloudId, securityGroupId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            OSSecurityGroupWithDetailsRsp securityGroupWithDetailsRsp = securityGroupService.getSecurityGroupDetails(cloudId, securityGroupId, fields, operUserId);

            return ResponseEntity.ok(securityGroupWithDetailsRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get security group failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/{security_group_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update security group", response = Object.class)
    @LNJoyAuditLog(value = "Update security group",
            resourceMapperId = CmpResources.OS_SECURITYGROUP,
            actionDescriptionTemplate = CmpOpenstackActionDescriptionTemplate.Descriptions.UPDATE_SECURITYGROUP,
            associatedResourceCnName = "安全组",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "securityGroupId", resourcePrimaryKeyClass = TblCmpOsSecuritygroupsKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpOsSecuritygroupsKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> updateSecurityGroup(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                @ApiParam(value = "security_group_id", required = true, name = "security_group_id") @PathVariable(value = "security_group_id") String securityGroupId,
                                                @ApiParam(value = "security_group", required = true, name = "security_group") @RequestBody OSSecurityGroupUpdateReq request,
                                                @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("update security group, cloud:{}, request:{}", cloudId, request);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return securityGroupService.updateSecurityGroup(cloudId, securityGroupId, request, operUserId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("update security group failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @DeleteMapping(value = "/{security_group_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Delete security group", response = Object.class)
    @LNJoyAuditLog(value = "Delete security group",
            resourceMapperId = CmpResources.OS_SECURITYGROUP,
            actionDescriptionTemplate = CmpOpenstackActionDescriptionTemplate.Descriptions.DELETE_SECURITYGROUP,
            associatedResourceCnName = "安全组",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "securityGroupId", resourcePrimaryKeyClass = TblCmpOsSecuritygroupsKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpOsSecuritygroupsKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> removeSecurityGroup(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                @ApiParam(value = "security_group_id", required = true, name = "security_group_id") @PathVariable("security_group_id")String securityGroupId,
                                                @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("remove security group, cloud:{}, security_group_id:{}", cloudId, securityGroupId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return securityGroupService.removeSecurityGroup(cloudId, securityGroupId, operUserId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove security group failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }
}
