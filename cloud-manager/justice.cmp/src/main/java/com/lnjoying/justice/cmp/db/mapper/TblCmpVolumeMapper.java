package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpVolume;
import com.lnjoying.justice.cmp.db.model.TblCmpVolumeExample;
import com.lnjoying.justice.cmp.db.model.TblCmpVolumeKey;
import java.util.List;
import java.util.Set;

import com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo.RootVolumeVo;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo.VolumeVo;
import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.NS_VOLUME)
public interface TblCmpVolumeMapper {
    long countByExample(TblCmpVolumeExample example);

    int deleteByExample(TblCmpVolumeExample example);

    int deleteByPrimaryKey(TblCmpVolumeKey key);

    int insert(TblCmpVolume record);

    int insertSelective(TblCmpVolume record);

    List<TblCmpVolume> selectByExample(TblCmpVolumeExample example);

    TblCmpVolume selectByPrimaryKey(TblCmpVolumeKey key);

    int updateByExampleSelective(@Param("record") TblCmpVolume record, @Param("example") TblCmpVolumeExample example);

    int updateByExample(@Param("record") TblCmpVolume record, @Param("example") TblCmpVolumeExample example);

    int updateByPrimaryKeySelective(TblCmpVolume record);

    int updateByPrimaryKey(TblCmpVolume record);

    List<VolumeVo> selectVolumes(TblCmpVolumeExample example);

    Long countVolumes(TblCmpVolumeExample example);

    List<RootVolumeVo> selectRootVolumes(@Param("cloudId")String cloudId, @Param("example") TblCmpVolumeExample example);

    Long countRootVolumes(@Param("cloudId")String cloudId, @Param("example") TblCmpVolumeExample example);

    Set<String> getVolumeIds(@Param("cloudId")String cloudId);

    int updateVolumeByEeIdSelective(TblCmpVolume record);

    TblCmpVolume selectByEeId(@Param("cloudId") String cloudId, @Param("eeId") String eeId);
}