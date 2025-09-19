package com.lnjoying.justice.iam.domain.dto.request.user;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "userRegisterRequest")
public class UserExBasicReq extends UserBasicReq
{
    /**
     * 0: Access is forbidden 1: Access is allowed
     */
    private int is_allowed = 0;
    private int status = 0;

    /**
     * 0:System 1:admin 2:bp 3:personal
     */
    private Integer kind;
    private int level;
}
