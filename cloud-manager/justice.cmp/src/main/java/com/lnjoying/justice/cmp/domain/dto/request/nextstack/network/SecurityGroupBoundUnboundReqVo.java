package com.lnjoying.justice.cmp.domain.dto.request.nextstack.network;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class SecurityGroupBoundUnboundReqVo
{
    @NotEmpty
    List<String> vmInstances;
}
