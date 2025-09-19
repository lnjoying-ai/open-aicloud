package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.db.model.TblCmpSgVmInstance;
import com.lnjoying.justice.cmp.db.model.TblCmpSgVmInstanceExample;
import com.lnjoying.justice.cmp.db.model.TblCmpSgVmInstanceKey;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

public interface TblCmpSgVmInstanceMapper {
    long countByExample(TblCmpSgVmInstanceExample example);

    int deleteByExample(TblCmpSgVmInstanceExample example);

    int deleteByPrimaryKey(TblCmpSgVmInstanceKey key);

    int insert(TblCmpSgVmInstance record);

    int insertSelective(TblCmpSgVmInstance record);

    List<TblCmpSgVmInstance> selectByExample(TblCmpSgVmInstanceExample example);

    TblCmpSgVmInstance selectByPrimaryKey(TblCmpSgVmInstanceKey key);

    int updateByExampleSelective(@Param("record") TblCmpSgVmInstance record, @Param("example") TblCmpSgVmInstanceExample example);

    int updateByExample(@Param("record") TblCmpSgVmInstance record, @Param("example") TblCmpSgVmInstanceExample example);

    int updateByPrimaryKeySelective(TblCmpSgVmInstance record);

    int updateByPrimaryKey(TblCmpSgVmInstance record);

    Set<String> getSgVmInstanceIds(@Param("cloudId")String cloudId, @Param("instanceId")String instanceId);

    Set<String> getSgVmInstanceIdsBySgIds(@Param("cloudId")String cloudId, @Param("sgId")String sgId);
}