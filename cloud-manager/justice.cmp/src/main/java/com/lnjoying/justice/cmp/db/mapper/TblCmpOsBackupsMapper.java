package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpOsBackups;
import com.lnjoying.justice.cmp.db.model.TblCmpOsBackupsExample;
import com.lnjoying.justice.cmp.db.model.TblCmpOsBackupsKey;
import java.util.List;
import java.util.Set;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.OS_VOLUME_BACKUP)
public interface TblCmpOsBackupsMapper {
    long countByExample(TblCmpOsBackupsExample example);

    int deleteByExample(TblCmpOsBackupsExample example);

    int deleteByPrimaryKey(TblCmpOsBackupsKey key);

    int insert(TblCmpOsBackups record);

    int insertSelective(TblCmpOsBackups record);

    List<TblCmpOsBackups> selectByExample(TblCmpOsBackupsExample example);

    TblCmpOsBackups selectByPrimaryKey(TblCmpOsBackupsKey key);

    int updateByExampleSelective(@Param("record") TblCmpOsBackups record, @Param("example") TblCmpOsBackupsExample example);

    int updateByExample(@Param("record") TblCmpOsBackups record, @Param("example") TblCmpOsBackupsExample example);

    int updateByPrimaryKeySelective(TblCmpOsBackups record);

    int updateByPrimaryKey(TblCmpOsBackups record);

    Set<String> getBackupIds(@Param("cloudId")String cloudId);
}