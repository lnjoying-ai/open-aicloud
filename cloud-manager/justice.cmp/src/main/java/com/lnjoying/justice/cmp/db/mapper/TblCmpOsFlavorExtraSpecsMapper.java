package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.db.model.TblCmpOsFlavorExtraSpecs;
import com.lnjoying.justice.cmp.db.model.TblCmpOsFlavorExtraSpecsExample;
import com.lnjoying.justice.cmp.db.model.TblCmpOsFlavorExtraSpecsKey;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

public interface TblCmpOsFlavorExtraSpecsMapper {
    long countByExample(TblCmpOsFlavorExtraSpecsExample example);

    int deleteByExample(TblCmpOsFlavorExtraSpecsExample example);

    int deleteByPrimaryKey(TblCmpOsFlavorExtraSpecsKey key);

    int insert(TblCmpOsFlavorExtraSpecs record);

    int insertSelective(TblCmpOsFlavorExtraSpecs record);

    List<TblCmpOsFlavorExtraSpecs> selectByExample(TblCmpOsFlavorExtraSpecsExample example);

    TblCmpOsFlavorExtraSpecs selectByPrimaryKey(TblCmpOsFlavorExtraSpecsKey key);

    int updateByExampleSelective(@Param("record") TblCmpOsFlavorExtraSpecs record, @Param("example") TblCmpOsFlavorExtraSpecsExample example);

    int updateByExample(@Param("record") TblCmpOsFlavorExtraSpecs record, @Param("example") TblCmpOsFlavorExtraSpecsExample example);

    int updateByPrimaryKeySelective(TblCmpOsFlavorExtraSpecs record);

    int updateByPrimaryKey(TblCmpOsFlavorExtraSpecs record);

    Set<String> getFlavorExtraSpecKeys(@Param("cloudId")String cloudId, @Param("flavorId")String flavorId);
}