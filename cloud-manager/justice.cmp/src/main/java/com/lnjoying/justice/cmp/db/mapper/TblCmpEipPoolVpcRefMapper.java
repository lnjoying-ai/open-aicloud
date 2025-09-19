package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.db.model.TblCmpEipPoolVpcRef;
import com.lnjoying.justice.cmp.db.model.TblCmpEipPoolVpcRefExample;
import com.lnjoying.justice.cmp.db.model.TblCmpEipPoolVpcRefKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblCmpEipPoolVpcRefMapper {
    long countByExample(TblCmpEipPoolVpcRefExample example);

    int deleteByExample(TblCmpEipPoolVpcRefExample example);

    int deleteByPrimaryKey(TblCmpEipPoolVpcRefKey key);

    int insert(TblCmpEipPoolVpcRef record);

    int insertSelective(TblCmpEipPoolVpcRef record);

    List<TblCmpEipPoolVpcRef> selectByExample(TblCmpEipPoolVpcRefExample example);

    TblCmpEipPoolVpcRef selectByPrimaryKey(TblCmpEipPoolVpcRefKey key);

    int updateByExampleSelective(@Param("record") TblCmpEipPoolVpcRef record, @Param("example") TblCmpEipPoolVpcRefExample example);

    int updateByExample(@Param("record") TblCmpEipPoolVpcRef record, @Param("example") TblCmpEipPoolVpcRefExample example);

    int updateByPrimaryKeySelective(TblCmpEipPoolVpcRef record);

    int updateByPrimaryKey(TblCmpEipPoolVpcRef record);
}