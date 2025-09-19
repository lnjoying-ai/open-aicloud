package com.lnjoying.justice.cmp.domain.dto.response.openstack.cinder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class OSExtVolumeSnapshotsWithDetailsRsp
{
    private List<OSExtVolumeSnapshotInfo> snapshots;

    @SerializedName("total_num")
    @JsonProperty("total_num")
    private long totalNum;
}
