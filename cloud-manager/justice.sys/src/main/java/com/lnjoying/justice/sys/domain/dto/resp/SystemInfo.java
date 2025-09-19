package com.lnjoying.justice.sys.domain.dto.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class SystemInfo
{
    @SerializedName("register_mode")
    @JsonProperty("register_mode")
    private String registerMode;
}
