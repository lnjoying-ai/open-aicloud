package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpOsInstances;
import com.lnjoying.justice.cmp.db.model.TblCmpOsInstancesExample;
import com.lnjoying.justice.cmp.db.model.TblCmpOsInstancesKey;
import java.util.List;
import java.util.Set;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import com.lnjoying.justice.schema.entity.stat.StatusMetrics;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.OS_SERVER)
public interface TblCmpOsInstancesMapper {
    long countByExample(TblCmpOsInstancesExample example);

    int deleteByExample(TblCmpOsInstancesExample example);

    int deleteByPrimaryKey(TblCmpOsInstancesKey key);

    int insert(TblCmpOsInstances record);

    int insertSelective(TblCmpOsInstances record);

    List<TblCmpOsInstances> selectByExample(TblCmpOsInstancesExample example);

    TblCmpOsInstances selectByPrimaryKey(TblCmpOsInstancesKey key);

    int updateByExampleSelective(@Param("record") TblCmpOsInstances record, @Param("example") TblCmpOsInstancesExample example);

    int updateByExample(@Param("record") TblCmpOsInstances record, @Param("example") TblCmpOsInstancesExample example);

    int updateByPrimaryKeySelective(TblCmpOsInstances record);

    int updateByPrimaryKey(TblCmpOsInstances record);

    Set<String> getInstanceIds(@Param("cloudId")String cloudId);

    List<StatusMetrics> getVmInstanceStatusMetrics(@Param("cloudId")String cloudId, @Param("userId")String userId, @Param("bpId")String bpId);
}