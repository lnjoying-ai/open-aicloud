package com.lnjoying.justice.schema.entity.stat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class GetClusterNumMetricsRsp
{
    @SerializedName("k8s_num")
    @JsonProperty("k8s_num")
    private int k8sNum;

    @SerializedName("k3s_num")
    @JsonProperty("k3s_num")
    private int k3sNum;
}
