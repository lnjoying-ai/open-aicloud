package com.lnjoying.justice.aos.domain.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/8/10 10:25
 */

@Data
@ApiModel(value = "ImportChartReq")
public class ImportChartReq implements Serializable
{

    private static final long serialVersionUID = -6756458158589303633L;

    @JsonProperty("package")
    private String packageContent;

}
