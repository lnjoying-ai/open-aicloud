package com.lnjoying.justice.aos.domain.dto.rsp;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.aos.domain.model.helm.RepoInfo;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/8/5 21:05
 */

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@ApiModel(value = "RepoRsp")
public class RepoRsp
{
    private long totalNum;

    private List<RepoInfo> repos;
}
