package com.lnjoying.justice.cmp.controller.openstack.image;

import com.lnjoying.justice.cmp.domain.dto.response.openstack.glance.OSExtImagesWithDetailsRsp;
import com.lnjoying.justice.cmp.entity.search.openstack.OSImageSearchCritical;
import com.lnjoying.justice.cmp.service.openstack.ImageService;
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

@RestSchema(schemaId = "osextensionimage")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/extension/v2/images")
@Api(value = "OpenStack Image Controller",tags = "OpenStack Image Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "os_ext_image"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "os_ext_images"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "OpenStack镜像"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpOsImages")})})
public class OSExtImageController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private ImageService imageService;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List images", response = Object.class)
    public ResponseEntity<OSExtImagesWithDetailsRsp> getImages(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                               @ApiParam(name = "limit") @RequestParam(required = false, value = "limit") Integer limit,
                                                               @ApiParam(name = "marker") @RequestParam(required = false, value = "marker") String marker,
                                                               @ApiParam(name = "name") @RequestParam(required = false, value = "name") String name,
                                                               @ApiParam(name = "owner") @RequestParam(required = false, value = "owner") String owner,
                                                               @ApiParam(name = "protected") @RequestParam(required = false, value = "protected") Boolean protect,
                                                               @ApiParam(name = "status") @RequestParam(required = false, value = "status") String statusStr,
                                                               @ApiParam(name = "tag") @RequestParam(required = false, value = "tag") String tag,
                                                               @ApiParam(name = "visibility") @RequestParam(required = false, value = "visibility") String visibility,
                                                               @ApiParam(name = "os_hidden") @RequestParam(required = false, value = "os_hidden") Boolean osHidden,
                                                               @ApiParam(name = "member_status") @RequestParam(required = false, value = "member_status") String memberStatus,
                                                               @ApiParam(name = "size_max") @RequestParam(required = false, value = "size_max") String sizeMax,
                                                               @ApiParam(name = "size_min") @RequestParam(required = false, value = "size_min") String sizeMin,
                                                               @ApiParam(name = "created_at") @RequestParam(required = false, value = "created_at") String createdAt,
                                                               @ApiParam(name = "updated_at") @RequestParam(required = false, value = "updated_at") String updatedAt,
                                                               @ApiParam(name = "sort_dir") @RequestParam(required = false, value = "sort_dir") String sortDir,
                                                               @ApiParam(name = "sort_key") @RequestParam(required = false, value = "sort_key") String sortKey,
                                                               @ApiParam(name = "sort") @RequestParam(required = false, value = "sort") String sort,
                                                               @ApiParam(name = "page_size") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                               @ApiParam(name = "page_num") @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                               @ApiParam(name = "image_type") @RequestParam(value = "image_type", required = false) String imageType,
                                                               @ApiParam(name = "instance_id") @RequestParam(value = "instance_id", required = false) String instanceId,
                                                               @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get images. cloud:{}", cloudId);

            OSImageSearchCritical imageSearchCritical = new OSImageSearchCritical(limit, marker, name, owner, protect, null,
                    tag, visibility, osHidden, memberStatus, sizeMax, sizeMin, createdAt, updatedAt, sortDir, sortKey, sort,
                    pageNum, pageSize, imageType, instanceId, statusStr);

            OSExtImagesWithDetailsRsp getImagesRsp;

            getImagesRsp = imageService.getImagesPage(cloudId, imageSearchCritical, null);

            return ResponseEntity.ok(getImagesRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get images failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }
}
