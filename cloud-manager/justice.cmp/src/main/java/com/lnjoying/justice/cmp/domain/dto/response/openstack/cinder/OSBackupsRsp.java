package com.lnjoying.justice.cmp.domain.dto.response.openstack.cinder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class OSBackupsRsp
{
    private List<OSBackupBasicInfo> backups;
    @JsonProperty("volumes_links")
    @SerializedName("backup_links")
    private List<String> backup_links;
    private Integer count;
}
