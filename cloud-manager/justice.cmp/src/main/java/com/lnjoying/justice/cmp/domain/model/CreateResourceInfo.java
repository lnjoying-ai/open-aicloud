package com.lnjoying.justice.cmp.domain.model;

import lombok.Data;

@Data
public class CreateResourceInfo
{
    private String cloudId;

    private String eeId;

    private String bpId;

    private String userId;

    private Object object;

    public CreateResourceInfo(String cloudId, String eeId, String bpId, String userId, Object object)
    {
        this.cloudId = cloudId;
        this.eeId = eeId;
        this.bpId = bpId;
        this.userId = userId;
        this.object = object;
    }
}
