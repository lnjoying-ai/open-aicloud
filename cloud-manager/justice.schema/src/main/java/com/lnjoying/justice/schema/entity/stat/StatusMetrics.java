package com.lnjoying.justice.schema.entity.stat;

import lombok.Data;

@Data
public class StatusMetrics
{
    private int status;
    private int count;
    private String statusStr;
}
