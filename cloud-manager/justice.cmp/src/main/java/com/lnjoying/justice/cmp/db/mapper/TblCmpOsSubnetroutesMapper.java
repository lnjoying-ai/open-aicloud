package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.db.model.TblCmpOsSubnetroutes;
import com.lnjoying.justice.cmp.db.model.TblCmpOsSubnetroutesExample;
import com.lnjoying.justice.cmp.db.model.TblCmpOsSubnetroutesKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblCmpOsSubnetroutesMapper {
    long countByExample(TblCmpOsSubnetroutesExample example);

    int deleteByExample(TblCmpOsSubnetroutesExample example);

    int deleteByPrimaryKey(TblCmpOsSubnetroutesKey key);

    int insert(TblCmpOsSubnetroutes record);

    int insertSelective(TblCmpOsSubnetroutes record);

    List<TblCmpOsSubnetroutes> selectByExample(TblCmpOsSubnetroutesExample example);

    TblCmpOsSubnetroutes selectByPrimaryKey(TblCmpOsSubnetroutesKey key);

    int updateByExampleSelective(@Param("record") TblCmpOsSubnetroutes record, @Param("example") TblCmpOsSubnetroutesExample example);

    int updateByExample(@Param("record") TblCmpOsSubnetroutes record, @Param("example") TblCmpOsSubnetroutesExample example);

    int updateByPrimaryKeySelective(TblCmpOsSubnetroutes record);

    int updateByPrimaryKey(TblCmpOsSubnetroutes record);
}