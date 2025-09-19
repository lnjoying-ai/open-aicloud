package com.lnjoying.justice.scheduler.domain.model;

import com.lnjoying.justice.schema.entity.dev.DevNeedInfo;
import com.lnjoying.justice.schema.entity.dev.LabelSelector;
import com.lnjoying.justice.schema.entity.dev.TargetNode;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AssignEdgeReq
{
    private String strategy;
    private DevNeedInfo devNeedInfo;
    private Integer onOneNode;
    private List<TargetNode> targetNodes;
    private Integer replicaNum;

    private Map<String, List<LabelSelector>> labelSelectorMap;

    private boolean replicaCompleteStrategy;

    private List<String> refIdList;
    private String waitAssignId;
    private String userId;
    private String bpId;
}
