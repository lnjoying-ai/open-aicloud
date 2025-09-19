package com.lnjoying.justice.iam.domain.dto.response.bp;

import lombok.Data;

import java.util.List;

@Data
public class QueryBpsRsp
{
	int total_num;
	List<BpRsp> bps;
}
