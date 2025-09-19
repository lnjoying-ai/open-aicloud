package com.lnjoying.justice.omc.db.mapper;

import com.lnjoying.justice.omc.db.model.TblOmcEvent;
import com.lnjoying.justice.omc.db.model.TblOmcEventExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TblOmcEventMapper {
    long countByExample(TblOmcEventExample example);

    int deleteByExample(TblOmcEventExample example);

    int deleteByPrimaryKey(String id);

    int insert(TblOmcEvent record);

    int insertSelective(TblOmcEvent record);

    List<TblOmcEvent> selectByExample(TblOmcEventExample example);

    TblOmcEvent selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TblOmcEvent record, @Param("example") TblOmcEventExample example);

    int updateByExample(@Param("record") TblOmcEvent record, @Param("example") TblOmcEventExample example);

    int updateByPrimaryKeySelective(TblOmcEvent record);

    int updateByPrimaryKey(TblOmcEvent record);
}