package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpDiskSpec;
import com.lnjoying.justice.cmp.db.model.TblCmpDiskSpecExample;
import java.util.List;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.NS_DISK_SPEC)
public interface TblCmpDiskSpecMapper {
    long countByExample(TblCmpDiskSpecExample example);

    int deleteByExample(TblCmpDiskSpecExample example);

    int insert(TblCmpDiskSpec record);

    int insertSelective(TblCmpDiskSpec record);

    List<TblCmpDiskSpec> selectByExample(TblCmpDiskSpecExample example);

    int updateByExampleSelective(@Param("record") TblCmpDiskSpec record, @Param("example") TblCmpDiskSpecExample example);

    int updateByExample(@Param("record") TblCmpDiskSpec record, @Param("example") TblCmpDiskSpecExample example);
}