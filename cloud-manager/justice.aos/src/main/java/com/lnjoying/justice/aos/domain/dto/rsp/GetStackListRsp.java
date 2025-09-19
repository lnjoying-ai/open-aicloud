package com.lnjoying.justice.aos.domain.dto.rsp;

import com.lnjoying.justice.aos.domain.model.StackInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetStackListRsp
{
	long total_num;
	List<StackInfo> stacks;
}
