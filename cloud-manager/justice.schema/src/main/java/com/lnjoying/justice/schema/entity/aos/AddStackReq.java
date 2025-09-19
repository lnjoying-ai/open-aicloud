package com.lnjoying.justice.schema.entity.aos;

import com.lnjoying.justice.schema.entity.dev.*;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class AddStackReq implements Serializable
{
	private String name;
	private String description;
	private String template_id;
	private Map<String, String> input_params;
	private String stack_compose;
	private String justice_compose;
	private List<String> labels;
	private Boolean auto_run;
	private String bp_id;
	private String user_id;
	private String aos_type;
	private String content_type;
	private int replica_num;
	private DevNeedInfo dev_need_info = DevNeedInfo.builder().cpu(new CpuNeed()).mem(new MemNeed()).disk(new DiskNeed()).build();
	private List<TargetNode> target_nodes;
	private String registry_id;
	private SchedulingStrategy scheduling_strategy;
	private Map<String, String> extra_params = new HashMap<>();
	private Boolean always_online;
	private String failover_policy;
	private String stack_type="deployment"; //deployment daemonset
	private Boolean use_service_penetration=false; //true: use service penetration
	private Integer deploy_strategy;   //0:site daemonset 1:node daemonset
}
