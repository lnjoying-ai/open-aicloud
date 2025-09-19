package com.lnjoying.justice.schema.service.omc;

import com.lnjoying.justice.schema.common.CombRetPacket;
import com.lnjoying.justice.schema.entity.omc.BpLastLoginTimeRsp;
import com.lnjoying.justice.schema.entity.omc.BpLoginDetailRsp;
import com.lnjoying.justice.schema.entity.omc.MonitorEndpointInfo;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OmcService
{
    void addLog(@ApiParam(name = "operationLog") OperationLog operationLog);
    void addEvent(@ApiParam(name = "operationEvent") OperationEvent operationEvent);

    CombRetPacket receivePortMappingResult(@ApiParam(name = "monitorId")String monitorId, @ApiParam(name = "endpoint")String endpoint, @ApiParam(name = "message")String message);

    CombRetPacket addMonitorEndpoint(@ApiParam(name = "monitorEndpointInfo") MonitorEndpointInfo monitorEndpointInfo);

    CombRetPacket deleteMonitorEndpoint(@ApiParam(name = "type") String type, @ApiParam(name = "uniqueId") String uniqueId);

    BpLastLoginTimeRsp getLastLoginTimeGroupByBpId(@ApiParam(name = "bpIds") List<String> bpIds, @ApiParam(name = "startDate")Date startDate,
                                                   @ApiParam(name = "endDate")Date endDate,
                                                   @ApiParam(name = "pageSize")Integer pageSize, @ApiParam(name = "pageNum")Integer pageNum,
                                                   @ApiParam(name = "sortField")String sortField, @ApiParam(name = "sortDirection")String sortDirection,
                                                   @ApiParam(name = "userId")String userId, @ApiParam(name = "userName")String userName);

    Map<String, Date> getSimpleLastLoginTimeGroupByBpId(@ApiParam(name = "bpIds")List<String> bpIds, @ApiParam(name = "startDate")Date startDate,
                                                               @ApiParam(name = "endDate")Date endDate,
                                                               @ApiParam(name = "pageSize")Integer pageSize, @ApiParam(name = "pageNum")Integer pageNum,
                                                               @ApiParam(name = "sortField")String sortField, @ApiParam(name = "sortDirection")String sortDirection);

    BpLoginDetailRsp getLoginDetailByBpId(@ApiParam(name = "bpId")String bpId, @ApiParam(name = "userId")String userId,
                                          @ApiParam(name = "startDate")Date startDate, @ApiParam(name = "endDate")Date endDate,
                                          @ApiParam(name = "pageSize")Integer pageSize, @ApiParam(name = "pageNum")Integer pageNum,
                                          @ApiParam(name = "sortField")String sortField, @ApiParam(name = "sortDirection")String sortDirection);


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    final class OperationLog implements Serializable
    {
        private static final long serialVersionUID = 1L;

        private String id;

        private String level;

        private String bpId;

        private String service;

        private String resource;

        private String resourceId;

        private String action;

        private String accessIp;

        private String userAgent;

        private String description;

        private String httpMethod;

        private String requestPath;

        private String inParams;

        private String outParams;

        private String result;

        private Integer executionTime;

        private String userId;

        private String operator;

        private String operationType;

        private Date triggerTime;

        private Date createdAt;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    final class OperationEvent implements Serializable
    {
        private static final long serialVersionUID = 1L;

        private String id;

        private String name;

        private String bpId;

        private String service;

        private String resource;

        private String action;

        private String resourceInstId;
        
        private String resourceInstName;

        private String type;

        private String level;

        private String requestPath;

        private String description;

        private String result;

        private String userId;

        private String operator;

        private Date triggerTime;

        private Date createdAt;

        private String friendlyDescription;
    }
}
