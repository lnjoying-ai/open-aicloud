package com.lnjoying.justice.iam.domain.dto.request.authz;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.iam.domain.dto.request.BaseReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/14 15:19
 */

@Data
@ApiModel(value = "AttachUserReq")
@JsonIgnoreProperties({"id", "principal_id", "principal_type"})
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AttachUserReq extends AttachReq
{

    @NotBlank(message = "user id can not be empty")
    @ApiModelProperty(value = "")
    @JsonProperty("user_id")
    private String queryUserId;

    //@NotBlank(message = "project id can not be empty")
    @ApiModelProperty(value = "")
    private String projectId;
}
