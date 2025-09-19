package com.lnjoying.justice.servicemanager.db.mapper;

import com.lnjoying.justice.servicemanager.db.model.TblServiceGatewayInfo;
import com.lnjoying.justice.servicemanager.db.model.TblServiceGatewayInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblServiceGatewayInfoMapper {
    long countByExample(TblServiceGatewayInfoExample example);

    int deleteByExample(TblServiceGatewayInfoExample example);

    int deleteByPrimaryKey(String id);

    int insert(TblServiceGatewayInfo record);

    int insertSelective(TblServiceGatewayInfo record);

    List<TblServiceGatewayInfo> selectByExample(TblServiceGatewayInfoExample example);

    TblServiceGatewayInfo selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TblServiceGatewayInfo record, @Param("example") TblServiceGatewayInfoExample example);

    int updateByExample(@Param("record") TblServiceGatewayInfo record, @Param("example") TblServiceGatewayInfoExample example);

    int updateByPrimaryKeySelective(TblServiceGatewayInfo record);

    int updateByPrimaryKey(TblServiceGatewayInfo record);
}