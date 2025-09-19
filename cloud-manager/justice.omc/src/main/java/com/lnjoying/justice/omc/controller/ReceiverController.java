package com.lnjoying.justice.omc.controller;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.omc.common.OmcResources;
import com.lnjoying.justice.omc.db.model.TblOmcAlertInhibitionRule;
import com.lnjoying.justice.omc.db.model.TblOmcAlertSilenceRule;
import com.lnjoying.justice.omc.db.repo.AlertReduceRepository;
import com.lnjoying.justice.omc.domain.dto.req.AddReceiverReq;
import com.lnjoying.justice.omc.domain.dto.req.UpdateReceiverReq;
import com.lnjoying.justice.omc.domain.dto.rsp.ReceiversRsp;
import com.lnjoying.justice.omc.handler.actiondescription.i18n.ReceiverActionDescriptionTemplate;
import com.lnjoying.justice.omc.service.ReceiverService;
import com.lnjoying.justice.schema.constant.ResourceOperationTypeConstants;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Extension;
import io.swagger.annotations.ExtensionProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;
import java.util.Map;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.*;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.getBpName;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/1 19:57
 */

@RestSchema(schemaId = "ReceiverController")
@RequestMapping(path = "/omc/v1/receivers")
@Slf4j
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "receiver"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "receivers"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "通知对象"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "tbl_omc_receiver")})})
public class ReceiverController extends OmcWebController
{
    @Autowired
    private ReceiverService receiverService;

    @Autowired
    private AlertReduceRepository alertReduceRepository;

    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "add receiver")
    @LNJoyAuditLog(value = "add receiver",
            tags = ResourceOperationTypeConstants.RESOURCE_CREATE,
            actionDescriptionTemplate = ReceiverActionDescriptionTemplate.Descriptions.ADD_RECEIVER,
            isResourceInstanceNameFromArgs = true,
            resourceInstanceNameFromArgsParseSpEl = "#obj?.get('name')?.toString()",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                            resourceIdParseSPEL = "#obj?.body?.toString()"))
    public ResponseEntity<Object> addReceiver(@ApiParam(required = true, name = "addReceiverReq") @RequestBody @Valid AddReceiverReq addReceiverReq)
    {
        log.info("add receiver . req: {}, userId: {}", addReceiverReq, getUserId());


        setBaseReq(addReceiverReq, getUserId(), getBpId(), getUserName(), getBpName());

        return status(CREATED).body(receiverService.addReceiver(addReceiverReq));
    }

    @PutMapping(value = "/{receiver_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update receiver")
    @LNJoyAuditLog(value = "update receiver",
            actionDescriptionTemplate = ReceiverActionDescriptionTemplate.Descriptions.UPDATE_RECEIVER,
            isResourceInstanceNameFromArgs = true,
            resourceInstanceNameFromArgsParseSpEl = "#obj?.get('name')?.toString()",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "receiverId"))
    public ResponseEntity<Map<String, String>> updateReceiver(@ApiParam(required = true, name = "receiver_id") @PathVariable String receiverId,
                                                          @ApiParam(required = true, name = "req") @RequestBody @Valid UpdateReceiverReq req)
    {
        String bpId = getBpId();
        String userId = getUserId();
        log.info("update receiver. req: {}, userId: {}", req, userId);

        // update userId and bpId
        setBaseReq(req, getUserId(), getBpId(), getUserName(), getBpName());
        receiverService.updateReceiver(receiverId, req);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }


    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get receivers")
    public ResponseEntity<Object> getReceiverList(@ApiParam(name = "name") @RequestParam(value = "name", required = false) String name,
                                                     @ApiParam(name = "page_size") @RequestParam(value = "page_size", required = false, defaultValue = Integer.MAX_VALUE + "") Integer pageSize,
                                                     @ApiParam(name = "page_num") @RequestParam(value = "page_num", required = false, defaultValue = "1") Integer pageNum)
    {

        String queryBpId = "";
        if (!isAdmin())
        {
            queryBpId = getUserAttributes().getLeft();
        }
        ReceiversRsp receiversRsp = receiverService.getReceiverList(queryBpId, queryUserId(), name, pageNum, pageSize);
        return status(OK).body(receiversRsp);

    }


    @DeleteMapping(value = "/{receiver_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete receiver")
    @LNJoyAuditLog(value = "delete receiver",
            resourceMapperId = OmcResources.RECEIVER,
            actionDescriptionTemplate = ReceiverActionDescriptionTemplate.Descriptions.DELETE_RECEIVER,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "receiverId"))
    public ResponseEntity<String> deleteReceiver(@ApiParam(required = true, name = "receiver_id") @PathVariable String receiverId)
    {

        log.info("delete receiver: {}, userId: {}", receiverId, getUserId());

        receiverService.deleteReceiver(receiverId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(value = "/inhibition", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get receivers")
    public ResponseEntity<Object> getNotifyList(@ApiParam(name = "name") @RequestParam(value = "name", required = false) String name,
                                                  @ApiParam(name = "page_size") @RequestParam(value = "page_size", required = false, defaultValue = Integer.MAX_VALUE + "") Integer pageSize,
                                                  @ApiParam(name = "page_num") @RequestParam(value = "page_num", required = false, defaultValue = "1") Integer pageNum)
    {
        List<TblOmcAlertInhibitionRule> tblOmcAlertInhibitionRules = alertReduceRepository.selectAllAlertInhibitionRules();
        return ResponseEntity.status(HttpStatus.OK).body(tblOmcAlertInhibitionRules);
    }

    @GetMapping(value = "/silence", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get receivers")
    public ResponseEntity<Object> getSilenceList(@ApiParam(name = "name") @RequestParam(value = "name", required = false) String name,
                                                @ApiParam(name = "page_size") @RequestParam(value = "page_size", required = false, defaultValue = Integer.MAX_VALUE + "") Integer pageSize,
                                                @ApiParam(name = "page_num") @RequestParam(value = "page_num", required = false, defaultValue = "1") Integer pageNum)
    {
        List<TblOmcAlertSilenceRule> tblOmcAlertSilenceRules = alertReduceRepository.selectAllAlertSilenceRules();
        return ResponseEntity.status(HttpStatus.OK).body(tblOmcAlertSilenceRules);
    }
}
