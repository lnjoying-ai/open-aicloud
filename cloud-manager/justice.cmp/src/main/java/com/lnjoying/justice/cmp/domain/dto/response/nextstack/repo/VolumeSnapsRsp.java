package com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo;

import lombok.Data;

import java.util.List;

@Data
public class VolumeSnapsRsp
{
    private long totalNum;

    private List<VolumeSnapDetailInfoRsp> volumeSnaps;
}
