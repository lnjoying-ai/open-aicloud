package com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo;

import lombok.Data;

import java.util.List;

@Data
public class VolumesRsp
{
    private long totalNum;

    private List<VolumeVo> volumes;
}
