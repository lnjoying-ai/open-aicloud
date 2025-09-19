package com.lnjoying.justice.schema.entity.servicemanager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceAgent
{
    private String agentId;
    private long timestamp;
}
