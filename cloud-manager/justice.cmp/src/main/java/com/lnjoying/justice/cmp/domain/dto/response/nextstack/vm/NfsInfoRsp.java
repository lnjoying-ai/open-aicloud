package com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm;

import com.lnjoying.justice.cmp.db.model.TblCmpNfs;
import com.micro.core.common.Utils;
import lombok.Data;

@Data
public class NfsInfoRsp
{
    private String nfsId;

    private String name;

    private String createTime;

    private String updateTime;

    private String vpcId;

    private String subnetId;

    private String portId;

    private String vpcCidr;

    private String subnetCidr;

    private Integer size;

    private Integer phaseStatus;

    private String servicePath;

    private String description;

    private String vpcName;

    private String subnetName;

    private String eeBp;
    private String eeUser;
    private String eeBpName;
    private String eeUserName;

    public void setNfsInfo(TblCmpNfs tblNfs)
    {
        this.nfsId = tblNfs.getNfsId();
        this.name = tblNfs.getName();
        this.createTime = Utils.formatDate(tblNfs.getCreateTime());
        this.updateTime = Utils.formatDate(tblNfs.getUpdateTime());
        this.vpcId = tblNfs.getVpcId();
        this.subnetId = tblNfs.getSubnetId();
        this.portId = tblNfs.getPortId();
        this.size = tblNfs.getSize();
        this.phaseStatus = tblNfs.getPhaseStatus();
        this.description = tblNfs.getDescription();

        this.eeBp = tblNfs.getEeBp();
        this.eeUser = tblNfs.getEeUser();
    }
}
