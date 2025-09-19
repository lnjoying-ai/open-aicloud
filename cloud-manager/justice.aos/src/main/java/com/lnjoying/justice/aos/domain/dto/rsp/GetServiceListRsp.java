package com.lnjoying.justice.aos.domain.dto.rsp;

import com.lnjoying.justice.aos.domain.model.ServiceInfo;
import com.lnjoying.justice.aos.domain.model.TemplateVerbose;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetServiceListRsp
{
	long total_num;
	List<ServiceInfo> services;
}
