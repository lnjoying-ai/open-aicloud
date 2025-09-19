package com.lnjoying.justice.schema.entity.omc;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2024/8/23 16:32
 */

@Data
@Builder
@ApiModel(value = "BpLoginDetailRsp")
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BpLoginDetailRsp
{

    private long totalNum;

    private List<BpLoginDetailDTO> userDatas;
}
