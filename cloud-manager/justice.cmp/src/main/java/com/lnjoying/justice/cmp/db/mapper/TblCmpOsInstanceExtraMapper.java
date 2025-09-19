package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.db.model.TblCmpOsInstanceExtra;
import com.lnjoying.justice.cmp.db.model.TblCmpOsInstanceExtraExample;
import com.lnjoying.justice.cmp.db.model.TblCmpOsInstanceExtraKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblCmpOsInstanceExtraMapper {
    long countByExample(TblCmpOsInstanceExtraExample example);

    int deleteByExample(TblCmpOsInstanceExtraExample example);

    int deleteByPrimaryKey(TblCmpOsInstanceExtraKey key);

    int insert(TblCmpOsInstanceExtra record);

    int insertSelective(TblCmpOsInstanceExtra record);

    List<TblCmpOsInstanceExtra> selectByExample(TblCmpOsInstanceExtraExample example);

    TblCmpOsInstanceExtra selectByPrimaryKey(TblCmpOsInstanceExtraKey key);

    int updateByExampleSelective(@Param("record") TblCmpOsInstanceExtra record, @Param("example") TblCmpOsInstanceExtraExample example);

    int updateByExample(@Param("record") TblCmpOsInstanceExtra record, @Param("example") TblCmpOsInstanceExtraExample example);

    int updateByPrimaryKeySelective(TblCmpOsInstanceExtra record);

    int updateByPrimaryKey(TblCmpOsInstanceExtra record);
}