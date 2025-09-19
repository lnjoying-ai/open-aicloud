package com.lnjoying.justice.scheduler.db.mapper;

import com.lnjoying.justice.scheduler.db.model.TblSchedEdgeMonopoly;
import com.lnjoying.justice.scheduler.db.model.TblSchedEdgeMonopolyExample;
import com.lnjoying.justice.scheduler.db.model.TblSchedEdgeMonopolyKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblSchedEdgeMonopolyMapper {
    long countByExample(TblSchedEdgeMonopolyExample example);

    int deleteByExample(TblSchedEdgeMonopolyExample example);

    int deleteByPrimaryKey(TblSchedEdgeMonopolyKey key);

    int insert(TblSchedEdgeMonopoly record);

    int insertSelective(TblSchedEdgeMonopoly record);

    List<TblSchedEdgeMonopoly> selectByExample(TblSchedEdgeMonopolyExample example);

    TblSchedEdgeMonopoly selectByPrimaryKey(TblSchedEdgeMonopolyKey key);

    int updateByExampleSelective(@Param("record") TblSchedEdgeMonopoly record, @Param("example") TblSchedEdgeMonopolyExample example);

    int updateByExample(@Param("record") TblSchedEdgeMonopoly record, @Param("example") TblSchedEdgeMonopolyExample example);

    int updateByPrimaryKeySelective(TblSchedEdgeMonopoly record);

    int updateByPrimaryKey(TblSchedEdgeMonopoly record);
}