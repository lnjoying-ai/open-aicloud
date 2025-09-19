package com.lnjoying.justice.iam.domain.dto.request.bp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class BpUniqueReq
{

    @ApiModelProperty(required = true, example = "")
    private String name;
}
