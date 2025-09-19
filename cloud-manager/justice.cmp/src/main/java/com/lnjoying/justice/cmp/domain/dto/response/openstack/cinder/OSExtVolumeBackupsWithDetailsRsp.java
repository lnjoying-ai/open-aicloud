package com.lnjoying.justice.cmp.domain.dto.response.openstack.cinder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class OSExtVolumeBackupsWithDetailsRsp
{
    private List<OSExtVolumeBackupInfo> backups;
    private Integer count;
    @JsonProperty("backup_links")
    @SerializedName("backup_links")
    private List<String> backupLinks;

    @SerializedName("total_num")
    @JsonProperty("total_num")
    private long totalNum;
}
