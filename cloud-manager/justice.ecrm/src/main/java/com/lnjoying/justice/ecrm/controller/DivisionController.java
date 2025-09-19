package com.lnjoying.justice.ecrm.controller;

import com.lnjoying.justice.commonweb.controller.RestWebController;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.ecrm.config.DescriptionConfig;
import com.lnjoying.justice.ecrm.config.model.AdminDivisionInfo;
import com.lnjoying.justice.ecrm.domain.dto.model.NationInfo;
import com.lnjoying.justice.ecrm.facade.DivisionServiceFacade;
import io.swagger.annotations.*;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;

@RestSchema(schemaId = "divisions")
@RequestMapping("/ecrm/v1")
@Api(value = "Division Controller",tags = "Division Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
		properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "division"),
				@ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "divisions"),
				@ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "区位"),
				@ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "")})})
public class DivisionController extends RestWebController
{
	@Autowired
	DivisionServiceFacade divisionServiceFacade;

	@RequestMapping(value = "/nations", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "get nations",notes = DescriptionConfig.NATIONS_MSG)
	public ResponseEntity<List<NationInfo>> getNations()
	{
		List<NationInfo> nationInfos = divisionServiceFacade.getNaitons();
		return ResponseEntity.ok().body(nationInfos);
	}

	@GetMapping(value = "/divisions/{nation}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "get divisions by nation",notes = DescriptionConfig.NATION_DIVISIONS_MSG)
	@LNJoyAuditLog(value = "get divisions by nation",
	        resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
	                        bindParameterName = "nation"))
	public ResponseEntity<List<AdminDivisionInfo>> getLevel1Divisions(@ApiParam(value = "", required = true, name = "nation")@PathVariable String nation)
	{
		List<AdminDivisionInfo> adminDivisionInfos = divisionServiceFacade.getDivisions(nation);
		return ResponseEntity.ok().body(adminDivisionInfos);
	}

	@GetMapping(value = "/divisions/{nation}/{adcode}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "get divisions by nation and adcode",notes = DescriptionConfig.DIVISIONS_MSG)
	@LNJoyAuditLog(value = "get divisions by nation and adcode",
	        resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
	                        bindParameterName = "nation"))
	public ResponseEntity<List<AdminDivisionInfo>> getDivision(@ApiParam(value = "", required = true, name = "nation")@PathVariable String nation,
															   @ApiParam(value = "", required = false, name = "adcode")@PathVariable String adcode)
	{
		List<AdminDivisionInfo> adminDivisionInfos = divisionServiceFacade.getDivisionByAdcode(nation,adcode);
		return ResponseEntity.ok().body(adminDivisionInfos);
	}
}
