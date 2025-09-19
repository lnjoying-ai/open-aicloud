package com.lnjoying.justice.ecrm.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckUniqueRsp
{
    @SerializedName("region_id")
    @JsonProperty("region_id")
    boolean regionId;

    @SerializedName("site_id")
    @JsonProperty("site_id")
    boolean siteId;
}
