package com.lnjoying.justice.aos.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lnjoying.justice.aos.common.ExposePort;
import com.lnjoying.justice.schema.entity.TipMessage;
import com.lnjoying.justice.schema.entity.dev.DevNeedInfo;
import com.lnjoying.justice.schema.entity.dev.TargetNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class StackInfo
{
	String id;
	String name;
	String description;
	StackTemplateBrief template;
	String bp_id;
	String bp_name;
	String user_id;
	String user_name;
	String create_user_id;
	StatusCode	status;
	int replica_num;
	List<TargetNode> target_nodes;
	FullDstNode dst_node;
	List<String> service_ids;
	List<String> labels;
	DevNeedInfo dev_need_info;
	TipMessage tip_message;
	List<ExposePort> port_maps;
	String	create_time;
	String  update_time;
	@Data
	@ApiModel(value = "StackStatusCode")
	public static final class StatusCode{
		private int code;
		private Map<String, String> desc;
	}

	@Data
	@ApiModel(value = "FullDstNode")
	public static final class FullDstNode{

		@JsonProperty(value = "dst_site_id")
		private String dstSiteId;

		@JsonProperty(value = "dst_site_name")
		private String dstSiteName;

		@JsonProperty(value = "dst_region_id")
		private String dstRegionId;

		@JsonProperty(value = "dst_region_name")
		private String dstRegionName;

		@JsonProperty(value = "dst_node_id")
		private String dstNodeId;

		@JsonProperty(value = "dst_node_name")
		private String dstNodeName;
	}
}
