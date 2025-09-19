package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpBaremetalDevice;
import com.lnjoying.justice.cmp.db.model.TblCmpBaremetalDeviceExample;
import com.lnjoying.justice.cmp.db.model.TblCmpBaremetalDeviceKey;
import java.util.List;
import java.util.Set;

import com.lnjoying.justice.cmp.domain.info.baremetal.BaremetalDeviceInfo;
import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.NS_BAREMETAL_DEVICE)
public interface TblCmpBaremetalDeviceMapper {
    long countByExample(TblCmpBaremetalDeviceExample example);

    int deleteByExample(TblCmpBaremetalDeviceExample example);

    int deleteByPrimaryKey(TblCmpBaremetalDeviceKey key);

    int insert(TblCmpBaremetalDevice record);

    int insertSelective(TblCmpBaremetalDevice record);

    List<TblCmpBaremetalDevice> selectByExample(TblCmpBaremetalDeviceExample example);

    TblCmpBaremetalDevice selectByPrimaryKey(TblCmpBaremetalDeviceKey key);

    int updateByExampleSelective(@Param("record") TblCmpBaremetalDevice record, @Param("example") TblCmpBaremetalDeviceExample example);

    int updateByExample(@Param("record") TblCmpBaremetalDevice record, @Param("example") TblCmpBaremetalDeviceExample example);

    int updateByPrimaryKeySelective(TblCmpBaremetalDevice record);

    int updateByPrimaryKey(TblCmpBaremetalDevice record);

    List<BaremetalDeviceInfo> selectByPhasesUserId(@Param("cloudId")String cloudId,
                                                   @Param("baremetalDevicePhaseStatus")Integer baremetalDevicePhaseStatus,
                                                   @Param("nicSpecPhaseStatus")Integer nicSpecPhaseStatus,
                                                   @Param("userId")String userId,
                                                   @Param("cpuNum") Integer cpuNum,
                                                   @Param("memTotal") Long memTotal,
                                                   @Param("orderByClause")String orderByClause,
                                                   @Param("pageSize")Integer pageSize,
                                                   @Param("startRow")Integer startRow);

    long countByPhasesUserId(@Param("cloudId")String cloudId,
                             @Param("baremetalDevicePhaseStatus")Integer baremetalDevicePhaseStatus,
                             @Param("nicSpecPhaseStatus")Integer nicSpecPhaseStatus,
                             @Param("userId")String userId,
                             @Param("cpuNum") Integer cpuNum,
                             @Param("memTotal") Long memTotal);

    Set<String> getBaremetalDeviceIds(@Param("cloudId")String cloudId);
}