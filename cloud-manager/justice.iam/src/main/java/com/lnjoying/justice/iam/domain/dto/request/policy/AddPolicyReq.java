package com.lnjoying.justice.iam.domain.dto.request.policy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.iam.db.model.TblIamPolicy;
import com.lnjoying.justice.iam.db.model.TblIamProject;
import com.lnjoying.justice.iam.domain.dto.request.BaseReq;
import com.lnjoying.justice.iam.validation.Enum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;

import static com.lnjoying.justice.iam.common.constant.ConstraintConstants.*;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/9 15:35
 */

@Data
@ApiModel(value = "AddPolicyReq")
@JsonIgnoreProperties({"id", "bp_id", "user_id", "bp_name", "user_name", "create_time", "update_time"})
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AddPolicyReq extends BaseReq
{

    /**
     * policy name
     */
    //@NotBlank(message = "policy name must not blank")
    //@Pattern(regexp = PATTERN_NAME, message = MESSAGE_NAME)
    @ApiModelProperty(value="policy name")
    private String policyName;

    @ApiModelProperty(value="")
    private String displayName;

    @ApiModelProperty(value="")
    private String description;

    /**
     * policy document
     */
    @NotBlank(message = "policy document must not blank")
    private String policyDocument;

    @ApiModelProperty(value = "policy type(1:system;2:custom;)")
    @Enum(clazz= TblIamPolicy.PolicyType.class, message = "policy type(1:system;2:custom;)")
    private Integer policyType = 2;

}
