package com.lnjoying.justice.cmp.domain.dto.response.openstack.cinder;

import com.lnjoying.justice.cmp.db.model.TblCmpOsBackups;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.OSLink;
import lombok.Data;

import java.util.List;

@Data
public class OSBackupBasicInfo
{
    private String id;
    private String name;
    private List<OSLink> links;

    public void setVolumeBasicInfo(TblCmpOsBackups tblCmpOsBackup)
    {
        this.id = tblCmpOsBackup.getId();
        this.name = tblCmpOsBackup.getDisplayName();
    }
}
