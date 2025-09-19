package com.lnjoying.justice.cmp.domain.dto.response.openstack.nova;

import com.lnjoying.justice.cmp.db.model.TblCmpOsFlavors;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.OSLink;
import lombok.Data;

import java.util.List;

@Data
public class OSFlavorBasicInfo
{
    private String id;
    private String name;
    private String description;
    private List<OSLink> links;

    public void setFlavorBasicInfo(TblCmpOsFlavors tblCmpOsFlavor)
    {
        this.id = tblCmpOsFlavor.getFlavorid();
        this.name = tblCmpOsFlavor.getName();
        this.description = tblCmpOsFlavor.getDescription();
    }
}
