package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpVolumeSnap;
import com.lnjoying.justice.cmp.db.model.TblCmpVolumeSnapExample;
import com.lnjoying.justice.cmp.db.model.TblCmpVolumeSnapKey;
import java.util.List;
import java.util.Set;

import com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo.VolumeSnapDetailInfoRsp;
import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.NS_VOLUME_SNAP)
public interface TblCmpVolumeSnapMapper {
    long countByExample(TblCmpVolumeSnapExample example);

    int deleteByExample(TblCmpVolumeSnapExample example);

    int deleteByPrimaryKey(TblCmpVolumeSnapKey key);

    int insert(TblCmpVolumeSnap record);

    int insertSelective(TblCmpVolumeSnap record);

    List<TblCmpVolumeSnap> selectByExample(TblCmpVolumeSnapExample example);

    TblCmpVolumeSnap selectByPrimaryKey(TblCmpVolumeSnapKey key);

    int updateByExampleSelective(@Param("record") TblCmpVolumeSnap record, @Param("example") TblCmpVolumeSnapExample example);

    int updateByExample(@Param("record") TblCmpVolumeSnap record, @Param("example") TblCmpVolumeSnapExample example);

    int updateByPrimaryKeySelective(TblCmpVolumeSnap record);

    int updateByPrimaryKey(TblCmpVolumeSnap record);

    Long countVolumeSnaps(TblCmpVolumeSnapExample example);

    List<VolumeSnapDetailInfoRsp> selectVolumeSnaps(TblCmpVolumeSnapExample example);

    Set<String> getVolumeSnapIds(@Param("cloudId")String cloudId);
}