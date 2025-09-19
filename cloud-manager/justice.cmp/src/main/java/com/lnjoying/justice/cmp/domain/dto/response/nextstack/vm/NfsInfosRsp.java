package com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm;

import lombok.Data;

import java.util.List;

@Data
public class NfsInfosRsp
{
    private long totalNum;

    private List<NfsInfoRsp> nfsInfos;
}
