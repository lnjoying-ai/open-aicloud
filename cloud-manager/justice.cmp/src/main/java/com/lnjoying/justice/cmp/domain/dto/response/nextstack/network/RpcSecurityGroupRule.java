package com.lnjoying.justice.cmp.domain.dto.response.nextstack.network;

import com.lnjoying.justice.cmp.db.model.TblCmpSecurityGroupRule;
import com.lnjoying.justice.cmp.domain.dto.AddressesRef;
import com.micro.core.common.Utils;
import lombok.Data;

@Data
public class RpcSecurityGroupRule
{
    String ruleId;
    Integer priority;
    Integer direction;
    Integer protocol;
    Integer addressType;
    Integer action;
    String description;
    String port;
    AddressesRef addressRef;
    String createTime;
    String updateTime;

    public void setSecurityGroupRule(TblCmpSecurityGroupRule tblSecurityGroupRule)
    {
        ruleId = tblSecurityGroupRule.getRuleId();
        priority = tblSecurityGroupRule.getPriority() == null ? null : tblSecurityGroupRule.getPriority().intValue();
        direction = tblSecurityGroupRule.getDirection() == null ? null : tblSecurityGroupRule.getDirection().intValue();
        protocol = tblSecurityGroupRule.getProtocol() == null ? null : tblSecurityGroupRule.getProtocol().intValue();
        addressType = tblSecurityGroupRule.getAddressType() == null ? null : tblSecurityGroupRule.getAddressType().intValue();
        port = tblSecurityGroupRule.getPort();
        addressRef = new AddressesRef();
        action = tblSecurityGroupRule.getAction() == null ? null : tblSecurityGroupRule.getAction().intValue();
        description = tblSecurityGroupRule.getDescription();
        createTime = Utils.formatDate(tblSecurityGroupRule.getCreateTime());
        updateTime = Utils.formatDate(tblSecurityGroupRule.getUpdateTime());
        if (null != tblSecurityGroupRule.getCidr() && !tblSecurityGroupRule.getCidr().isEmpty())
        {
            addressRef.setCidr(tblSecurityGroupRule.getCidr());
        }
        else if (null != tblSecurityGroupRule.getSgIdReference() && !tblSecurityGroupRule.getSgIdReference().isEmpty())
        {
            addressRef.setSgId(tblSecurityGroupRule.getSgIdReference());
        }
        else if (null != tblSecurityGroupRule.getPoolId() && !tblSecurityGroupRule.getPoolId().isEmpty())
        {
            addressRef.setIpPoolId(tblSecurityGroupRule.getPoolId());
        }
    }
}
