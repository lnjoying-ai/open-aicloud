package com.lnjoying.justice.iam.domain.dto.response.policy;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.commonweb.util.JacksonUtils;
import com.lnjoying.justice.iam.db.model.TblIamPolicy;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static com.lnjoying.justice.iam.db.model.TblIamPolicy.DocumentType.REGO;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/10 19:16
 */

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Slf4j
public class TblIamPolicyDetail extends TblIamPolicy
{
    /**
     * username
     */
    @ApiModelProperty(value = "user name")
    private String userName;

    /**
     * bp name
     */
    @ApiModelProperty(value = "bp name")
    private String bpName;

    @ApiModelProperty(value = "")
    private String document;

    private int versionTotal;

}
