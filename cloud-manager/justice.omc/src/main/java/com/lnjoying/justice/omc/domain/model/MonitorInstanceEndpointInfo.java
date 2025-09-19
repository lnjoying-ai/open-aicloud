package com.lnjoying.justice.omc.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/21 14:48
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MonitorInstanceEndpointInfo
{
    private String name;

    private Integer originalPort;

    private String mappedEndpoint;
}
