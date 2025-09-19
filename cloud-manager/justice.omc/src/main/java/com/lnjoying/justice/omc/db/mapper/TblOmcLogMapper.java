package com.lnjoying.justice.omc.db.mapper;
import java.util.Date;

import com.lnjoying.justice.omc.db.model.TblOmcLog;
import com.lnjoying.justice.omc.db.model.TblOmcLogExample;
import com.lnjoying.justice.omc.domain.model.HourlyOperationLog;
import com.lnjoying.justice.schema.entity.omc.BpLastLoginTimeDTO;
import com.lnjoying.justice.schema.entity.omc.BpLoginDetailDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TblOmcLogMapper {
    long countByExample(TblOmcLogExample example);

    long deleteByExample(TblOmcLogExample example);

    int deleteByPrimaryKey(String id);

    int insert(TblOmcLog record);

    int insertSelective(TblOmcLog record);

    List<TblOmcLog> selectByExample(TblOmcLogExample example);

    TblOmcLog selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TblOmcLog record, @Param("example") TblOmcLogExample example);

    int updateByExample(@Param("record") TblOmcLog record, @Param("example") TblOmcLogExample example);

    int updateByPrimaryKeySelective(TblOmcLog record);

    int updateByPrimaryKey(TblOmcLog record);

    List<HourlyOperationLog> selectAllByCreatedAtBetweenEqualAndLevelAndBpIdAndUserId(@Param("minCreatedAt")Date minCreatedAt, @Param("maxCreatedAt")Date maxCreatedAt, @Param("level")String level, @Param("bpId")String bpId, @Param("userId")String userId);

    List<BpLastLoginTimeDTO> selectLoginLogsGroupByBpIdAndCreateTimeBetween(@Param("bpIds")List<String> bpIds, @Param("minCreateTime")Date minCreateTime,
                                                                            @Param("maxCreateTime")Date maxCreateTime, @Param("sortDirection")String sortDirection,
                                                                            @Param("userId")String userId, @Param("userName")String userName);

    List<BpLoginDetailDTO> selectLoginLogsByBpIdAndCreateTimeBetween(@Param("bpId")String bpId, @Param("userId")String userId, @Param("minCreateTime")Date minCreateTime,
                                                                     @Param("maxCreateTime")Date maxCreateTime,
                                                                     @Param("sortDirection")String sortDirection);
}