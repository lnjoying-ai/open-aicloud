package com.lnjoying.justice.iam.domain.dto.request.authz;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/14 15:19
 */

@Data
@ApiModel(value = "DetachRoleReq")
@JsonIgnoreProperties({"id", "principal_id", "principal_type"})
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DetachRoleReq extends DetachReq
{

    @NotBlank(message = "role id can not be empty")
    @ApiModelProperty(value = "")
    @JsonProperty(value = "role_id")
    private String roleId;

}
