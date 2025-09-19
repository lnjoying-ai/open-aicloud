package com.lnjoying.justice.cmp.db.repo;

import com.lnjoying.justice.cmp.db.mapper.TblCmpStoragePoolMapper;
import com.lnjoying.justice.cmp.db.mapper.TblCmpVolumeMapper;
import com.lnjoying.justice.cmp.db.mapper.TblCmpVolumeSnapMapper;
import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo.RootVolumeVo;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo.VolumeSnapDetailInfoRsp;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo.VolumeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Repository
@Transactional(rollbackFor = {Exception.class})
public class RepoRepository
{
    @Autowired
    private TblCmpStoragePoolMapper tblCmpStoragePoolMapper;

    @Autowired
    private TblCmpVolumeMapper tblCmpVolumeMapper;

    @Autowired
    private TblCmpVolumeSnapMapper tblCmpVolumeSnapMapper;

    public TblCmpStoragePool getStoragePoolById(String cloudId, String storagePoolId)
    {
        return tblCmpStoragePoolMapper.selectByPrimaryKey(new TblCmpStoragePoolKey(cloudId, storagePoolId));
    }

    public int insertStoragePool(TblCmpStoragePool tblCmpStoragePool)
    {
        return tblCmpStoragePoolMapper.insert(tblCmpStoragePool);
    }

    public List<TblCmpStoragePool> getStoragePools(TblCmpStoragePoolExample example)
    {
        return tblCmpStoragePoolMapper.selectByExample(example);
    }

    public long countStoragePoolsByExample(TblCmpStoragePoolExample example)
    {
        return tblCmpStoragePoolMapper.countByExample(example);
    }

    public int updateStoragePool(TblCmpStoragePool tblCmpStoragePool)
    {
        return tblCmpStoragePoolMapper.updateByPrimaryKeySelective(tblCmpStoragePool);
    }

    public Set<String> getStoragePoolIds(String cloudId)
    {
        return tblCmpStoragePoolMapper.getStoragePoolIds(cloudId);
    }

    public TblCmpVolume getVolumeById(String cloudId, String volumeId)
    {
        return tblCmpVolumeMapper.selectByPrimaryKey(new TblCmpVolumeKey(cloudId, volumeId));
    }

    public int insertVolume(TblCmpVolume tblCmpVolume)
    {
        return tblCmpVolumeMapper.insert(tblCmpVolume);
    }

    public List<TblCmpVolume> getVolumes(TblCmpVolumeExample example)
    {
        return tblCmpVolumeMapper.selectByExample(example);
    }

    public long countVolumesByExample(TblCmpVolumeExample example)
    {
        return tblCmpVolumeMapper.countByExample(example);
    }

    public int updateVolume(TblCmpVolume tblCmpVolume)
    {
        return tblCmpVolumeMapper.updateByPrimaryKeySelective(tblCmpVolume);
    }

    public int updateVolumeForce(TblCmpVolume tblCmpVolume)
    {
        return tblCmpVolumeMapper.updateByPrimaryKey(tblCmpVolume);
    }

    public Set<String> getVolumeIds(String cloudId)
    {
        return tblCmpVolumeMapper.getVolumeIds(cloudId);
    }

    public List<VolumeVo> selectVolumes(TblCmpVolumeExample example)
    {
        return tblCmpVolumeMapper.selectVolumes(example);
    }

    public Long countVolumes(TblCmpVolumeExample example)
    {
        return tblCmpVolumeMapper.countVolumes(example);
    }

    public List<RootVolumeVo> selectRootVolumes(String cloudId, TblCmpVolumeExample example)
    {
        return tblCmpVolumeMapper.selectRootVolumes(cloudId, example);
    }

    public Long countRootVolumes(String cloudId, TblCmpVolumeExample example)
    {
        return tblCmpVolumeMapper.countRootVolumes(cloudId, example);
    }

    public int updateVolumeByEeIdSelective(TblCmpVolume tblCmpVolume)
    {
        return tblCmpVolumeMapper.updateVolumeByEeIdSelective(tblCmpVolume);
    }

    public TblCmpVolume getVolumeByEeId(String cloudId, String eeId)
    {
        return tblCmpVolumeMapper.selectByEeId(cloudId, eeId);
    }

    public TblCmpVolumeSnap getVolumeSnapById(String cloudId, String volumeSnapId)
    {
        return tblCmpVolumeSnapMapper.selectByPrimaryKey(new TblCmpVolumeSnapKey(cloudId, volumeSnapId));
    }

    public int insertVolumeSnap(TblCmpVolumeSnap tblCmpVolumeSnap)
    {
        return tblCmpVolumeSnapMapper.insert(tblCmpVolumeSnap);
    }

    public List<TblCmpVolumeSnap> getVolumeSnaps(TblCmpVolumeSnapExample example)
    {
        return tblCmpVolumeSnapMapper.selectByExample(example);
    }

    public long countVolumeSnapsByExample(TblCmpVolumeSnapExample example)
    {
        return tblCmpVolumeSnapMapper.countByExample(example);
    }

    public int updateVolumeSnap(TblCmpVolumeSnap tblCmpVolumeSnap)
    {
        return tblCmpVolumeSnapMapper.updateByPrimaryKeySelective(tblCmpVolumeSnap);
    }

    public Set<String> getVolumeSnapIds(String cloudId)
    {
        return tblCmpVolumeSnapMapper.getVolumeSnapIds(cloudId);
    }

    public Long countVolumeSnaps(TblCmpVolumeSnapExample example)
    {
        return tblCmpVolumeSnapMapper.countVolumeSnaps(example);
    }

    public List<VolumeSnapDetailInfoRsp> selectVolumeSnaps(TblCmpVolumeSnapExample example)
    {
        return tblCmpVolumeSnapMapper.selectVolumeSnaps(example);
    }
}
