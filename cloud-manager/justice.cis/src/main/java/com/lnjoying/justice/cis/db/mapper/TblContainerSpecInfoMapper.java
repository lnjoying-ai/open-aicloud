package com.lnjoying.justice.cis.db.mapper;

import com.lnjoying.justice.cis.common.constant.CisResources;
import com.lnjoying.justice.cis.db.model.TblContainerSpecInfo;
import com.lnjoying.justice.cis.db.model.TblContainerSpecInfoExample;
import java.util.List;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(CisResources.CONTAINER_SPEC_INFO)
public interface TblContainerSpecInfoMapper {
    long countByExample(TblContainerSpecInfoExample example);

    int deleteByExample(TblContainerSpecInfoExample example);

    int deleteByPrimaryKey(String specId);

    int insert(TblContainerSpecInfo record);

    int insertSelective(TblContainerSpecInfo record);

    List<TblContainerSpecInfo> selectByExample(TblContainerSpecInfoExample example);

    TblContainerSpecInfo selectByPrimaryKey(String specId);

    int updateByExampleSelective(@Param("record") TblContainerSpecInfo record, @Param("example") TblContainerSpecInfoExample example);

    int updateByExample(@Param("record") TblContainerSpecInfo record, @Param("example") TblContainerSpecInfoExample example);

    int updateByPrimaryKeySelective(TblContainerSpecInfo record);

    int updateByPrimaryKey(TblContainerSpecInfo record);
}