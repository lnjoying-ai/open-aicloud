package com.lnjoying.justice.servicemanager.db.mapper;

import com.lnjoying.justice.servicemanager.db.model.TblServicePortInfo;
import com.lnjoying.justice.servicemanager.db.model.TblServicePortInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblServicePortInfoMapper {
    long countByExample(TblServicePortInfoExample example);

    int deleteByExample(TblServicePortInfoExample example);

    int deleteByPrimaryKey(String id);

    int insert(TblServicePortInfo record);

    int insertSelective(TblServicePortInfo record);

    List<TblServicePortInfo> selectByExample(TblServicePortInfoExample example);

    TblServicePortInfo selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TblServicePortInfo record, @Param("example") TblServicePortInfoExample example);

    int updateByExample(@Param("record") TblServicePortInfo record, @Param("example") TblServicePortInfoExample example);

    int updateByPrimaryKeySelective(TblServicePortInfo record);

    int updateByPrimaryKey(TblServicePortInfo record);
}