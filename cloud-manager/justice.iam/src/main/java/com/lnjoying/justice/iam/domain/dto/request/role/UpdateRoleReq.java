package com.lnjoying.justice.iam.domain.dto.request.role;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.iam.db.model.TblIamRole;
import com.lnjoying.justice.iam.domain.dto.request.BaseReq;
import com.lnjoying.justice.iam.validation.Enum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;

import static com.lnjoying.justice.iam.common.constant.ConstraintConstants.MESSAGE_NAME;
import static com.lnjoying.justice.iam.common.constant.ConstraintConstants.PATTERN_NAME;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/9 15:35
 */

@Data
@ApiModel(value = "UpdateRoleReq")
@JsonIgnoreProperties({"id", "bp_id", "user_id", "create_time", "update_time"})
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateRoleReq extends BaseReq
{

    @ApiModelProperty(value="")
    private Long roleId;

    @ApiModelProperty(value="")
    @JsonProperty("iam_code")
    private String platform;

    @ApiModelProperty(value="")
    @Pattern(regexp = PATTERN_NAME, message = MESSAGE_NAME)
    @JsonProperty("name")
    private String role;

    @ApiModelProperty(value="")
    private String projectId;

    @ApiModelProperty(value="")
    private String description;

    /**
     * role type(1:system;2:custom;)
     */
    @Enum(clazz= TblIamRole.RoleType.class, message = "role type(1:system;2:custom;)")
    @ApiModelProperty(value="role type(1:system;2:custom;)")
    private Integer roleType;

    @ApiModelProperty(value="")
    private String bpId;

    @ApiModelProperty(value="")
    private String userId;


}
