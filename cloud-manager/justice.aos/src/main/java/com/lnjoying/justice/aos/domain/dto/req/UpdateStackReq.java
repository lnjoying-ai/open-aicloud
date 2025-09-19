package com.lnjoying.justice.aos.domain.dto.req;

import com.lnjoying.justice.schema.entity.dev.DevNeedInfo;
import com.lnjoying.justice.schema.entity.scheduler.DstNode;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class UpdateStackReq
{
	String description;
	String template_id;
	Map<String, Object> input_params;
	String stack_compose;
	String justice_compose;
	List<String> labels;
	boolean auto_run;
	String bp_id;
	String user_id;
	DevNeedInfo dev_need_info;
	DstNode target_nodes;
}
