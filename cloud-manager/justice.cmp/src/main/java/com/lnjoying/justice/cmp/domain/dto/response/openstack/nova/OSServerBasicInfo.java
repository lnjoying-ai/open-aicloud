package com.lnjoying.justice.cmp.domain.dto.response.openstack.nova;

import com.lnjoying.justice.cmp.db.model.TblCmpOsInstances;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.OSLink;
import lombok.Data;

import java.util.List;

@Data
public class OSServerBasicInfo
{
    private String id;
    private String name;
    private List<OSLink> links;

    public void setServerBasicInfo(TblCmpOsInstances tblCmpOsInstance)
    {
        this.id = tblCmpOsInstance.getUuid();
        this.name = tblCmpOsInstance.getDisplayName();
    }
}
