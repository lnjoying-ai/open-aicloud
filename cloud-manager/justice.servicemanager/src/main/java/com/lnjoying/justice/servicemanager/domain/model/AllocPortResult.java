package com.lnjoying.justice.servicemanager.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllocPortResult
{
    private String svcNode;
    private String portAllocationId;
    private String internalIp;
    private String externalIp;
    private Integer svcPort;
    private Integer status;
}
