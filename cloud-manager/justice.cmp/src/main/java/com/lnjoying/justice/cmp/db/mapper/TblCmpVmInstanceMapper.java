package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpVmInstance;
import com.lnjoying.justice.cmp.db.model.TblCmpVmInstanceExample;
import com.lnjoying.justice.cmp.db.model.TblCmpVmInstanceKey;
import java.util.List;
import java.util.Set;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import com.lnjoying.justice.schema.entity.stat.StatusMetrics;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.NS_VM_INSTANCE)
public interface TblCmpVmInstanceMapper {
    long countByExample(TblCmpVmInstanceExample example);

    int deleteByExample(TblCmpVmInstanceExample example);

    int deleteByPrimaryKey(TblCmpVmInstanceKey key);

    int insert(TblCmpVmInstance record);

    int insertSelective(TblCmpVmInstance record);

    List<TblCmpVmInstance> selectByExample(TblCmpVmInstanceExample example);

    TblCmpVmInstance selectByPrimaryKey(TblCmpVmInstanceKey key);

    int updateByExampleSelective(@Param("record") TblCmpVmInstance record, @Param("example") TblCmpVmInstanceExample example);

    int updateByExample(@Param("record") TblCmpVmInstance record, @Param("example") TblCmpVmInstanceExample example);

    int updateByPrimaryKeySelective(TblCmpVmInstance record);

    int updateByPrimaryKey(TblCmpVmInstance record);

    long sumRootDiskSizeByUserId(@Param("cloudId") String cloudId, @Param("userId") String userId);

    long sumDataDiskSizeByUserId(@Param("cloudId") String cloudId, @Param("userId") String userId);

    Set<String> getVmInstanceIds(@Param("cloudId")String cloudId);

    List<StatusMetrics> getVmInstanceStatusMetrics(@Param("cloudId")String cloudId, @Param("userId")String userId, @Param("bpId")String bpId);

    int updateVmInstanceByEeIdSelective(TblCmpVmInstance record);

    TblCmpVmInstance selectByEeId(@Param("cloudId") String cloudId, @Param("eeId") String eeId);
}