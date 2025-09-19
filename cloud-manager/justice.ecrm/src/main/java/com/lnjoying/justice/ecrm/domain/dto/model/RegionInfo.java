package com.lnjoying.justice.ecrm.domain.dto.model;

import com.lnjoying.justice.ecrm.db.model.TblRegionInfo;
import com.micro.core.common.Utils;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class RegionInfo
{
	String                    id;
	String                  name;
	String           description;
	List<NameIDInfo> gw_node_ids;
	String           create_time;
	String            upate_time;
	Map<String, String>   labels;
	Map<String, String>   taints;

	public void setInfo(TblRegionInfo tblRegionInfo)
	{
		this.setId(tblRegionInfo.getRegionId());
		this.setName(tblRegionInfo.getRegionName());
		this.setDescription(tblRegionInfo.getRegionDesc());
		this.setCreate_time(Utils.formatDate(tblRegionInfo.getCreateTime()));
		this.setUpate_time(Utils.formatDate(tblRegionInfo.getUpdateTime()));
	}
}
