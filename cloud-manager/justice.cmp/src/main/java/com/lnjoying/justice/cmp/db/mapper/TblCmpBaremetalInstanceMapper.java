package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpBaremetalInstance;
import com.lnjoying.justice.cmp.db.model.TblCmpBaremetalInstanceExample;
import com.lnjoying.justice.cmp.db.model.TblCmpBaremetalInstanceKey;
import java.util.List;
import java.util.Set;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.NS_BAREMETAL_INSTANCE)
public interface TblCmpBaremetalInstanceMapper {
    long countByExample(TblCmpBaremetalInstanceExample example);

    int deleteByExample(TblCmpBaremetalInstanceExample example);

    int deleteByPrimaryKey(TblCmpBaremetalInstanceKey key);

    int insert(TblCmpBaremetalInstance record);

    int insertSelective(TblCmpBaremetalInstance record);

    List<TblCmpBaremetalInstance> selectByExample(TblCmpBaremetalInstanceExample example);

    TblCmpBaremetalInstance selectByPrimaryKey(TblCmpBaremetalInstanceKey key);

    int updateByExampleSelective(@Param("record") TblCmpBaremetalInstance record, @Param("example") TblCmpBaremetalInstanceExample example);

    int updateByExample(@Param("record") TblCmpBaremetalInstance record, @Param("example") TblCmpBaremetalInstanceExample example);

    int updateByPrimaryKeySelective(TblCmpBaremetalInstance record);

    int updateByPrimaryKey(TblCmpBaremetalInstance record);

    long countByPhasesUserId(@Param("cloudId")String cloudId, @Param("userId")String userId,  @Param("baremetalInstancePhaseStatus")Integer baremetalInstancePhaseStatus);

    Set<String> getBaremetalInstanceIds(@Param("cloudId")String cloudId);
}