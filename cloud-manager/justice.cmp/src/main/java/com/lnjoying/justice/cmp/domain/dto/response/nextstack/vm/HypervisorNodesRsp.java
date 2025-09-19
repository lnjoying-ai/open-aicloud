package com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm;

import com.lnjoying.justice.cmp.db.model.TblCmpHypervisorNode;
import com.micro.core.common.Utils;
import lombok.Data;

import java.util.List;

@Data
public class HypervisorNodesRsp
{
    private long totalNum;
    private List<HypervisorNodeRsp> hypervisorNodes;

    @Data
    public static class HypervisorNodeRsp
    {
        private String nodeId;

        private String name;

        private String manageIp;

        private String hostname;

        private String description;

        private Integer phaseStatus;

        private String createTime;

        private String updateTime;

        private Integer cpuLogCount;

        private Integer memTotal;

        public void setHypervisorNode(TblCmpHypervisorNode tblCmpHypervisorNode)
        {
            nodeId = tblCmpHypervisorNode.getNodeId();
            name = tblCmpHypervisorNode.getName();
            manageIp = tblCmpHypervisorNode.getManageIp();
            hostname = tblCmpHypervisorNode.getHostName();
            description = tblCmpHypervisorNode.getDescription();
            memTotal = tblCmpHypervisorNode.getMemTotal();
            cpuLogCount = tblCmpHypervisorNode.getCpuLogCount();
            createTime = Utils.formatDate(tblCmpHypervisorNode.getCreateTime());
            updateTime = Utils.formatDate(tblCmpHypervisorNode.getUpdateTime());
            phaseStatus = tblCmpHypervisorNode.getPhaseStatus();
        }
    }
}
