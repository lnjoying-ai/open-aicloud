package com.lnjoying.justice.iam.domain.dto.request.wx;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/10/19 14:53
 */

@Data
public class CodeReq
{
    /**
     *code when wx login
     */
    @NotBlank(message = "code must not blank")
    String code;
}
