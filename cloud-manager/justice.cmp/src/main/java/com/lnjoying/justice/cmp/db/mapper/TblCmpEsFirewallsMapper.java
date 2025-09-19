package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.db.model.TblCmpEsFirewalls;
import com.lnjoying.justice.cmp.db.model.TblCmpEsFirewallsExample;
import com.lnjoying.justice.cmp.db.model.TblCmpEsFirewallsKey;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

public interface TblCmpEsFirewallsMapper {
    long countByExample(TblCmpEsFirewallsExample example);

    int deleteByExample(TblCmpEsFirewallsExample example);

    int deleteByPrimaryKey(TblCmpEsFirewallsKey key);

    int insert(TblCmpEsFirewalls record);

    int insertSelective(TblCmpEsFirewalls record);

    List<TblCmpEsFirewalls> selectByExample(TblCmpEsFirewallsExample example);

    TblCmpEsFirewalls selectByPrimaryKey(TblCmpEsFirewallsKey key);

    int updateByExampleSelective(@Param("record") TblCmpEsFirewalls record, @Param("example") TblCmpEsFirewallsExample example);

    int updateByExample(@Param("record") TblCmpEsFirewalls record, @Param("example") TblCmpEsFirewallsExample example);

    int updateByPrimaryKeySelective(TblCmpEsFirewalls record);

    int updateByPrimaryKey(TblCmpEsFirewalls record);

    Set<String> getFirewallIds(@Param("cloudId")String cloudId);
}