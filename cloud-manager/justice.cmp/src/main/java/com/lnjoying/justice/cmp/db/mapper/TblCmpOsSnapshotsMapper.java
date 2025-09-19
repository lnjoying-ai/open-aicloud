package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpOsSnapshots;
import com.lnjoying.justice.cmp.db.model.TblCmpOsSnapshotsExample;
import com.lnjoying.justice.cmp.db.model.TblCmpOsSnapshotsKey;
import java.util.List;
import java.util.Set;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.OS_VOLUME_SNAPSHOT)
public interface TblCmpOsSnapshotsMapper {
    long countByExample(TblCmpOsSnapshotsExample example);

    int deleteByExample(TblCmpOsSnapshotsExample example);

    int deleteByPrimaryKey(TblCmpOsSnapshotsKey key);

    int insert(TblCmpOsSnapshots record);

    int insertSelective(TblCmpOsSnapshots record);

    List<TblCmpOsSnapshots> selectByExample(TblCmpOsSnapshotsExample example);

    TblCmpOsSnapshots selectByPrimaryKey(TblCmpOsSnapshotsKey key);

    int updateByExampleSelective(@Param("record") TblCmpOsSnapshots record, @Param("example") TblCmpOsSnapshotsExample example);

    int updateByExample(@Param("record") TblCmpOsSnapshots record, @Param("example") TblCmpOsSnapshotsExample example);

    int updateByPrimaryKeySelective(TblCmpOsSnapshots record);

    int updateByPrimaryKey(TblCmpOsSnapshots record);

    Set<String> getSnapshotIds(@Param("cloudId")String cloudId);
}