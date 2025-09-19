package com.lnjoying.justice.aos.domain.dto.rsp;

import com.lnjoying.justice.aos.domain.model.TemplateInfo;
import com.lnjoying.justice.aos.domain.model.TemplateVerbose;
import lombok.Data;

import java.util.List;

@Data
public class GetTemplateListRsp
{
	long total_num;
	List<TemplateVerbose> templates;
}
