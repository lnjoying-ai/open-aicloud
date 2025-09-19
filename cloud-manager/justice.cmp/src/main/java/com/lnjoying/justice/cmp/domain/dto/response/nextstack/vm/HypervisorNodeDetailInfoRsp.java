package com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm;

import com.lnjoying.justice.cmp.db.model.TblCmpHypervisorNode;
import com.micro.core.common.Utils;
import lombok.Data;

@Data
public class HypervisorNodeDetailInfoRsp
{
    String nodeId;

    String name;

    String manageIp;

    String hostname;

    String description;

    String sysUsername;

    String pubkeyId;

    String createTime;

    String updateTime;

    private boolean hasGpu;

    public void setHypervisorNodeDetailInfo(TblCmpHypervisorNode tblCmpHypervisorNode)
    {
        nodeId = tblCmpHypervisorNode.getNodeId();

        name = tblCmpHypervisorNode.getName();

        manageIp = tblCmpHypervisorNode.getManageIp();

        hostname = tblCmpHypervisorNode.getHostName();

        description = tblCmpHypervisorNode.getDescription();

        sysUsername = tblCmpHypervisorNode.getSysUsername();

        pubkeyId = tblCmpHypervisorNode.getPubkeyId();

        createTime = Utils.formatDate(tblCmpHypervisorNode.getCreateTime());

        updateTime = Utils.formatDate(tblCmpHypervisorNode.getUpdateTime());
    }
}
