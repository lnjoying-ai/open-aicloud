package com.lnjoying.justice.iam.domain.dto.response.project;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.iam.db.model.TblIamProject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/9 20:05
 */

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TblIamProjectDetail extends TblIamProject
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

    @ApiModelProperty(value="")
    private String parentName;


    @ApiModelProperty(value="")
    private String parentDisplayName;
}
