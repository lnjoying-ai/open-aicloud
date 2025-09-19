package com.lnjoying.justice.ecrm.domain.dto.response;

import com.lnjoying.justice.ecrm.domain.dto.model.GwNodeInfo;
import lombok.Data;

import java.util.List;

@Data
public class QueryGwRsp
{
	long         total_num;
	List<GwNodeInfo> nodes;
}
