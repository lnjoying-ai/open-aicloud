package com.lnjoying.justice.omc.controller;

import com.lnjoying.justice.commonweb.controller.RestWebController;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.validation.date.ValidDate;
import com.lnjoying.justice.omc.config.DescriptionConfig;
import com.lnjoying.justice.omc.domain.dto.response.QueryApiLogRsp;
import com.lnjoying.justice.omc.domain.model.HourlyOperationEventTrendRsp;
import com.lnjoying.justice.omc.domain.model.HourlyOperationLogTrendRsp;
import com.lnjoying.justice.omc.domain.model.search.ApiLogSearchCritical;
import com.lnjoying.justice.omc.facade.LogServiceFacade;
import com.lnjoying.justice.omc.util.DateUtils;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import com.micro.core.common.Pair;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static com.lnjoying.justice.omc.util.DateUtils.getHour;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;
import static org.springframework.util.StringUtils.hasText;
import static org.springframework.util.StringUtils.isEmpty;
import static org.apache.commons.lang3.EnumUtils.isValidEnum;

import org.springframework.web.bind.annotation.*;

import java.util.Date;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2023年10月31日 20:02
 */
@RestSchema(schemaId = "operationLogController")
@RequestMapping(path = "/omc/v1/apilogs")
@Api(value = "OperationLog Controller", tags = "OperationLog Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "apilog"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "apilogs"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "api日志"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblOperationLog")})})
public class ApiLogController extends RestWebController
{

    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private LogServiceFacade logServiceFacade;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get logs", notes = DescriptionConfig.GET_API_LOG_LIST)
    @LNJoyAuditLog(value = "get logs", logOff = true)
    public ResponseEntity<QueryApiLogRsp> getLogs(
            @ApiParam(name = "page_num") @RequestParam(value = "page_num", required = false) Integer pageNum,
            @ApiParam(name = "page_size") @RequestParam(value = "page_size", required = false) Integer pageSize,
            @ApiParam(name = "service") @RequestParam(value = "service", required = false) String serviceName,
            @ApiParam(name = "resource") @RequestParam(value = "resource", required = false) String resourceName,
            @ApiParam(name = "resource_id") @RequestParam(value = "resource_id", required = false) String resourceId,
            @ApiParam(name = "user_id") @RequestParam(value = "user_id", required = false) String userId,
            @ApiParam(name = "level") @RequestParam(value = "level", required = false) String logLevel,
            @ApiParam(name = "http_method") @RequestParam(value = "http_method", required = false) String httpMethod,
            @ApiParam(name = "start_date") @RequestParam(value = "start_date", required = false) @ValidDate String startDate,
            @ApiParam(name = "end_date") @RequestParam(value = "end_date", required = false) @ValidDate String endDate,
            @RequestHeader(name = UserHeadInfo.BPID, required = false) String loginUserBpId,
            @RequestHeader(name = UserHeadInfo.USERID, required = false) String loginUserId,
            @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String loginUserAuthorities)
    {
        try
        {
            if (hasText(startDate) && hasText(endDate) && DateUtils.stringToDate(endDate).before(DateUtils.stringToDate(startDate)))
            {
                throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.WARNING);
            }

            ApiLogSearchCritical searchCritical = ApiLogSearchCritical.builder()
                    .serviceName(isEmpty(serviceName) ? null : serviceName)
                    .resourceName(isEmpty(resourceName) ? null : resourceName)
                    .resourceId(isEmpty(resourceId) ? null : resourceId)
                    .bpId(loginUserBpId)
                    .userId(isEmpty(userId) ? null : userId)
                    .logLevel(isValidEnum(ErrorLevel.class, logLevel) ? logLevel : null)
                    .httpMethod(isEmpty(httpMethod) ? null : httpMethod)
                    .startTriggerTime(!hasText(startDate) ? null : DateUtils.stringToDate(startDate))
                    .endTriggerTime(!hasText(endDate) ? null : DateUtils.stringToDate(endDate))
                    .build();
            if (pageNum != null)
            {
                searchCritical.setPageNum(pageNum);
            }
            if (pageSize != null)
            {
                searchCritical.setPageSize(pageSize);
            }
            return ResponseEntity.ok().body(logServiceFacade.getLogs(searchCritical));
        } catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("get logs error. {}", e.getMessage());
            throw throwWebException(e);
        }
    }


    @GetMapping(value = "/hourly/trend", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get operation log counts")
    public ResponseEntity<Object> getOperationLogHourlyTrend(@ApiParam(name = "start_date") @RequestParam(value = "startDate", required = false) String startDate,
                                                               @ApiParam(name = "end_date") @RequestParam(value = "endDate", required = false) String endDate)
    {

        // the last 1 days including today
        Pair<Date, Date> datePair = getHour(startDate, endDate, -23, 1);
        HourlyOperationLogTrendRsp hourlyOperationLogTrendRsp = logServiceFacade.getOperationLogHourlyTrend(datePair.getLeft(), datePair.getRight());
        return status(OK).body(hourlyOperationLogTrendRsp);

    }

}
