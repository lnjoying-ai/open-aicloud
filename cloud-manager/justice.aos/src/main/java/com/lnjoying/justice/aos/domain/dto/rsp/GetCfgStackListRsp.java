package com.lnjoying.justice.aos.domain.dto.rsp;

import com.lnjoying.justice.aos.domain.model.StackInfo;
import lombok.Data;

import java.util.List;

@Data
public class GetCfgStackListRsp
{
	List<StackInfo> stacks;
}
