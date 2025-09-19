package com.lnjoying.justice.ecrm.domain.dto.response;

import com.lnjoying.justice.ecrm.domain.dto.model.RegionSiteInfo;
import lombok.Data;

import java.util.List;

@Data
public class QuerySiteRsp
{
	private long totalNum;

	List<RegionSiteInfo> sites;
}
