package com.lnjoying.justice.schema.entity.omc;

import com.lnjoying.justice.schema.entity.omc.BpLastLoginTimeDTO;
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
@ApiModel(value = "BpLastLoginTimeRsp")
@NoArgsConstructor
@AllArgsConstructor
public class BpLastLoginTimeRsp
{

    private long totalNum;

    private List<BpLastLoginTimeDTO> lastLoginTimeDTOS;
}
