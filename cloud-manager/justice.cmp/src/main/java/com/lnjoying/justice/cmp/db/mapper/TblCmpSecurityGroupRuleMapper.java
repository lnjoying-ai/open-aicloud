package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpSecurityGroupRule;
import com.lnjoying.justice.cmp.db.model.TblCmpSecurityGroupRuleExample;
import com.lnjoying.justice.cmp.db.model.TblCmpSecurityGroupRuleKey;
import java.util.List;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.NS_SECURITY_GROUP_RULE)
public interface TblCmpSecurityGroupRuleMapper {
    long countByExample(TblCmpSecurityGroupRuleExample example);

    int deleteByExample(TblCmpSecurityGroupRuleExample example);

    int deleteByPrimaryKey(TblCmpSecurityGroupRuleKey key);

    int insert(TblCmpSecurityGroupRule record);

    int insertSelective(TblCmpSecurityGroupRule record);

    List<TblCmpSecurityGroupRule> selectByExample(TblCmpSecurityGroupRuleExample example);

    TblCmpSecurityGroupRule selectByPrimaryKey(TblCmpSecurityGroupRuleKey key);

    int updateByExampleSelective(@Param("record") TblCmpSecurityGroupRule record, @Param("example") TblCmpSecurityGroupRuleExample example);

    int updateByExample(@Param("record") TblCmpSecurityGroupRule record, @Param("example") TblCmpSecurityGroupRuleExample example);

    int updateByPrimaryKeySelective(TblCmpSecurityGroupRule record);

    int updateByPrimaryKey(TblCmpSecurityGroupRule record);
}