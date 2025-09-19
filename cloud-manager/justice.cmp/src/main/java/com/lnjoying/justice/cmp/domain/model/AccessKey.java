package com.lnjoying.justice.cmp.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class AccessKey
{
    @SerializedName("id")
    @JsonProperty("id")
    private String id;

    @SerializedName("secret")
    @JsonProperty("secret")
    private String secret;
}
