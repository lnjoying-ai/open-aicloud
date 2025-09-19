package com.lnjoying.justice.ecrm.domain.dto.response;

import com.lnjoying.justice.ecrm.domain.dto.model.EdgeNodeInfo;
import lombok.Data;

import java.util.List;

@Data
public class QueryEdgeRsp
{
	long               total_num;
	List<EdgeNodeInfo> nodes;
}
