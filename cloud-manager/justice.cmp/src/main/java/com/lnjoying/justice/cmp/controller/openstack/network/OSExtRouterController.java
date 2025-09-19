package com.lnjoying.justice.cmp.controller.openstack.network;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpOsRoutersKey;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron.OSExtRouterUpdateReq;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSExtRouterWithDetailsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSExtRoutersWithDetailsRsp;
import com.lnjoying.justice.cmp.entity.search.openstack.OSRouterSearchCritical;
import com.lnjoying.justice.cmp.handler.actiondescription.i18n.zh_cn.CmpOpenstackActionDescriptionTemplate;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.openstack.RouterService;
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

@RestSchema(schemaId = "osextensionrouter")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/extension/v2.0/routers")
@Api(value = "OpenStack Router Controller",tags = "OpenStack Router Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "os_ext_router"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "os_ext_routers"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "OpenStack路由器"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpOsRouters")})})
public class OSExtRouterController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private RouterService routerService;

    @Autowired
    private CloudService cloudService;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List Routers", response = Object.class)
    public ResponseEntity<OSExtRoutersWithDetailsRsp> getRouters(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                 @ApiParam(name = "id") @RequestParam(required = false, value = "id") String id,
                                                                 @ApiParam(name = "tenant_id") @RequestParam(required = false, value = "tenant_id") String tenantId,
                                                                 @ApiParam(name = "project_id") @RequestParam(required = false, value = "project_id") String projectId,
                                                                 @ApiParam(name = "name") @RequestParam(required = false, value = "name") String name,
                                                                 @ApiParam(name = "description") @RequestParam(required = false, value = "description") String description,
                                                                 @ApiParam(name = "admin_state_up") @RequestParam(required = false, value = "admin_state_up") Boolean adminStateUp,
                                                                 @ApiParam(name = "revision_number") @RequestParam(required = false, value = "revision_number") Integer revisionNumber,
                                                                 @ApiParam(name = "sort_dir") @RequestParam(required = false, value = "sort_dir") String sortDir,
                                                                 @ApiParam(name = "sort_key") @RequestParam(required = false, value = "sort_key") String sortKey,
                                                                 @ApiParam(name = "tags") @RequestParam(required = false, value = "tags") String tags,
                                                                 @ApiParam(name = "tags-any") @RequestParam(required = false, value = "tags-any") String tagsAny,
                                                                 @ApiParam(name = "not-tags") @RequestParam(required = false, value = "not-tags") String notTags,
                                                                 @ApiParam(name = "not-tags-any") @RequestParam(required = false, value = "not-tags-any") String notTagsAny,
                                                                 @ApiParam(name = "fields") @RequestParam(required = false, value = "fields") String fields,
                                                                 @ApiParam(name = "page_size") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                                 @ApiParam(name = "page_num") @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                                 @ApiParam(name = "status") @RequestParam(required = false, value = "status") String status,
                                                                 @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get routers. cloud:{}", cloudId);

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

            OSRouterSearchCritical routerSearchCritical = new OSRouterSearchCritical(id, tenantId, projectId, name, description,
                    adminStateUp, revisionNumber, sortDir, sortKey, tags, tagsAny, notTags, notTagsAny, fields, pageNum, pageSize, status);

            OSExtRoutersWithDetailsRsp osRoutersWithDetails = routerService.getRoutersPage(cloudId, routerSearchCritical, filterUserId);

            return ResponseEntity.ok(osRoutersWithDetails);
        }
        catch (Exception e)
        {
            LOGGER.error("get routers failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/{router_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Show Router Details", response = Object.class)
    public ResponseEntity<OSExtRouterWithDetailsRsp> getRouterDetails(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                      @ApiParam(value = "router_id", required = true, name = "router_id") @PathVariable("router_id") String routerId,
                                                                      @ApiParam(name = "fields") @RequestParam(required = false, value = "fields") String fields,
                                                                      @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get router details. cloud:{}, router_id:{}", cloudId, routerId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            OSExtRouterWithDetailsRsp routerWithDetails = routerService.getExtRouterDetails(cloudId, routerId, fields, operUserId);
            return ResponseEntity.ok(routerWithDetails);
        }
        catch (Exception e)
        {
            LOGGER.error("get router details: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/{router_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update Router", response = Object.class)
    @LNJoyAuditLog(value = "Update Router",
            resourceMapperId = CmpResources.OS_ROUTER,
            actionDescriptionTemplate = CmpOpenstackActionDescriptionTemplate.Descriptions.EXT_UPDATE_ROUTER,
            associatedResourceCnName = "路由器",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "routerId", resourcePrimaryKeyClass = TblCmpOsRoutersKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpOsRoutersKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> updateRouter(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                               @ApiParam(value = "router_id", required = true, name = "router_id") @PathVariable(value = "router_id") String routerId,
                                               @ApiParam(value = "router", required = true, name = "router") @RequestBody OSExtRouterUpdateReq request,
                                               @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("update router, cloud:{}, request:{}", cloudId, request);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return routerService.updateExtRouter(cloudId, routerId, request, bpId, operUserId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("update router failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }
}
