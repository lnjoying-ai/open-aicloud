package com.lnjoying.justice.servicemanager.db.mapper;

import com.lnjoying.justice.servicemanager.db.model.TblServiceGatewayPortAllocation;
import com.lnjoying.justice.servicemanager.db.model.TblServiceGatewayPortAllocationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblServiceGatewayPortAllocationMapper {
    long countByExample(TblServiceGatewayPortAllocationExample example);

    int deleteByExample(TblServiceGatewayPortAllocationExample example);

    int deleteByPrimaryKey(String id);

    int insert(TblServiceGatewayPortAllocation record);

    int insertSelective(TblServiceGatewayPortAllocation record);

    List<TblServiceGatewayPortAllocation> selectByExample(TblServiceGatewayPortAllocationExample example);

    TblServiceGatewayPortAllocation selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TblServiceGatewayPortAllocation record, @Param("example") TblServiceGatewayPortAllocationExample example);

    int updateByExample(@Param("record") TblServiceGatewayPortAllocation record, @Param("example") TblServiceGatewayPortAllocationExample example);

    int updateByPrimaryKeySelective(TblServiceGatewayPortAllocation record);

    int updateByPrimaryKey(TblServiceGatewayPortAllocation record);

    List<TblServiceGatewayPortAllocation> selectByServiceGatewayId(@Param("serviceGatewayId")String serviceGatewayId);

    List<TblServiceGatewayPortAllocation> selectByExampleLeftJoinPortRange(TblServiceGatewayPortAllocationExample example);
}