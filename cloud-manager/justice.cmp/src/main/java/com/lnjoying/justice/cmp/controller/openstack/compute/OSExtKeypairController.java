package com.lnjoying.justice.cmp.controller.openstack.compute;

import com.lnjoying.justice.cmp.domain.dto.response.openstack.nova.OSExtKeyPairsRsp;
import com.lnjoying.justice.cmp.entity.search.openstack.OSKeyPairSearchCritical;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.openstack.KeypairService;
import com.lnjoying.justice.commonweb.controller.RestWebController;
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

@RestSchema(schemaId = "osextensionkeypair")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/extension/v2.1/os-keypairs")
@Api(value = "OpenStack Keypair Controller",tags = "OpenStack Keypair Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "os_ext_keypair"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "os_ext_keypairs"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "OpenStack SSH密钥对"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpOsKeyPairs")})})
public class OSExtKeypairController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private CloudService cloudService;

    @Autowired
    private KeypairService keypairService;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List Keypairs", response = Object.class)
    public ResponseEntity<OSExtKeyPairsRsp> getKeypairs(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                        @ApiParam(name = "user_id") @RequestParam(required = false, value = "user_id") String userId,
                                                        @ApiParam(name = "limit") @RequestParam(required = false, value = "limit") Integer limit,
                                                        @ApiParam(name = "marker") @RequestParam(required = false, value = "marker") String marker,
                                                        @ApiParam(name = "page_size") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                        @ApiParam(name = "page_num") @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                        @ApiParam(name = "name") @RequestParam(required = false, value = "name") String name,
                                                        @RequestHeader(name = UserHeadInfo.USERID, required = false) String operUserId)
    {
        try
        {
            LOGGER.info("get keypairs. cloud:{}", cloudId);

            String filterUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                filterUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            OSKeyPairSearchCritical keyPairSearchCritical = new OSKeyPairSearchCritical(userId, limit, marker, pageNum, pageSize, name);

            OSExtKeyPairsRsp keyPairsRsp = keypairService.getKeypairsPage(cloudId, keyPairSearchCritical, filterUserId);

            return ResponseEntity.ok(keyPairsRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get keypairs failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }
}
