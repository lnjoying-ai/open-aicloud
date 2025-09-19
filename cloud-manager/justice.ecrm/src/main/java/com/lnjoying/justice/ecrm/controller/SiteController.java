package com.lnjoying.justice.ecrm.controller;

import com.lnjoying.justice.commonweb.controller.RestWebController;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.ecrm.config.DescriptionConfig;
import com.lnjoying.justice.ecrm.domain.dto.request.SiteInputReq;
import com.lnjoying.justice.ecrm.domain.dto.response.QuerySiteRsp;
import com.lnjoying.justice.ecrm.facade.SiteServiceFacade;
import com.lnjoying.justice.schema.constant.ResourceOperationTypeConstants;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;

@RestSchema(schemaId = "sites")
@RequestMapping("/ecrm/v1/sites")
@Api(value = "Site Controller",tags = "Site Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "site"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "sites"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "站点"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblSiteInfo")})})
public class SiteController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    SiteServiceFacade siteServiceFacade;

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "add site",notes = DescriptionConfig.ADD_SITE_MSG)
    @LNJoyAuditLog(value = "add site",
            tags = ResourceOperationTypeConstants.RESOURCE_CREATE,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                            resourceIdParseSPEL = "#obj?.body?.toString()"))
    public ResponseEntity<String> addSite(@ApiParam(value = "", required = true, name = "siteIputInfo")@RequestBody SiteInputReq siteInputReq,
                                          @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                          @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        try
        {
            siteServiceFacade.addSite(siteInputReq);
            return ResponseEntity.status(HttpStatus.CREATED).body(siteInputReq.getId());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("create region error. {}", e.getMessage());
            throw throwWebException(e);
        }

    }

    @PutMapping(value = "/{site_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update site",notes = DescriptionConfig.UPDATE_SITE_MSG)
    @LNJoyAuditLog(value = "update site",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "site_id"))
    public ResponseEntity<String> updateSite(@ApiParam(value = "site_id", required = true, name = "site_id")@PathVariable String site_id,
                                             @ApiParam(value = "", required = true, name = "siteIputInfo")@RequestBody SiteInputReq siteInputReq,
                                             @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                             @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        try
        {
            siteServiceFacade.updateSite(site_id, siteInputReq);
            return ResponseEntity.ok(null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("create region error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @DeleteMapping(value = "/{site_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete site",notes = DescriptionConfig.DELETE_SITE_MSG)
    @LNJoyAuditLog(value = "delete site",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "site_id"))
    public ResponseEntity<String> deleteRegion(@ApiParam(value = "siteId", required = true, name = "site_id")@PathVariable String site_id,
                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                               @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        try
        {
            siteServiceFacade.deleteSite(site_id);
            return ResponseEntity.ok(null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("delete site error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get site list",notes = DescriptionConfig.SITE_LIST_MSG)
    public ResponseEntity<QuerySiteRsp> getSites(@ApiParam(value = "region_id", required = false, name = "region_id") @RequestParam(required = false) String region_id,
                                                 @ApiParam(value = "site_id", required = false, name = "site_id") @RequestParam(required = false) String site_id,
                                                 @ApiParam(value = "orchestration", required = false, name = "orchestration") @RequestParam(required = false) String orchestration,
                                                 @ApiParam(value = "name", required = false, name = "name") @RequestParam(required = false) String name,
                                                 @ApiParam(name = "page_size") @RequestParam(value = "page_size", required = false, defaultValue = Integer.MAX_VALUE + "")  Integer pageSize,
                                                 @ApiParam(name = "page_num") @RequestParam(value = "page_num", required = false, defaultValue = "1") Integer pageNum,
                                                 @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                 @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities,
                                                 @RequestHeader(name = UserHeadInfo.USERKIND,    required = false) String userKind)
    {


        try
        {
            return ResponseEntity.ok().body(siteServiceFacade.getPageableSites(region_id, site_id, orchestration, name, pageNum, pageSize));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("delete site error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }
}
