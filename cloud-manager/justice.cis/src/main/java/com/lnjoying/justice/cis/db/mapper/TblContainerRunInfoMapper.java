package com.lnjoying.justice.cis.db.mapper;

import com.lnjoying.justice.cis.db.model.TblContainerRunInfo;
import com.lnjoying.justice.cis.db.model.TblContainerRunInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblContainerRunInfoMapper {
    long countByExample(TblContainerRunInfoExample example);

    int deleteByExample(TblContainerRunInfoExample example);

    int deleteByPrimaryKey(String runId);

    int insert(TblContainerRunInfo record);

    int insertSelective(TblContainerRunInfo record);

    List<TblContainerRunInfo> selectByExample(TblContainerRunInfoExample example);

    TblContainerRunInfo selectByPrimaryKey(String runId);

    int updateByExampleSelective(@Param("record") TblContainerRunInfo record, @Param("example") TblContainerRunInfoExample example);

    int updateByExample(@Param("record") TblContainerRunInfo record, @Param("example") TblContainerRunInfoExample example);

    int updateByPrimaryKeySelective(TblContainerRunInfo record);

    int updateByPrimaryKey(TblContainerRunInfo record);
}