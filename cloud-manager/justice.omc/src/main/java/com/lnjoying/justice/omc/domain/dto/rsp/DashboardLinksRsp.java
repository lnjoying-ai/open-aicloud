package com.lnjoying.justice.omc.domain.dto.rsp;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.omc.domain.model.DashboardLink;
import com.lnjoying.justice.omc.domain.model.PrometheusInstance;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/10/30 16:58
 */

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@ApiModel(value = "DashboardLinksRsp")
public class DashboardLinksRsp
{

    private long totalNum;

    private List<DashboardLink> links;
}
