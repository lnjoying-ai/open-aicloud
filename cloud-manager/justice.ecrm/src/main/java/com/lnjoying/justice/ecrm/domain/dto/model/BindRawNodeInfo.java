package com.lnjoying.justice.ecrm.domain.dto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class BindRawNodeInfo
{
    @SerializedName("uuid")
    @JsonProperty("uuid")
    private String uuid;

    @SerializedName("node_name")
    @JsonProperty("node_name")
    private String nodeName;

    @SerializedName("region_id")
    @JsonProperty("region_id")
    private String regionId;

    @SerializedName("site_id")
    @JsonProperty("site_id")
    private String siteId;
}
