package com.lnjoying.justice.cmp.service.rpc;

import com.lnjoying.justice.cmp.db.model.TblCmpVolume;
import com.lnjoying.justice.cmp.db.model.TblCmpVolumeExample;
import com.lnjoying.justice.cmp.db.repo.RepoRepository;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo.RpcVolumeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.REMOVED;

@Service
public class RpcVolumeServiceImpl
{
    @Autowired
    private RepoRepository repoRepository;

    public List<RpcVolumeInfo>  getVolumeInfosByVmId(String cloudId, String vmId)
    {
        TblCmpVolumeExample example = new TblCmpVolumeExample();
        TblCmpVolumeExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        criteria.andVmIdEqualTo(vmId);
        criteria.andImageIdIsNull();
        criteria.andPhaseStatusNotEqualTo(REMOVED);
        example.setOrderByClause("create_time desc");
        List<TblCmpVolume> volumes = repoRepository.getVolumes(example);
        if (volumes.size() ==0 ) return  null;
        return volumes.stream().map(this::setVolumeInfo).collect(Collectors.toList());
    }

    private RpcVolumeInfo setVolumeInfo(TblCmpVolume tblVolume)
    {
        RpcVolumeInfo volumeInfo = new RpcVolumeInfo();
        volumeInfo.setVolumeId(tblVolume.getVolumeId());
        volumeInfo.setVolumeName(tblVolume.getName());
        volumeInfo.setSize(tblVolume.getSize());
        volumeInfo.setPhaseStatus(tblVolume.getPhaseStatus());
        volumeInfo.setType(tblVolume.getType());
        volumeInfo.setSource(tblVolume.getSource());
        volumeInfo.setChargeType(tblVolume.getChargeType());
        volumeInfo.setPriceUnit(tblVolume.getPriceUnit());
        volumeInfo.setPeriod(tblVolume.getPeriod());
        volumeInfo.setExpireTime(tblVolume.getExpireTime());
        return volumeInfo;
    }
}
