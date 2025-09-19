package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.db.model.TblCmpOsRouterroutes;
import com.lnjoying.justice.cmp.db.model.TblCmpOsRouterroutesExample;
import com.lnjoying.justice.cmp.db.model.TblCmpOsRouterroutesKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblCmpOsRouterroutesMapper {
    long countByExample(TblCmpOsRouterroutesExample example);

    int deleteByExample(TblCmpOsRouterroutesExample example);

    int deleteByPrimaryKey(TblCmpOsRouterroutesKey key);

    int insert(TblCmpOsRouterroutes record);

    int insertSelective(TblCmpOsRouterroutes record);

    List<TblCmpOsRouterroutes> selectByExample(TblCmpOsRouterroutesExample example);

    TblCmpOsRouterroutes selectByPrimaryKey(TblCmpOsRouterroutesKey key);

    int updateByExampleSelective(@Param("record") TblCmpOsRouterroutes record, @Param("example") TblCmpOsRouterroutesExample example);

    int updateByExample(@Param("record") TblCmpOsRouterroutes record, @Param("example") TblCmpOsRouterroutesExample example);

    int updateByPrimaryKeySelective(TblCmpOsRouterroutes record);

    int updateByPrimaryKey(TblCmpOsRouterroutes record);

    int updateRouterrouteEeStatusByRouterid(@Param("cloudId") String cloudId, @Param("routerId") String routerId, @Param("eeStatus") Integer eeStatus);
}