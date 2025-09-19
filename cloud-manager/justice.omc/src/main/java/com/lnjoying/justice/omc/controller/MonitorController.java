package com.lnjoying.justice.omc.controller;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.omc.domain.dto.req.AddIntegrationTaskReq;
import com.lnjoying.justice.omc.domain.dto.req.PatchPrometheusReq;
import com.lnjoying.justice.omc.domain.dto.rsp.DashboardLinksRsp;
import com.lnjoying.justice.omc.domain.dto.rsp.IntegrationTargetsRsp;
import com.lnjoying.justice.omc.domain.dto.rsp.IntegrationsRsp;
import com.lnjoying.justice.omc.domain.dto.rsp.PrometheusInstancesRsp;
import com.lnjoying.justice.omc.domain.model.PrometheusInstanceDetail;
import com.lnjoying.justice.omc.service.GrafanaService;
import com.lnjoying.justice.omc.service.IntegrationTaskService;
import com.lnjoying.justice.schema.constant.ResourceOperationTypeConstants;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
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
import javax.validation.constraints.Pattern;

import java.util.List;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/10/25 15:10
 */

@RestSchema(schemaId = "MonitorController")
@RequestMapping(path = "/omc/v1/monitor")
@Slf4j
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "monitor"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "monitors"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "监控"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "tbl_omc_monitor_instance")})})
public class MonitorController extends OmcWebController
{

    @Autowired
    private IntegrationTaskService integrationTaskService;

    @Autowired
    private GrafanaService grafanaService;

    @PostMapping(value = "/integrations", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "add integration task")
    @LNJoyAuditLog(value = "add integration task",
            tags = ResourceOperationTypeConstants.RESOURCE_CREATE,
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                    resourceIdParseSPEL = "#obj?.body?.toString()"))
    public ResponseEntity<Object> addIntegrationTask(@ApiParam(required = true, name = "integrationTaskReq") @RequestBody @Valid AddIntegrationTaskReq addIntegrationTaskReq)
    {
        log.info("add integration task. req: {}, userId: {}", addIntegrationTaskReq, getUserId());


        setBaseReq(addIntegrationTaskReq, getUserId(), getBpId(), getUserName(), getBpName());

        return status(CREATED).body(integrationTaskService.addIntegrationTask(addIntegrationTaskReq));
    }


    @GetMapping(value = "/integrations/params", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get integration params")
    public ResponseEntity<Object> getIntegrationParams(@ApiParam(name = "integration_type") @RequestParam(value = "integrationType", required = false) Integer integrationType)
    {
        log.info("get integration params. req: {}, userId: {}", integrationType, getUserId());


        return status(OK).body(integrationTaskService.getIntegrationParams(integrationType));
    }

    @GetMapping(value = "/integrations", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get integration list")
    public ResponseEntity<Object> getIntegrationList(@ApiParam(name = "integration_type") @RequestParam(value = "integrationType", required = false) Integer integrationType,
                                                 @ApiParam(name = "page_size") @RequestParam(value = "page_size", required = false, defaultValue = Integer.MAX_VALUE + "") Integer pageSize,
                                                 @ApiParam(name = "page_num") @RequestParam(value = "page_num", required = false, defaultValue = "1") Integer pageNum)
    {
        String queryBpId = "";
        if (!isAdmin())
        {
            queryBpId = getUserAttributes().getLeft();
        }
        IntegrationsRsp integrationsRsp = integrationTaskService.getIntegrationList(integrationType, queryBpId, queryUserId(), pageNum, pageSize);
        return status(OK).body(integrationsRsp);

    }

    @Deprecated
    @GetMapping(value = "/integrations/targets", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get integration target list")
    public ResponseEntity<Object> getIntegrationTargetList(@ApiParam(name = "target_type") @RequestParam(value = "target_type", required = false) Integer targetType,
                                                         @ApiParam(name = "site") @RequestParam(value = "site", required = false) String site,
                                                         @ApiParam(name = "region") @RequestParam(value = "region", required = false) String region,
                                                         @ApiParam(name = "name") @RequestParam(value = "name", required = false) String nodeName,
                                                         @ApiParam(name = "page_size") @RequestParam(value = "page_size", required = false,
                                                                 defaultValue = Integer.MAX_VALUE + "") Integer pageSize,
                                                         @ApiParam(name = "page_num") @RequestParam(value = "page_num", required = false, defaultValue = "1") Integer pageNum)
    {
        String queryBpId = "";
        if (!isAdmin())
        {
            queryBpId = getUserAttributes().getLeft();
        }
        IntegrationTargetsRsp targetsRsp =  integrationTaskService.getIntegrationTargetList(targetType, site, region, nodeName, queryBpId, queryUserId(), pageNum, pageSize);
        return status(OK).body(targetsRsp);

    }

    @GetMapping(value = "/integrations/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get integration")
    @LNJoyAuditLog(value = "get integration",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "id"),
            actionDescriptionField = "get", actionDescriptionFormatString = "get integration: %s")
    public ResponseEntity<Object> getIntegration(@ApiParam(required = true, name = "id") @PathVariable String id)
    {
        String bpId = getBpId();
        String userId = getUserId();

        return ResponseEntity.status(HttpStatus.OK).body( integrationTaskService.getIntegration(id, bpId, userId));
    }

    @PatchMapping(value = "/integrations/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "integration action")
    @LNJoyAuditLog(value = "integration action",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "id"),
            actionDescriptionField = "action", actionDescriptionFormatString = "integration action: %s")
    public ResponseEntity<String> integrationAction(@ApiParam(required = true, name = "id") @PathVariable String id,
                                                    @ApiParam(required = true, name = "action") @RequestParam(required = true) @Valid @Pattern(regexp = "(?i)reinstall|(?i)uninstall") String action)
    {
        String bpId = getBpId();
        String userId = getUserId();
        integrationTaskService.action(id, userId, bpId, action);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(value = "/prometheus", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "prometheus instance")
    public ResponseEntity<Object> getPrometheusInstanceList(@ApiParam(name = "type") @RequestParam(value = "type", required = false) Integer type,
                                                            @ApiParam(name = "name") @RequestParam(value = "name", required = false) String name,
                                                            @ApiParam(name = "site_id") @RequestParam(value = "site_id", required = false) String siteId,
                                                     @ApiParam(name = "page_size") @RequestParam(value = "page_size", required = false, defaultValue = Integer.MAX_VALUE + "") Integer pageSize,
                                                     @ApiParam(name = "page_num") @RequestParam(value = "page_num", required = false, defaultValue = "1") Integer pageNum)
    {

        PrometheusInstancesRsp prometheusInstancesRsp = integrationTaskService.getPrometheusInstanceList(type, name, siteId, pageNum, pageSize);
        return status(OK).body(prometheusInstancesRsp);

    }

    @GetMapping(value = "/prometheus/{prometheus_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get prometheus")
    public ResponseEntity<Object> getPrometheus(@ApiParam(required = true, name = "prometheus_id") @PathVariable String prometheusId)
    {

        PrometheusInstanceDetail prometheus = integrationTaskService.getPrometheus(prometheusId);
        return ResponseEntity.status(HttpStatus.OK).body(prometheus);
    }

    @PatchMapping(value = "/prometheus/{prometheus_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get prometheus")
    public ResponseEntity<Object> patchPrometheus(@ApiParam(required = true, name = "prometheus_id") @PathVariable String prometheusId,
                                                  @ApiParam(value = "", required = true, name = "configEdgeReq") @RequestBody PatchPrometheusReq req)
    {

        integrationTaskService.patchPrometheus(prometheusId, req);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(value = "/prometheus/{prometheus_id}/data/query", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get prometheus data query")
    public ResponseEntity<Object> getPrometheusDataQuery(@ApiParam(required = true, name = "prometheus_id") @PathVariable String prometheusId,
                                                         @ApiParam(name = "query") @RequestParam(value = "query", required = false) String query,
                                                         @ApiParam(name = "time") @RequestParam(value = "time", required = false) String time,
                                                         @ApiParam(name = "timeout") @RequestParam(value = "timeout", required = false) Integer timeout)
    {

        String data = integrationTaskService.getPrometheusDataQuery(query, time, timeout);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    @GetMapping(value = "/prometheus/{prometheus_id}/jobs", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get prometheus")
    public ResponseEntity<Object> getPrometheusJobs(@ApiParam(required = true, name = "prometheus_id") @PathVariable String prometheusId)
    {

        Object prometheusJobs = integrationTaskService.getPrometheusJobs(prometheusId);
        return ResponseEntity.status(HttpStatus.OK).body(prometheusJobs);
    }


    @GetMapping(value = "/prometheus/{prometheus_id}/data/query_range", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get prometheus data query_range")
    public ResponseEntity<Object> getPrometheusDataQueryRange(@ApiParam(required = true, name = "prometheus_id") @PathVariable String prometheusId,
                                                         @ApiParam(name = "query") @RequestParam(value = "query", required = false) String query,
                                                         @ApiParam(name = "step") @RequestParam(value = "step", required = false) String step,
                                                         @ApiParam(name = "startTime") @RequestParam(value = "startTime", required = false) String startTime,
                                                         @ApiParam(name = "endTime") @RequestParam(value = "endTime", required = false) String endTime,
                                                         @ApiParam(name = "timeout") @RequestParam(value = "timeout", required = false) Integer timeout)
    {

        String data = integrationTaskService.getPrometheusDataQueryRange(query, step, startTime, endTime, timeout);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }


    @GetMapping(value = "/link", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "dashboard link")
    public ResponseEntity<Object> getDashboardLinkList(@ApiParam(name = "type") @RequestParam(required = false, value = "type") List<String> type,
                                                            @ApiParam(name = "page_size") @RequestParam(value = "page_size", required = false, defaultValue = Integer.MAX_VALUE + "") Integer pageSize,
                                                            @ApiParam(name = "page_num") @RequestParam(value = "page_num", required = false, defaultValue = "1") Integer pageNum)
    {

        DashboardLinksRsp dashboardLinksRsp = grafanaService.getDashboardLinkList(type, pageNum, pageSize);
        return status(OK).body(dashboardLinksRsp);

    }
}
