package com.lnjoying.justice.omc.controller;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.omc.common.OmcResources;
import com.lnjoying.justice.omc.domain.dto.req.AddAlarmReq;
import com.lnjoying.justice.omc.domain.dto.req.AlertLogProcessReq;
import com.lnjoying.justice.omc.domain.dto.req.UpdateAlarmReq;
import com.lnjoying.justice.omc.domain.dto.rsp.*;
import com.lnjoying.justice.omc.domain.model.DailyAlertLogTrendRsp;
import com.lnjoying.justice.omc.handler.actiondescription.i18n.AlarmActionDescriptionTemplate;
import com.lnjoying.justice.omc.service.AlarmService;
import com.lnjoying.justice.omc.util.DateUtils;
import com.lnjoying.justice.schema.constant.ResourceOperationTypeConstants;
import com.micro.core.common.Pair;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Extension;
import io.swagger.annotations.ExtensionProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import java.util.Date;
import java.util.Map;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.*;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.getBpName;
import static com.lnjoying.justice.omc.util.DateUtils.getDate;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/1 14:59
 */

@RestSchema(schemaId = "AlarmController")
@RequestMapping(path = "/omc/v1/alarms")
@Slf4j
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "alarm"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "alarms"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "报警器"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "tbl_omc_monitor_instance")})})
public class AlarmController extends OmcWebController
{

    @Autowired
    private AlarmService alarmService;

    @GetMapping(value = "/groups", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get alarm groups")
    public ResponseEntity<Object> getAlarmGroupList(@ApiParam(name = "name") @RequestParam(value = "name", required = false) String name,
                                                     @ApiParam(name = "page_size") @RequestParam(value = "page_size", required = false, defaultValue = Integer.MAX_VALUE + "") Integer pageSize,
                                                     @ApiParam(name = "page_num") @RequestParam(value = "page_num", required = false, defaultValue = "1") Integer pageNum)
    {

        AlertGroupsRsp alarmGroupsRsp = alarmService.getAlarmGroupList(name, pageNum, pageSize);
        return status(OK).body(alarmGroupsRsp);

    }


    @GetMapping(value = "/metrics", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get alarm metrics")
    public ResponseEntity<Object> getAlarmMetricList(@ApiParam(name = "metric_name") @RequestParam(value = "metric_name", required = false) String metricName,
                                                     @ApiParam(name = "group_id") @RequestParam(value = "group_id", required = false) String groupId,
                                                    @ApiParam(name = "page_size") @RequestParam(value = "page_size", required = false, defaultValue = Integer.MAX_VALUE + "") Integer pageSize,
                                                    @ApiParam(name = "page_num") @RequestParam(value = "page_num", required = false, defaultValue = "1") Integer pageNum)
    {

        AlertMetricsRsp alarmAlertsRsp = alarmService.getAlarmMetricList(metricName, groupId, pageNum, pageSize);
        return status(OK).body(alarmAlertsRsp);

    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "add alarm ")
    @LNJoyAuditLog(value = "add alarm ",
            tags = ResourceOperationTypeConstants.RESOURCE_CREATE,
            actionDescriptionTemplate = AlarmActionDescriptionTemplate.Descriptions.ADD_ALARM,
            isResourceInstanceNameFromArgs = true,
            resourceInstanceNameFromArgsParseSpEl = "#obj?.get('name')?.toString()",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                            resourceIdParseSPEL = "#obj?.body?.toString()"))
    public ResponseEntity<Object> addAlarm(@ApiParam(required = true, name = "integrationTaskReq") @RequestBody @Valid AddAlarmReq addAlarmReq)
    {
        log.info("add alarm. req: {}, userId: {}", addAlarmReq, getUserId());


        setBaseReq(addAlarmReq, getUserId(), getBpId(), getUserName(), getBpName());

        return status(CREATED).body(alarmService.addAlarm(addAlarmReq));
    }

    @PutMapping(value = "/{alarm_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update alarm")
    @LNJoyAuditLog(value = "update alarm",
            resourceMapperId = OmcResources.ALARM,
            actionDescriptionTemplate = AlarmActionDescriptionTemplate.Descriptions.UPDATE_ALARM,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "alarmId"))
    public ResponseEntity<Map<String, String>> updateAlarm(@ApiParam(required = true, name = "alarm_id") @PathVariable String alarmId,
                                                             @ApiParam(required = true, name = "req") @RequestBody @Valid UpdateAlarmReq req)
    {
        String bpId = getBpId();
        String userId = getUserId();
        log.info("update alarm. req: {}, userId: {}", req, userId);

        // update userId and bpId
        setBaseReq(req, getUserId(), getBpId(), getUserName(), getBpName());
        alarmService.updateAlarm(alarmId, req);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get alarm ")
    public ResponseEntity<Object> getAlarmList(@ApiParam(name = "rule_type") @RequestParam(value = "rule_type", required = false) Integer ruleType,
                                                     @ApiParam(name = "group_id") @RequestParam(value = "group_id", required = false) String groupId,
                                                     @ApiParam(name = "name") @RequestParam(value = "name", required = false) String name,
                                                     @ApiParam(name = "page_size") @RequestParam(value = "page_size", required = false, defaultValue = Integer.MAX_VALUE + "") Integer pageSize,
                                                     @ApiParam(name = "page_num") @RequestParam(value = "page_num", required = false, defaultValue = "1") Integer pageNum)
    {
        String queryBpId = "";
        if (!isAdmin())
        {
            queryBpId = getUserAttributes().getLeft();
        }
        AlarmsRsp alarmsRsp = alarmService.getAlarmList(queryBpId, queryUserId(), ruleType, groupId, name, pageNum, pageSize);
        return status(OK).body(alarmsRsp);

    }

    @DeleteMapping(value = "/{alarm_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete alarm")
    @LNJoyAuditLog(value = "delete alarm",
            resourceMapperId = OmcResources.ALARM,
            actionDescriptionTemplate = AlarmActionDescriptionTemplate.Descriptions.DELETE_ALARM,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "alarmId"))
    public ResponseEntity<String> deleteAlarm(@ApiParam(required = true, name = "alarm_id") @PathVariable String alarmId)
    {

        log.info("delete alarm: {}, userId: {}", alarmId, getUserId());

        alarmService.deleteAlarm(alarmId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(value = "/{alarm_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get alarm info")
    @LNJoyAuditLog(value = "get alarm info",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "alarmId"))
    public ResponseEntity<Object> getAlarm(@ApiParam(required = true, name = "alarm_id") @PathVariable String alarmId)
    {

        return status(OK).body(alarmService.getAlarm(alarmId));

    }

    @PatchMapping(value = "/{alarm_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "alarm action")
    @LNJoyAuditLog(value = "alarm action",
            resourceMapperId = OmcResources.ALARM,
            actionDescriptionTemplateClass = AlarmActionDescriptionTemplate.class,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "alarmId"),
            actionDescriptionField = "action",
            actionDescriptionFormatString = "alarm action: %s")
    public ResponseEntity<String> alarmAction(@ApiParam(required = true, name = "alarm_id") @PathVariable String alarmId,
                                                    @ApiParam(required = true, name = "action") @RequestParam(required = true) @Valid @Pattern(regexp = "(?i)active|(?i)" +
                                                            "deactive") String action)
    {
        log.info(" alarm{} action: {}, userId: {}", alarmId, action, getUserId());
        alarmService.action(alarmId, action);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }


    @GetMapping(value = "/alert-logs", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get alert logs")
    public ResponseEntity<Object> getAlertLogs(@ApiParam(name = "status") @RequestParam(value = "status", required = false) Integer status,
                                               @ApiParam(name = "alert_status") @RequestParam(value = "alert_status", required = false) Integer alertStatus,
                                               @ApiParam(name = "process_status") @RequestParam(value = "process_status", required = false) String processStatus,
                                               @ApiParam(name = "level") @RequestParam(value = "level", required = false) Integer level,
                                               @ApiParam(name = "group_id") @RequestParam(value = "group_id", required = false) String groupId,
                                               @ApiParam(name = "resource_type_id") @RequestParam(value = "resource_type_id", required = false) String resourceTypeId,
                                               @ApiParam(name = "resource_id") @RequestParam(value = "resource_id", required = false) String resourceId,
                                               @ApiParam(name = "page_size") @RequestParam(value = "page_size", required = false, defaultValue = Integer.MAX_VALUE + "") Integer pageSize,
                                               @ApiParam(name = "page_num") @RequestParam(value = "page_num", required = false, defaultValue = "1") Integer pageNum)
    {

        String queryBpId = "";
        if (!isAdmin())
        {
            queryBpId = getUserAttributes().getLeft();
        }

        AlertLogsRsp alertLogsRsp = alarmService.getAlertLogsList(queryBpId, queryUserId(), status, alertStatus, processStatus, level, groupId, resourceTypeId, resourceId, pageNum,
                pageSize);
        return status(OK).body(alertLogsRsp);

    }

    @GetMapping(value = "/alert-logs/counts", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get alert log counts")
    public ResponseEntity<Object> getAlertLogCounts(@ApiParam(name = "start_date") @RequestParam(value = "startDate", required = false) String startDate,
                                                 @ApiParam(name = "end_date") @RequestParam(value = "endDate", required = false) String endDate)
    {


        Pair<Date, Date> datePair = convertDate(startDate, endDate);
        AlertLogCountsRsp alertLogCountsRsp = alarmService.getAlertLogCounts(datePair.getLeft(), datePair.getRight());
        return status(OK).body(alertLogCountsRsp);

    }

    @GetMapping(value = "/alert-logs/daily/trend", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get alert log counts")
    public ResponseEntity<Object> getAlertLogDailyTrend(@ApiParam(name = "start_date") @RequestParam(value = "startDate", required = false) String startDate,
                                                    @ApiParam(name = "end_date") @RequestParam(value = "endDate", required = false) String endDate)
    {

        // the last 7 days including today
        Pair<Date, Date> datePair = getDate(startDate, endDate, -6, 1);
        DailyAlertLogTrendRsp dailyAlertLogTrendRsp = alarmService.getAlertLogDailyTrend(datePair.getLeft(), datePair.getRight());
        return status(OK).body(dailyAlertLogTrendRsp);

    }

    @PostMapping(value = "/alert-logs/process/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "alert log action")
    @LNJoyAuditLog(value = "alert log action",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                            bindParameterName = "alertLogId"))
    public ResponseEntity<String> alertLogDeal(@ApiParam(required = true, name = "id") @PathVariable String alertLogId,
                                               @ApiParam(required = true, name = "req") @RequestBody @Valid AlertLogProcessReq req)
    {
        log.info(" alert log process req: {}, userId: {}", alertLogId, req, getUserId());
        alarmService.handleAlertLog(alertLogId, req);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    private Pair<Date, Date> convertDate(String startDateStr, String endDateStr)
    {
        Date startDate = null, endDate = null;
        if (StringUtils.isNotBlank(startDateStr))
        {
            startDate = DateUtils.stringToDate(startDateStr);
        }

        if (StringUtils.isNotBlank(endDateStr))
        {
            endDate = DateUtils.stringToDate(endDateStr);
        }

        return new Pair<>(startDate, endDate);
    }
}
