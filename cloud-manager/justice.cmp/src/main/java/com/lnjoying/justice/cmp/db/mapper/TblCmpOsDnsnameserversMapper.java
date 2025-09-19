package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.db.model.TblCmpOsDnsnameservers;
import com.lnjoying.justice.cmp.db.model.TblCmpOsDnsnameserversExample;
import com.lnjoying.justice.cmp.db.model.TblCmpOsDnsnameserversKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblCmpOsDnsnameserversMapper {
    long countByExample(TblCmpOsDnsnameserversExample example);

    int deleteByExample(TblCmpOsDnsnameserversExample example);

    int deleteByPrimaryKey(TblCmpOsDnsnameserversKey key);

    int insert(TblCmpOsDnsnameservers record);

    int insertSelective(TblCmpOsDnsnameservers record);

    List<TblCmpOsDnsnameservers> selectByExample(TblCmpOsDnsnameserversExample example);

    TblCmpOsDnsnameservers selectByPrimaryKey(TblCmpOsDnsnameserversKey key);

    int updateByExampleSelective(@Param("record") TblCmpOsDnsnameservers record, @Param("example") TblCmpOsDnsnameserversExample example);

    int updateByExample(@Param("record") TblCmpOsDnsnameservers record, @Param("example") TblCmpOsDnsnameserversExample example);

    int updateByPrimaryKeySelective(TblCmpOsDnsnameservers record);

    int updateByPrimaryKey(TblCmpOsDnsnameservers record);
}