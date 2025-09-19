package com.lnjoying.justice.iam.domain.dto.response.role;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.iam.domain.model.IamPolicy;
import com.lnjoying.justice.iam.domain.model.IamRole;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/9 19:25
 */

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@ApiModel(value = "RolesRsp")
public class RoleRsp
{
    private long totalNum;

    private List<IamRole> roles;
}
