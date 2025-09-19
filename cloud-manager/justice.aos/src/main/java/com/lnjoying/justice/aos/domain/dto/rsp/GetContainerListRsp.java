package com.lnjoying.justice.aos.domain.dto.rsp;

import com.lnjoying.justice.aos.domain.model.ContainerInstInfo;
import com.lnjoying.justice.aos.domain.model.ServiceInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetContainerListRsp
{
	long total_num;
	List<ContainerInstInfo> containers;
}
