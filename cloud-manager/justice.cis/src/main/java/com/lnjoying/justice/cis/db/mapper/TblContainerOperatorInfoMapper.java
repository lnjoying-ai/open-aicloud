package com.lnjoying.justice.cis.db.mapper;

import com.lnjoying.justice.cis.db.model.TblContainerOperatorInfo;
import com.lnjoying.justice.cis.db.model.TblContainerOperatorInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblContainerOperatorInfoMapper {
    long countByExample(TblContainerOperatorInfoExample example);

    int deleteByExample(TblContainerOperatorInfoExample example);

    int deleteByPrimaryKey(String operId);

    int insert(TblContainerOperatorInfo record);

    int insertSelective(TblContainerOperatorInfo record);

    List<TblContainerOperatorInfo> selectByExample(TblContainerOperatorInfoExample example);

    TblContainerOperatorInfo selectByPrimaryKey(String operId);

    int updateByExampleSelective(@Param("record") TblContainerOperatorInfo record, @Param("example") TblContainerOperatorInfoExample example);

    int updateByExample(@Param("record") TblContainerOperatorInfo record, @Param("example") TblContainerOperatorInfoExample example);

    int updateByPrimaryKeySelective(TblContainerOperatorInfo record);

    int updateByPrimaryKey(TblContainerOperatorInfo record);
}