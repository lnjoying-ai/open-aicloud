package com.lnjoying.justice.aos.db.mapper;

import com.lnjoying.justice.aos.db.model.TblCfgdataStackInfo;
import com.lnjoying.justice.aos.db.model.TblCfgdataStackInfoExample;
import com.lnjoying.justice.aos.db.model.TblStackInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TblCfgdataStackInfoMapper {
    long countByExample(TblCfgdataStackInfoExample example);

    int deleteByExample(TblCfgdataStackInfoExample example);

    int deleteByPrimaryKey(String cfgVolumeId);

    int insert(TblCfgdataStackInfo record);

    int insertSelective(TblCfgdataStackInfo record);

    List<TblCfgdataStackInfo> selectByExample(TblCfgdataStackInfoExample example);

    TblCfgdataStackInfo selectByPrimaryKey(String cfgVolumeId);

    int updateByExampleSelective(@Param("record") TblCfgdataStackInfo record, @Param("example") TblCfgdataStackInfoExample example);

    int updateByExample(@Param("record") TblCfgdataStackInfo record, @Param("example") TblCfgdataStackInfoExample example);

    int updateByPrimaryKeySelective(TblCfgdataStackInfo record);

    int updateByPrimaryKey(TblCfgdataStackInfo record);

    List<TblStackInfo> selectCfgStacks(@Param("userId")String userId, @Param("dataId")String dataId, @Param("group")String group, @Param("notInStatusCollection")List<Integer> notInStatusCollection);
}