package com.lnjoying.justice.cmp.domain.dto.response.openstack.cinder;

import com.lnjoying.justice.cmp.db.model.TblCmpOsVolumes;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.OSLink;
import lombok.Data;

import java.util.List;

@Data
public class OSVolumeBasicInfo
{
    private String id;
    private String name;
    private List<OSLink> links;

    public void setVolumeBasicInfo(TblCmpOsVolumes tblCmpOsVolume)
    {
        this.id = tblCmpOsVolume.getId();
        this.name = tblCmpOsVolume.getDisplayName();
    }
}
