package com.lnjoying.justice.cmp.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cmp.domain.model.CloudInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CloudInfoListRsp
{
    @SerializedName("total_num")
    @JsonProperty("total_num")
    private long totalNum;

    @SerializedName("clouds")
    @JsonProperty("clouds")
    List<CloudInfo> clouds;
}
