package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpOsFlavors;
import com.lnjoying.justice.cmp.db.model.TblCmpOsFlavorsExample;
import com.lnjoying.justice.cmp.db.model.TblCmpOsFlavorsKey;
import java.util.List;
import java.util.Set;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.OS_FLAVOR)
public interface TblCmpOsFlavorsMapper {
    long countByExample(TblCmpOsFlavorsExample example);

    int deleteByExample(TblCmpOsFlavorsExample example);

    int deleteByPrimaryKey(TblCmpOsFlavorsKey key);

    int insert(TblCmpOsFlavors record);

    int insertSelective(TblCmpOsFlavors record);

    List<TblCmpOsFlavors> selectByExample(TblCmpOsFlavorsExample example);

    TblCmpOsFlavors selectByPrimaryKey(TblCmpOsFlavorsKey key);

    int updateByExampleSelective(@Param("record") TblCmpOsFlavors record, @Param("example") TblCmpOsFlavorsExample example);

    int updateByExample(@Param("record") TblCmpOsFlavors record, @Param("example") TblCmpOsFlavorsExample example);

    int updateByPrimaryKeySelective(TblCmpOsFlavors record);

    int updateByPrimaryKey(TblCmpOsFlavors record);

    Set<String> getFlavorIds(@Param("cloudId")String cloudId);
}