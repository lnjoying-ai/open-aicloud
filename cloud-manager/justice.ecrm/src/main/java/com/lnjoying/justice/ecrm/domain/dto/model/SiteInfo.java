package com.lnjoying.justice.ecrm.domain.dto.model;

import com.lnjoying.justice.ecrm.db.model.TblSiteInfo;
import com.micro.core.common.Utils;
import lombok.Data;

import java.util.Map;

@Data
public class SiteInfo
{
	String                    id;
	String                  name;
	String             region_id;
	String           region_name;
	Location            location;
	String           description;
	String           create_time;
	String           update_time;
	Map<String, String>   labels;
	Map<String, String>   taints;
	int                cloud_num;
	int                 node_num;

	public void setInfo(TblSiteInfo tblSiteInfo)
	{
		this.setId(tblSiteInfo.getSiteId());
		this.setRegion_id(tblSiteInfo.getRegionId());
		this.setName(tblSiteInfo.getSiteName());
		this.setDescription(tblSiteInfo.getSiteDesc());
		this.setUpdate_time(Utils.formatDate(tblSiteInfo.getUpdateTime()));
		this.setCreate_time(Utils.formatDate(tblSiteInfo.getCreateTime()));
	}
}
