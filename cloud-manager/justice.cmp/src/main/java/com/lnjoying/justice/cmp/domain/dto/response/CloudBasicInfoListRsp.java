package com.lnjoying.justice.cmp.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cmp.domain.model.CloudBasicInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CloudBasicInfoListRsp
{
    @SerializedName("total_num")
    @JsonProperty("total_num")
    private int totalNum;

    @SerializedName("clouds")
    @JsonProperty("clouds")
    List<CloudBasicInfo> clouds;
}
