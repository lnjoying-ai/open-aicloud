package com.lnjoying.justice.ecrm.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.ecrm.domain.dto.model.BindRawNodeInfo;
import lombok.Data;

import java.util.List;

@Data
public class EdgeBindsReq
{
    @SerializedName("bind_infos")
    @JsonProperty("bind_infos")
    List<BindRawNodeInfo> bindInfos;
}
