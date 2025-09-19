package com.lnjoying.justice.omc.controller;

import com.lnjoying.justice.commonweb.controller.RestWebController;
import com.lnjoying.justice.omc.config.DescriptionConfig;
import com.lnjoying.justice.omc.domain.dto.req.GetNodeStatusMetricsReq;
import com.lnjoying.justice.omc.domain.dto.rsp.GetNodeResourceSortMetricsRsp;
import com.lnjoying.justice.omc.domain.dto.rsp.IntegrationsRsp;
import com.lnjoying.justice.omc.domain.dto.rsp.StatsCountsRsp;
import com.lnjoying.justice.omc.domain.model.DailyStatusCount;
import com.lnjoying.justice.omc.service.StatsService;
import com.lnjoying.justice.omc.util.DateUtils;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import com.lnjoying.justice.schema.entity.stat.GetClusterNumMetricsRsp;
import com.lnjoying.justice.schema.entity.stat.GetStatusMetricsRsp;
import com.lnjoying.justice.schema.entity.stat.NodesStatusMetrics;
import com.micro.core.common.Pair;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Extension;
import io.swagger.annotations.ExtensionProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.*;
import static com.lnjoying.justice.omc.util.DateUtils.getDate;
import static com.lnjoying.justice.omc.util.DateUtils.getStartOfDayWithOffset;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/1 10:54
 */

@RestSchema(schemaId = "StatsController")
@RequestMapping(path = "/omc/v1/stats")
@Slf4j
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "stat"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "stats"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "状态统计"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "")})})
public class StatsController  extends RestWebController
{

    @Autowired
    private StatsService statsService;


    @GetMapping(value = "/counts", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get stats counts")
    public ResponseEntity<Object> getStatsCounts(@ApiParam(name = "start_date") @RequestParam(value = "startDate", required = false) String startDate,
                                                 @ApiParam(name = "end_date") @RequestParam(value = "endDate", required = false) String endDate)
    {

        Pair<Date, Date> datePair = getDate(startDate, endDate, 0, -1);
        StatsCountsRsp statsCounts = statsService.getStatsCounts(datePair.getLeft(), datePair.getRight());
        return status(OK).body(statsCounts);

    }

    @GetMapping(value = "/daily/status-counts", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get stats counts")
    public ResponseEntity<Object> getDailyStatusCounts(@ApiParam(name = "start_date") @RequestParam(value = "startDate", required = false) String startDate,
                                                       @ApiParam(name = "end_date") @RequestParam(value = "endDate", required = false) String endDate,
                                                       @ApiParam(name = "resource_type") @RequestParam(value = "resourceType", required = false) int resourceType)
    {

        Pair<Date, Date> datePair = getDate(startDate, endDate, -6, 1);
        List<DailyStatusCount> dailyStatusCounts = statsService.getDailyStatsCounts(datePair.getLeft(), datePair.getRight(), resourceType);
        return status(OK).body(dailyStatusCounts);

    }


    @GetMapping(value = "/status/metrics", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get status metrics",notes = DescriptionConfig.STACK_METRICS_MSG)
    public ResponseEntity<Object> getStatusMetrics(@ApiParam(value = "filter", required = false, name = "filter",  defaultValue = "cloud") String filter,
                                                                @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                                @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                                @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        try
        {
            String operUser = userId;
            String operBp = bpId;

            if (isAdmin())
            {
                operUser = null;
                operBp = null;
            }
            else if (isBpUser())
            {
                operUser = null;
            }
            return ResponseEntity.ok().body(statsService.getStatusMetrics(filter, operUser, operBp));

        }
        catch (Exception e)
        {
            log.error("query status metrics info error. Error:{}", e.getMessage());
            throw throwWebException(e);
        }
    }


    @GetMapping(value = "/clusters/metrics", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get clusters metrics",notes = DescriptionConfig.STACK_METRICS_MSG)
    public ResponseEntity<GetClusterNumMetricsRsp> getClusterNumMetrics(@RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                                        @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                                        @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        try
        {

            return ResponseEntity.ok().body(statsService.getClusterNumMetrics(userId, bpId));
        }
        catch (Exception e)
        {
            log.error("query clusters metrics info error. Error:{}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @PostMapping(value = "/nodes/status/metrics", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get node status metrics",notes = DescriptionConfig.NODE_STATUS_METRICS_MSG)
    public ResponseEntity<NodesStatusMetrics> getNodeStatusMetrics(@ApiParam(value = "get node status metrics req", required = true, name = "get node status metrics req") @RequestBody GetNodeStatusMetricsReq req,
                                                                   @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                                   @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                                   @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        try
        {
            String ownerUser = null;
            String ownerBp = null;
            if (isBpAdmin())
            {
                ownerBp = bpId;
            }
            else if (isBpUser())
            {
                ownerBp = bpId;
                ownerUser = userId;
            }

            return ResponseEntity.ok().body(statsService.getNodeStatusMetrics(req.getRegionId(), req.getSiteId(), req.getFilter(), ownerBp, ownerUser));
        }
        catch (Exception e)
        {
            log.error("query stack stat info error. Error:{}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/nodes/metrics/sort", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get node resource sort metrics",notes = DescriptionConfig.STACK_METRICS_MSG)
    public ResponseEntity<GetNodeResourceSortMetricsRsp> getNodeResourceSortMetrics(@ApiParam(value = "condition", required = false, name = "condition", defaultValue = "cpu") String condition,
                                                                                    @ApiParam(value = "sort", required = false, name = "sort", defaultValue = "desc") String sort,
                                                                                    @ApiParam(value = "limit", required = false, name = "limit", defaultValue = "5") int limit,
                                                                                    @ApiParam(value = "step", required = false, name = "step", defaultValue = "1m") String timeRange,
                                                                                    @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId,
                                                                                    @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                                                    @RequestHeader(name = UserHeadInfo.AUTIORITIES, required = false) String authorities)
    {
        try
        {

            return ResponseEntity.ok().body(statsService.getNodeResourceSortMetrics(condition, sort, limit, userId, bpId, timeRange));
        }
        catch (Exception e)
        {
            log.error("query node resource sort metrics info error. Error:{}", e.getMessage());
            throw throwWebException(e);
        }
    }
}
