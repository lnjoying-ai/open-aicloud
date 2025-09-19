package com.lnjoying.justice.ecrm.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class CheckUniqueReq
{
    @SerializedName("region_id")
    @JsonProperty("region_id")
    String regionId;

    @SerializedName("site_id")
    @JsonProperty("site_id")
    String siteId;
}
