package com.lnjoying.justice.schema.entity.stat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class GetCloudNumMetricsRsp
{
    @SerializedName("vm_num")
    @JsonProperty("vm_num")
    private int vmNum;

    @SerializedName("baremetal_num")
    @JsonProperty("baremetal_num")
    private int baremetalNum;
}
