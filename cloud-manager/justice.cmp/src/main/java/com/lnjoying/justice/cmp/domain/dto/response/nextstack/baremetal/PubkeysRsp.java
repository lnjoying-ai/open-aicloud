package com.lnjoying.justice.cmp.domain.dto.response.nextstack.baremetal;

import com.lnjoying.justice.cmp.domain.info.baremetal.PubkeyDetailInfo;
import lombok.Data;

import java.util.List;

@Data
public class PubkeysRsp
{
    private long totalNum;
    private List<PubkeyDetailInfo> pubkeys;
}
