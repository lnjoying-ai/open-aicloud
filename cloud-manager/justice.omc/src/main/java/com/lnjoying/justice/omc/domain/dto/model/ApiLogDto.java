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
 * @date: 2023年11月01日 15:31
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ApiLogDto
{
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

    private Date triggerTime;

    private String operationType;
}
