package com.lnjoying.justice.cluster.manager.domain.dto.rsp.cluster;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.cluster.manager.domain.dto.model.template.ClusterTemplateInfo;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/1/26 19:41
 */

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@ApiModel(value = "ClusterTemplatesRspDto")
public class ClusterTemplatesRspDto
{
    private long totalNum;

    private List<ClusterTemplateInfo> templates;
}
