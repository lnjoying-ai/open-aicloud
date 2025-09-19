package com.lnjoying.justice.cmp.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Authorization
{
    @SerializedName("access_key")
    @JsonProperty("access_key")
    private AccessKey accessKey;

    @SerializedName("password")
    @JsonProperty("password")
    private Password password;
}
