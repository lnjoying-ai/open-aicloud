package com.lnjoying.justice.omc.domain.dto.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2023年11月02日 14:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OperationEventDto
{
    private String id;

    private String name;

    private String bpId;

    private String service;

//    private String serviceCnName;

    private String resource;

//    private String resourceCnName;

    private String action;

    private String resourceId;
    private String resourceInstName;

    private String type;

//    private String typeCnName;

    private String level;

    private String requestPath;

    private String description;

    private String result;

    private String userId;

    private String operator;

    private Date triggerTime;

    private String friendlyDescription;
}
