package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpDiskInfo;
import com.lnjoying.justice.cmp.db.model.TblCmpDiskInfoExample;
import com.lnjoying.justice.cmp.db.model.TblCmpDiskInfoKey;
import java.util.List;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.NS_DISK)
public interface TblCmpDiskInfoMapper {
    long countByExample(TblCmpDiskInfoExample example);

    int deleteByExample(TblCmpDiskInfoExample example);

    int deleteByPrimaryKey(TblCmpDiskInfoKey key);

    int insert(TblCmpDiskInfo record);

    int insertSelective(TblCmpDiskInfo record);

    List<TblCmpDiskInfo> selectByExample(TblCmpDiskInfoExample example);

    TblCmpDiskInfo selectByPrimaryKey(TblCmpDiskInfoKey key);

    int updateByExampleSelective(@Param("record") TblCmpDiskInfo record, @Param("example") TblCmpDiskInfoExample example);

    int updateByExample(@Param("record") TblCmpDiskInfo record, @Param("example") TblCmpDiskInfoExample example);

    int updateByPrimaryKeySelective(TblCmpDiskInfo record);

    int updateByPrimaryKey(TblCmpDiskInfo record);
}