package com.lnjoying.justice.cmp.domain.dto.request.nextstack.network;

import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
public class SgRulesCreateUpdateReqVo
{
    @Valid
    private List<SgRuleCreateUpdateReqVo> createSgRules;
}
