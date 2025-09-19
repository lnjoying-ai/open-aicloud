package com.lnjoying.justice.cmp.domain.dto.response.openstack.cinder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class OSVolumeBackupsWithDetailsRsp
{
    private List<OSVolumeBackupInfo> backups;
    private Integer count;
    @JsonProperty("backup_links")
    @SerializedName("backup_links")
    private List<String> backupLinks;
}
