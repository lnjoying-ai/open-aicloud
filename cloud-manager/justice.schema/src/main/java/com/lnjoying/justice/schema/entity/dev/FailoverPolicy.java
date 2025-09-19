package com.lnjoying.justice.schema.entity.dev;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FailoverPolicy implements Serializable
{
    @SerializedName("need_failover")
    @JsonProperty("need_failover")
    private Boolean needFailover;

    @SerializedName("delays")
    @JsonProperty("delays")
    private long delays;

    @SerializedName("failover_range")
    @JsonProperty("failover_range")
    private String failoverRange;
}
