package com.lnjoying.justice.ecrm.controller;

import com.lnjoying.justice.commonweb.controller.RestWebController;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.ecrm.config.DescriptionConfig;
import com.lnjoying.justice.ecrm.domain.dto.request.CheckUniqueReq;
import com.lnjoying.justice.ecrm.domain.dto.response.CheckUniqueRsp;
import com.lnjoying.justice.ecrm.facade.RegionServiceFacade;
import com.lnjoying.justice.ecrm.facade.SiteServiceFacade;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.constant.RoleConstants;
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

@RestSchema(schemaId = "unique")
@RequestMapping("/ecrm/v1")
@Api(value = "Unique Check Controller",tags = "Unique Check Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "unique"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "uniques"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "区域站点唯一性"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "")})})
public class UniqueCheckController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    RegionServiceFacade regionServiceFacade;

    @Autowired
    SiteServiceFacade siteServiceFacade;

    @PostMapping(value = "/uniqueness", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Check if region/site is unique",notes = DescriptionConfig.CHECK_UNIQUE_MSG)
    public ResponseEntity<CheckUniqueRsp> checkUnique(@ApiParam(value = "check_unique_req", required = false, name = "check_unique_req") @RequestBody CheckUniqueReq checkUniqueReq,
                                                      @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        try
        {
            CheckUniqueRsp checkUniqueRsp = new CheckUniqueRsp(false, false);
            if (StringUtils.isNotEmpty(checkUniqueReq.getRegionId()))
            {
                checkUniqueRsp.setRegionId(regionServiceFacade.checkUnique(checkUniqueReq.getRegionId()));
            }
            if (StringUtils.isNotEmpty(checkUniqueReq.getSiteId()))
            {
                checkUniqueRsp.setSiteId(siteServiceFacade.checkUnique(checkUniqueReq.getSiteId()));
            }
            return ResponseEntity.ok().body(checkUniqueRsp);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("Check unique error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }
}
