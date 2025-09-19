package com.lnjoying.justice.cmp.controller.openstack.network;

import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSExtSecurityGroupsWithDetailsRsp;
import com.lnjoying.justice.cmp.entity.search.openstack.OSSecurityGroupSearchCritical;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.openstack.SecurityGroupService;
import com.lnjoying.justice.cmp.utils.osclient.OSClientUtil;
import com.lnjoying.justice.commonweb.controller.RestWebController;
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

@RestSchema(schemaId = "osextensionsecuritygroup")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/extension/v2.0/security-groups")
@Api(value = "OpenStack Security Groups Controller",tags = "OpenStack Security Groups Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "os_ext_securitygroup"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "os_ext_securitygroups"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "OpenStack安全组"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpOsSecuritygroups")})})
public class OSExtSecurityGroupController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private SecurityGroupService securityGroupService;

    @Autowired
    private CloudService cloudService;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List security groups", response = Object.class)
    public ResponseEntity<OSExtSecurityGroupsWithDetailsRsp> getSecurityGroups(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
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
                                                                               @ApiParam(name = "page_size") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                                               @ApiParam(name = "page_num") @RequestParam(value = "pageNum", required = false) Integer pageNum,
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
                    pageNum, pageSize);

            OSExtSecurityGroupsWithDetailsRsp securityGroupsWithDetailsRsp = securityGroupService.getSecurityGroupsPage(cloudId, securityGroupSearchCritical, filterUserId);

            return ResponseEntity.ok(securityGroupsWithDetailsRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get security groups failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }
}
