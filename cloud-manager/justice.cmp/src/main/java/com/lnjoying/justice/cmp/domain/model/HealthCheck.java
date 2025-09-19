package com.lnjoying.justice.cmp.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class HealthCheck
{
    @SerializedName("health_url")
    @JsonProperty("health_url")
    private String healthUrl;

    @SerializedName("response")
    @JsonProperty("response")
    private String response;

    @SerializedName("interval")
    @JsonProperty("interval")
    private Long interval = 60000L;
}
