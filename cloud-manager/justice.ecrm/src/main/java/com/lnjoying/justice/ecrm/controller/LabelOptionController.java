package com.lnjoying.justice.ecrm.controller;

import com.lnjoying.justice.commonweb.controller.RestWebController;
import com.lnjoying.justice.ecrm.config.DescriptionConfig;
import com.lnjoying.justice.ecrm.domain.dto.model.LabelOption;
import com.lnjoying.justice.ecrm.facade.LabelOptionServiceFacade;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;


@RestSchema(schemaId = "labels")
@RequestMapping("/ecrm/v1")
@Api(value = "Label Controller",tags = "Label Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "label"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "labels"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "标签"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblLabelOptionInfo")})})
public class LabelOptionController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    LabelOptionServiceFacade labelOptionServiceFacade;

    @GetMapping(value = "/labels/show-inputs/{label_type}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get label option",notes = DescriptionConfig.GET_LABEL_OPTION_MSG)
    public ResponseEntity<List<LabelOption>> getLabelOption(@ApiParam(value = "label_type", required = false, name = "label_type") @PathVariable(value = "label_type") String labelType,
                                                 @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        try
        {
            return ResponseEntity.ok().body(labelOptionServiceFacade.getLabelOption(labelType));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("get label option error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }


    @GetMapping(value = "/taints/show-inputs/{taint_type}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get label option",notes = DescriptionConfig.GET_TAINT_OPTION_MSG)
    public ResponseEntity<List<LabelOption>> getTaintOption(@ApiParam(value = "taint_type", required = false, name = "taint_type") @PathVariable(value = "taint_type") String taintType,
                                                            @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {

        try
        {
            return ResponseEntity.ok().body(labelOptionServiceFacade.getTaintOption(taintType));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("get taint option error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }
}
