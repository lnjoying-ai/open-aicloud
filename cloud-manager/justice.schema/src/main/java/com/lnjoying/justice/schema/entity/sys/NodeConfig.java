package com.lnjoying.justice.schema.entity.sys;

import lombok.Data;

import java.io.Serializable;

@Data
public class NodeConfig  implements Serializable
{
	String region_id;
	int node_type = 2;
	String node_id;
	String private_key;
	String core_version = "1.0.0";
	String protocol_version = "1.0.0";
	String node_name;
	int    magic_num = 0XE8D1A111;

	public void reset()
	{
		node_name = "";
		node_id = "";
		private_key = "";
		core_version = "";
		protocol_version = "";
	}
}
