package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.db.model.TblCmpOsAllowedaddresspairs;
import com.lnjoying.justice.cmp.db.model.TblCmpOsAllowedaddresspairsExample;
import com.lnjoying.justice.cmp.db.model.TblCmpOsAllowedaddresspairsKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblCmpOsAllowedaddresspairsMapper {
    long countByExample(TblCmpOsAllowedaddresspairsExample example);

    int deleteByExample(TblCmpOsAllowedaddresspairsExample example);

    int deleteByPrimaryKey(TblCmpOsAllowedaddresspairsKey key);

    int insert(TblCmpOsAllowedaddresspairs record);

    int insertSelective(TblCmpOsAllowedaddresspairs record);

    List<TblCmpOsAllowedaddresspairs> selectByExample(TblCmpOsAllowedaddresspairsExample example);

    TblCmpOsAllowedaddresspairs selectByPrimaryKey(TblCmpOsAllowedaddresspairsKey key);

    int updateByExampleSelective(@Param("record") TblCmpOsAllowedaddresspairs record, @Param("example") TblCmpOsAllowedaddresspairsExample example);

    int updateByExample(@Param("record") TblCmpOsAllowedaddresspairs record, @Param("example") TblCmpOsAllowedaddresspairsExample example);

    int updateByPrimaryKeySelective(TblCmpOsAllowedaddresspairs record);

    int updateByPrimaryKey(TblCmpOsAllowedaddresspairs record);
}