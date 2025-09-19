package com.lnjoying.justice.cluster.manager.db.mapper;

import com.lnjoying.justice.cluster.manager.db.model.TblClusterAgentInfo;
import com.lnjoying.justice.cluster.manager.db.model.TblClusterAgentInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblClusterAgentInfoMapper {
    long countByExample(TblClusterAgentInfoExample example);

    int deleteByExample(TblClusterAgentInfoExample example);

    int deleteByPrimaryKey(String agentId);

    int insert(TblClusterAgentInfo record);

    int insertSelective(TblClusterAgentInfo record);

    List<TblClusterAgentInfo> selectByExample(TblClusterAgentInfoExample example);

    TblClusterAgentInfo selectByPrimaryKey(String agentId);

    int updateByExampleSelective(@Param("record") TblClusterAgentInfo record, @Param("example") TblClusterAgentInfoExample example);

    int updateByExample(@Param("record") TblClusterAgentInfo record, @Param("example") TblClusterAgentInfoExample example);

    int updateByPrimaryKeySelective(TblClusterAgentInfo record);

    int updateByPrimaryKey(TblClusterAgentInfo record);

    String selectAgentIdByClusterId(@Param("clusterId")String clusterId);
}