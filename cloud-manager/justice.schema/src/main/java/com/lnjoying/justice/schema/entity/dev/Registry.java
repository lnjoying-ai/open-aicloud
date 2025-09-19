package com.lnjoying.justice.schema.entity.dev;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class Registry implements Serializable
{
    @ApiModelProperty(example = "registryAddr")
    @SerializedName("server")
    @JsonProperty("server")
    String                         server;
    
    @ApiModelProperty(example = "login-name")
    @SerializedName("username")
    @JsonProperty("username")
    String                        username;
    
    @ApiModelProperty(example = "login-password")
    @SerializedName("password")
    @JsonProperty("password")
    String                        password;
}
