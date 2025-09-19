package com.lnjoying.justice.cmp.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class ActionCloudReq
{
    @SerializedName("action")
    @JsonProperty("action")
    private String action;
}
