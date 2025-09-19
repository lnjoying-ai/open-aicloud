package com.lnjoying.justice.schema.entity.omc;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.schema.common.UserMetaDTO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2024/8/23 16:32
 */

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BpLoginDetailDTO extends UserMetaDTO
{

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
