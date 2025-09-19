package com.lnjoying.justice.cmp.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Password
{
    @SerializedName("userid")
    @JsonProperty("userid")
    private String userid;

    @SerializedName("username")
    @JsonProperty("username")
    private String username;

    @SerializedName("password")
    @JsonProperty("password")
    private String password;
}
