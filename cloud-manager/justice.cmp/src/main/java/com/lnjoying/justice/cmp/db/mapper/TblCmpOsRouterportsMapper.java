package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.db.model.TblCmpOsRouterports;
import com.lnjoying.justice.cmp.db.model.TblCmpOsRouterportsExample;
import com.lnjoying.justice.cmp.db.model.TblCmpOsRouterportsKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblCmpOsRouterportsMapper {
    long countByExample(TblCmpOsRouterportsExample example);

    int deleteByExample(TblCmpOsRouterportsExample example);

    int deleteByPrimaryKey(TblCmpOsRouterportsKey key);

    int insert(TblCmpOsRouterports record);

    int insertSelective(TblCmpOsRouterports record);

    List<TblCmpOsRouterports> selectByExample(TblCmpOsRouterportsExample example);

    TblCmpOsRouterports selectByPrimaryKey(TblCmpOsRouterportsKey key);

    int updateByExampleSelective(@Param("record") TblCmpOsRouterports record, @Param("example") TblCmpOsRouterportsExample example);

    int updateByExample(@Param("record") TblCmpOsRouterports record, @Param("example") TblCmpOsRouterportsExample example);

    int updateByPrimaryKeySelective(TblCmpOsRouterports record);

    int updateByPrimaryKey(TblCmpOsRouterports record);

    int updateRouterportEeStatusByRouterid(@Param("cloudId") String cloudId, @Param("routerId") String routerId, @Param("eeStatus") Integer eeStatus);
}