package com.lnjoying.justice.servicemanager.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import javax.validation.constraints.Pattern;
import java.util.List;

@Data
public class UpdateServicePortReq
{
    @SerializedName("name")
    @JsonProperty("name")
    @Pattern(regexp = "^[\\u4E00-\\u9FA5A-Za-z0-9][\\u4E00-\\u9FA5A-Za-z0-9_@-]{0,63}$")
    private String name;

    @SerializedName("description")
    @JsonProperty("description")
    private String description;

    @SerializedName("tags")
    @JsonProperty("tags")
    private List<String> tags;

    @SerializedName("target_services")
    @JsonProperty("target_services")
    private List<AddTargetServiceReq> addTargetServiceReqs;
}
