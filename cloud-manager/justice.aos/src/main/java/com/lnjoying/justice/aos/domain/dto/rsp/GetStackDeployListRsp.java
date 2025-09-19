package com.lnjoying.justice.aos.domain.dto.rsp;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.aos.domain.model.StackDeployInfo;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.util.List;
/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/1/13 21:10
 */

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@ApiModel(value = "GetStackDeployListRsp")
public class GetStackDeployListRsp
{
	long totalNum;

	List<StackDeployInfo> deployments;
}
