package com.lnjoying.justice.schema.entity.docker;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Regulus
 * @Date: 11/16/21 3:11 PM
 * @Description:
 */
@Data
public class HealthCheck implements Serializable
{
    private static final long serialVersionUID = 1L;
    @SerializedName("Interval")
    private Long interval;
    @SerializedName("Timeout")
    private Long timeout;
    @SerializedName("Test")
    private List<String> test;
    @SerializedName("Retries")
    private Integer retries;
    @SerializedName("StartPeriod")
    private Long startPeriod;
}
