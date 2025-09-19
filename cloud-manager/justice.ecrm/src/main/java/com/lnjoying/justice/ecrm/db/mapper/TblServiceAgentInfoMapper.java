package com.lnjoying.justice.ecrm.db.mapper;

import com.lnjoying.justice.ecrm.db.model.TblServiceAgentInfo;
import com.lnjoying.justice.ecrm.db.model.TblServiceAgentInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblServiceAgentInfoMapper {
    long countByExample(TblServiceAgentInfoExample example);

    int deleteByExample(TblServiceAgentInfoExample example);

    int deleteByPrimaryKey(String agentId);

    int insert(TblServiceAgentInfo record);

    int insertSelective(TblServiceAgentInfo record);

    List<TblServiceAgentInfo> selectByExample(TblServiceAgentInfoExample example);

    TblServiceAgentInfo selectByPrimaryKey(String agentId);

    int updateByExampleSelective(@Param("record") TblServiceAgentInfo record, @Param("example") TblServiceAgentInfoExample example);

    int updateByExample(@Param("record") TblServiceAgentInfo record, @Param("example") TblServiceAgentInfoExample example);

    int updateByPrimaryKeySelective(TblServiceAgentInfo record);

    int updateByPrimaryKey(TblServiceAgentInfo record);

    String selectAgentIdBySiteId(@Param("siteId") String siteId);
}