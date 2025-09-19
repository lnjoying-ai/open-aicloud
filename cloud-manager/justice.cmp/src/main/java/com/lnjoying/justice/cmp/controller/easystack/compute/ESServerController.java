package com.lnjoying.justice.cmp.controller.easystack.compute;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpOsInstancesKey;
import com.lnjoying.justice.cmp.handler.actiondescription.i18n.zh_cn.CmpOpenstackActionDescriptionTemplate;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.easystack.ESServerService;
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

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isAdmin;

@RestSchema(schemaId = "esserver")
@RequestMapping("/cmp/v1/ESK/clouds/{cloud_id}/v2.1/servers")
@Api(value = "EasyStack Server Controller",tags = "EasyStack Server Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "es_server"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "es_servers"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "OpenStack云主机"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpOsInstances")})})
public class ESServerController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private ESServerService serverService;

    @Autowired
    private CloudService cloudService;

    @DeleteMapping(value = "/{server_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Delete Server", response = Object.class)
    @LNJoyAuditLog(value = "Delete Server",
            resourceMapperId = CmpResources.OS_SERVER,
            actionDescriptionTemplate = CmpOpenstackActionDescriptionTemplate.Descriptions.DELETE_INSTANCE,
            associatedResourceCnName = "云主机",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "serverId", resourcePrimaryKeyClass = TblCmpOsInstancesKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpOsInstancesKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> removeServer(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                               @ApiParam(value = "server_id", required = true, name = "server_id") @PathVariable("server_id")String serverId,
                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("remove server, cloud:{}, server_id:{}", cloudId, serverId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, operUserId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return serverService.removeServer(cloudId, serverId, operUserId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove server failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }
}
