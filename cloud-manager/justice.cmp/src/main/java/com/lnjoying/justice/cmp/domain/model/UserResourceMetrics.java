package com.lnjoying.justice.cmp.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class UserResourceMetrics
{
    @SerializedName("vm_num")
    @JsonProperty("vm_num")
    private Integer vmNum;
}
