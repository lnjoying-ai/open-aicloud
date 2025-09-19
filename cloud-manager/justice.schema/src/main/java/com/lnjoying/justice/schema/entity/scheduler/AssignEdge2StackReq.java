package com.lnjoying.justice.schema.entity.scheduler;

import com.lnjoying.justice.schema.entity.dev.DevNeedInfo;
import com.lnjoying.justice.schema.entity.dev.SchedulingStrategy;
import com.lnjoying.justice.schema.entity.dev.TargetNode;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AssignEdge2StackReq implements Serializable
{
	private String name;

	private DevNeedInfo dev_need_info;

	private List<TargetNode> target_nodes;

	private Integer replica_num;

	private String stack_compose;

	private String justice_compose;

	private String bp_id;

	private String user_id;

	private String aos_type;

	private SchedulingStrategy scheduling_strategy;

	private List<String> waitAssignIdList;

	private String specId;
}
