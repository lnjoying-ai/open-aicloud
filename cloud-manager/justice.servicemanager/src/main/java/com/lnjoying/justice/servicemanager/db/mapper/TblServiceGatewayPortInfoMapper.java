package com.lnjoying.justice.servicemanager.db.mapper;

import com.lnjoying.justice.servicemanager.db.model.TblServiceGatewayPortInfo;
import com.lnjoying.justice.servicemanager.db.model.TblServiceGatewayPortInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblServiceGatewayPortInfoMapper {
    long countByExample(TblServiceGatewayPortInfoExample example);

    int deleteByExample(TblServiceGatewayPortInfoExample example);

    int deleteByPrimaryKey(String id);

    int insert(TblServiceGatewayPortInfo record);

    int insertSelective(TblServiceGatewayPortInfo record);

    List<TblServiceGatewayPortInfo> selectByExample(TblServiceGatewayPortInfoExample example);

    TblServiceGatewayPortInfo selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TblServiceGatewayPortInfo record, @Param("example") TblServiceGatewayPortInfoExample example);

    int updateByExample(@Param("record") TblServiceGatewayPortInfo record, @Param("example") TblServiceGatewayPortInfoExample example);

    int updateByPrimaryKeySelective(TblServiceGatewayPortInfo record);

    int updateByPrimaryKey(TblServiceGatewayPortInfo record);
}