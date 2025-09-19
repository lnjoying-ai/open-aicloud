package com.lnjoying.justice.aos.domain.model;

import com.lnjoying.justice.schema.entity.dev.TargetNode;
import com.lnjoying.justice.schema.entity.scheduler.DstNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ServiceInfo
{
	String                   name;
	String                     id;
	String            description;
//	List<DstNode> target_nodes;
	DstNode dst_node;
	List<String>         inst_ids;
	int                  inst_num;
	boolean	      auto_run = true;
	String               stack_id;
	String             stack_name;
	String            create_time;
	String            update_time;
	String             image_name;
	StatusCode status;

	@Data
	@ApiModel(value = "ServiceStatusCode")
	public static final class StatusCode{
		private int code;
		private Map<String, String> desc;
	}
}
