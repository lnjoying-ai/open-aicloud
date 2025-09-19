package com.lnjoying.justice.cmp.domain.dto.response.nextstack.network;

import com.lnjoying.justice.cmp.db.model.TblCmpSubnet;
import com.micro.core.common.Utils;
import lombok.Data;

@Data
public class SubnetDetailInfoRspVo
{
    private String subnetId;
    private String name;
    private Integer phaseStatus;
    private String phaseInfo;
    private String vpcId;
    private String vpcName;
    private String userId;
    private String userName;
    private Integer addressType;
    private String cidr;
    private String gatewayIp;
    private String createTime;
    private String updateTime;

    private String eeBp;
    private String eeUser;
    private String eeBpName;
    private String eeUserName;
    private Integer eeStatus;

    public void setSubnetDetailInfoRsp(TblCmpSubnet tblCmpSubnet)
    {
        this.subnetId = tblCmpSubnet.getSubnetId();
        this.name = tblCmpSubnet.getName();
        this.phaseStatus = tblCmpSubnet.getPhaseStatus() == null ? null : tblCmpSubnet.getPhaseStatus().intValue();
        this.phaseInfo = tblCmpSubnet.getPhaseInfo();
        this.vpcId = tblCmpSubnet.getVpcId();
        this.userId = tblCmpSubnet.getUserId();
        this.addressType = tblCmpSubnet.getAddressType() == null ? null : tblCmpSubnet.getAddressType().intValue();
        this.cidr = tblCmpSubnet.getSubnetCidr();
        this.gatewayIp = tblCmpSubnet.getGatewayIp();
        this.createTime = Utils.formatDate(tblCmpSubnet.getCreateTime());
        this.updateTime = Utils.formatDate(tblCmpSubnet.getUpdateTime());
        this.eeBp = tblCmpSubnet.getEeBp();
        this.eeUser = tblCmpSubnet.getEeUser();
        this.eeStatus = tblCmpSubnet.getEeStatus();
    }
}
