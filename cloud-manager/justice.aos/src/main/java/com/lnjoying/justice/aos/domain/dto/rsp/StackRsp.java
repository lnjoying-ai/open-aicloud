package com.lnjoying.justice.aos.domain.dto.rsp;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.aos.domain.model.helm.ChartInfo;
import com.lnjoying.justice.aos.domain.model.helm.HelmStackInfo;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/8/10 9:52
 */

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@ApiModel(value = "StackRsp")
public class StackRsp
{
    private long totalNum;

    private List<HelmStackInfo> apps;
}
