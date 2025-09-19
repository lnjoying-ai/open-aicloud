package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpFlavor;
import com.lnjoying.justice.cmp.db.model.TblCmpFlavorExample;
import com.lnjoying.justice.cmp.db.model.TblCmpFlavorKey;
import java.util.List;
import java.util.Set;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.NS_FLAVOR)
public interface TblCmpFlavorMapper {
    long countByExample(TblCmpFlavorExample example);

    int deleteByExample(TblCmpFlavorExample example);

    int deleteByPrimaryKey(TblCmpFlavorKey key);

    int insert(TblCmpFlavor record);

    int insertSelective(TblCmpFlavor record);

    List<TblCmpFlavor> selectByExample(TblCmpFlavorExample example);

    TblCmpFlavor selectByPrimaryKey(TblCmpFlavorKey key);

    int updateByExampleSelective(@Param("record") TblCmpFlavor record, @Param("example") TblCmpFlavorExample example);

    int updateByExample(@Param("record") TblCmpFlavor record, @Param("example") TblCmpFlavorExample example);

    int updateByPrimaryKeySelective(TblCmpFlavor record);

    int updateByPrimaryKey(TblCmpFlavor record);

    Set<String> getFlavorIds(@Param("cloudId")String cloudId);
}