package com.lnjoying.justice.ecrm.domain.dto.response;

import com.lnjoying.justice.ecrm.domain.dto.model.RegionInfo;
import lombok.Data;

import java.util.List;

@Data
public class QueryRegionRsp
{
	private long totalNum;

	List<RegionInfo> regions;
}
