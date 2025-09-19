package com.lnjoying.justice.ecrm.domain.dto.model;

import com.lnjoying.justice.ecrm.domain.dto.model.SiteInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
public class RegionSiteInfo
{
	String     region_id;
	String   region_name;
	@EqualsAndHashCode.Exclude
	List<SiteInfo> sites;

	public RegionSiteInfo()
	{}

	public RegionSiteInfo(String region_id, String region_name)
	{
		this.region_id = region_id;
		this.region_name = region_name;
	}
}
