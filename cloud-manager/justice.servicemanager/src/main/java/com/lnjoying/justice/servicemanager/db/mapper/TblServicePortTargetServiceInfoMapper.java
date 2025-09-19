package com.lnjoying.justice.servicemanager.db.mapper;

import com.lnjoying.justice.servicemanager.db.model.TblServicePortTargetServiceInfo;
import com.lnjoying.justice.servicemanager.db.model.TblServicePortTargetServiceInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblServicePortTargetServiceInfoMapper {
    long countByExample(TblServicePortTargetServiceInfoExample example);

    int deleteByExample(TblServicePortTargetServiceInfoExample example);

    int deleteByPrimaryKey(String id);

    int insert(TblServicePortTargetServiceInfo record);

    int insertSelective(TblServicePortTargetServiceInfo record);

    List<TblServicePortTargetServiceInfo> selectByExample(TblServicePortTargetServiceInfoExample example);

    TblServicePortTargetServiceInfo selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TblServicePortTargetServiceInfo record, @Param("example") TblServicePortTargetServiceInfoExample example);

    int updateByExample(@Param("record") TblServicePortTargetServiceInfo record, @Param("example") TblServicePortTargetServiceInfoExample example);

    int updateByPrimaryKeySelective(TblServicePortTargetServiceInfo record);

    int updateByPrimaryKey(TblServicePortTargetServiceInfo record);
}