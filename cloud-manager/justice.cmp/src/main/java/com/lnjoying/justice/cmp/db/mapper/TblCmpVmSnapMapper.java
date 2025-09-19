package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpVmSnap;
import com.lnjoying.justice.cmp.db.model.TblCmpVmSnapExample;
import com.lnjoying.justice.cmp.db.model.TblCmpVmSnapKey;
import java.util.List;
import java.util.Set;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.NS_VM_SNAP)
public interface TblCmpVmSnapMapper {
    long countByExample(TblCmpVmSnapExample example);

    int deleteByExample(TblCmpVmSnapExample example);

    int deleteByPrimaryKey(TblCmpVmSnapKey key);

    int insert(TblCmpVmSnap record);

    int insertSelective(TblCmpVmSnap record);

    List<TblCmpVmSnap> selectByExample(TblCmpVmSnapExample example);

    TblCmpVmSnap selectByPrimaryKey(TblCmpVmSnapKey key);

    int updateByExampleSelective(@Param("record") TblCmpVmSnap record, @Param("example") TblCmpVmSnapExample example);

    int updateByExample(@Param("record") TblCmpVmSnap record, @Param("example") TblCmpVmSnapExample example);

    int updateByPrimaryKeySelective(TblCmpVmSnap record);

    int updateByPrimaryKey(TblCmpVmSnap record);

    Set<String> getSnapIds(@Param("cloudId")String cloudId);
}