package com.lnjoying.justice.iam.domain.dto.request.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.iam.db.model.TblIamService;
import com.lnjoying.justice.iam.domain.dto.request.BaseReq;
import com.lnjoying.justice.iam.validation.Enum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static com.lnjoying.justice.iam.common.constant.ConstraintConstants.MESSAGE_NAME;
import static com.lnjoying.justice.iam.common.constant.ConstraintConstants.PATTERN_NAME;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/9 15:35
 */

@Data
@ApiModel(value = "UpdateServiceReq")
@JsonIgnoreProperties({"id", "bp_id", "user_id", "create_time", "update_time"})
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateServiceReq extends BaseReq
{

    @NotBlank(message = "service name must not blank")
    @Pattern(regexp = PATTERN_NAME, message = MESSAGE_NAME)
    @ApiModelProperty(value="")
    private String name;

    @ApiModelProperty(value="")
    private String displayName;

    @ApiModelProperty(value="")
    private String iamCode;

    @ApiModelProperty(value="")
    private String parentId;

    @ApiModelProperty(value="")
    private String arnFormat;

    @ApiModelProperty(value="")
    private String arnRegex;

    /**
     * status(1:normal;-1:delete)
     */
    @ApiModelProperty(value="status(1:normal;-1:delete)")
    private Integer status;

    /**
     * enable(1:enable;-1:disable)
     */
    @Enum(clazz = TblIamService.ServiceEnableStatus.class, message = "enable(1:enable;-1:disable)")
    @ApiModelProperty(value="enable(1:enable;-1:disable)")
    private Integer enable;

    @ApiModelProperty(value="")
    private String description;

}
