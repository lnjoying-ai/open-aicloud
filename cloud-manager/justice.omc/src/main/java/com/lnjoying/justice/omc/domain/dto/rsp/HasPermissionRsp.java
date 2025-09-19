package com.lnjoying.justice.omc.domain.dto.rsp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/24 20:53
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HasPermissionRsp
{
    private boolean hasPermission;
}
