package com.lnjoying.justice.cmp.domain.dto.response.nextstack.network;

import com.lnjoying.justice.cmp.db.model.TblCmpVpc;
import com.micro.core.common.Utils;
import lombok.Data;

@Data
public class VpcDetailInfoRspVo
{
    private String vpcId;
    private String name;
    private Integer phaseStatus;
    private String phaseInfo;
    private String userId;
    private String userName;
    private Integer count;
    private String createTime;
    private String updateTime;
    private String cidr;

    private String eeBp;
    private String eeUser;
    private String eeBpName;
    private String eeUserName;
    private Integer eeStatus;

    public void setVpcDetailInfoRsp(TblCmpVpc tblCmpVpc)
    {
        this.vpcId = tblCmpVpc.getVpcId();
        this.name = tblCmpVpc.getName();
        this.phaseStatus = tblCmpVpc.getPhaseStatus() == null ? null : tblCmpVpc.getPhaseStatus().intValue();
        this.userId = tblCmpVpc.getUserId();
        this.phaseInfo = tblCmpVpc.getPhaseInfo();
        this.cidr = tblCmpVpc.getVpcCidr();
        this.createTime = Utils.formatDate(tblCmpVpc.getCreateTime());
        this.updateTime = Utils.formatDate(tblCmpVpc.getUpdateTime());

        this.eeBp = tblCmpVpc.getEeBp();
        this.eeUser = tblCmpVpc.getEeUser();
        this.eeStatus = tblCmpVpc.getEeStatus();
    }
}
