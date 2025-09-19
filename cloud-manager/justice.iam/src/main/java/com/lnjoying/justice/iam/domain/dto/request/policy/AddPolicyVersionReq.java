package com.lnjoying.justice.iam.domain.dto.request.policy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.iam.db.model.TblIamPolicy;
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
@ApiModel(value = "AddPolicyVersionReq")
@JsonIgnoreProperties({"id", "bp_id", "user_id", "create_time", "update_time"})
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AddPolicyVersionReq extends BaseReq
{

    @ApiModelProperty(value="")
    private String description;

    /**
     * policy document
     */
    @NotBlank(message = "policy document must not blank")
    private String policyDocument;

}
