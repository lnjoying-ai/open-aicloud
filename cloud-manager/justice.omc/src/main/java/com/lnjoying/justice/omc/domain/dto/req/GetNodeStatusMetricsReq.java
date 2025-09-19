package com.lnjoying.justice.omc.domain.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2024/1/6 19:34
 */

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetNodeStatusMetricsReq
{
    @SerializedName("region_id")
    @JsonProperty("region_id")
    String regionId;

    @SerializedName("site_id")
    @JsonProperty("site_id")
    String siteId;

    @SerializedName("filter")
    @JsonProperty("filter")
    String filter;
}
