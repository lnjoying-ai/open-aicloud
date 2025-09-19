package com.lnjoying.justice.schema.entity.scheduler;

import com.lnjoying.justice.schema.entity.dev.DevNeedInfo;
import com.lnjoying.justice.schema.entity.dev.SchedulingStrategy;
import com.lnjoying.justice.schema.entity.dev.TargetNode;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AssignEdge2InstReq implements Serializable
{
	private String name;

	private DevNeedInfo dev_need_info;

	private Integer on_one_node;

	private List<TargetNode> target_nodes;

	private Integer replica_num;

	private String inst_params;

	private boolean auto_run;

	private String bp_id;

	private String user_id;

	private String inst_type;

	private SchedulingStrategy scheduling_strategy;

	private List<String> waitAssignIdList;

	private String specId;
}
