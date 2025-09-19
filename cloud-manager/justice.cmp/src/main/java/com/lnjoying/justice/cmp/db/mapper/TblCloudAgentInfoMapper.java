package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCloudAgentInfo;
import com.lnjoying.justice.cmp.db.model.TblCloudAgentInfoExample;
import java.util.List;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.CLOUD_AGENT)
public interface TblCloudAgentInfoMapper {
    long countByExample(TblCloudAgentInfoExample example);

    int deleteByExample(TblCloudAgentInfoExample example);

    int deleteByPrimaryKey(String agentId);

    int insert(TblCloudAgentInfo record);

    int insertSelective(TblCloudAgentInfo record);

    List<TblCloudAgentInfo> selectByExample(TblCloudAgentInfoExample example);

    TblCloudAgentInfo selectByPrimaryKey(String agentId);

    int updateByExampleSelective(@Param("record") TblCloudAgentInfo record, @Param("example") TblCloudAgentInfoExample example);

    int updateByExample(@Param("record") TblCloudAgentInfo record, @Param("example") TblCloudAgentInfoExample example);

    int updateByPrimaryKeySelective(TblCloudAgentInfo record);

    int updateByPrimaryKey(TblCloudAgentInfo record);

    String selectWorkerIdByCloudId(@Param("cloudId")String cloudId);
}