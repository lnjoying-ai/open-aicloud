package com.lnjoying.justice.schema.entity.iam;

import com.lnjoying.justice.schema.common.UserMetaDTO;
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
@ApiModel(value = "UserMetasRsp")
@NoArgsConstructor
@AllArgsConstructor
public class UserMetasRsp
{
    private long totalNum;

    private List<UserMetaDTO> userMetas;
}
