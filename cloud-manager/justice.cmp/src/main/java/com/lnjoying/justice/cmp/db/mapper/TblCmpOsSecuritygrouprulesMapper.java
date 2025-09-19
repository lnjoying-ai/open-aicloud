package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpOsSecuritygrouprules;
import com.lnjoying.justice.cmp.db.model.TblCmpOsSecuritygrouprulesExample;
import com.lnjoying.justice.cmp.db.model.TblCmpOsSecuritygrouprulesKey;
import java.util.List;
import java.util.Set;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.OS_SECURITYGROUP_RULE)
public interface TblCmpOsSecuritygrouprulesMapper {
    long countByExample(TblCmpOsSecuritygrouprulesExample example);

    int deleteByExample(TblCmpOsSecuritygrouprulesExample example);

    int deleteByPrimaryKey(TblCmpOsSecuritygrouprulesKey key);

    int insert(TblCmpOsSecuritygrouprules record);

    int insertSelective(TblCmpOsSecuritygrouprules record);

    List<TblCmpOsSecuritygrouprules> selectByExample(TblCmpOsSecuritygrouprulesExample example);

    TblCmpOsSecuritygrouprules selectByPrimaryKey(TblCmpOsSecuritygrouprulesKey key);

    int updateByExampleSelective(@Param("record") TblCmpOsSecuritygrouprules record, @Param("example") TblCmpOsSecuritygrouprulesExample example);

    int updateByExample(@Param("record") TblCmpOsSecuritygrouprules record, @Param("example") TblCmpOsSecuritygrouprulesExample example);

    int updateByPrimaryKeySelective(TblCmpOsSecuritygrouprules record);

    int updateByPrimaryKey(TblCmpOsSecuritygrouprules record);

    Set<String> getSecuritygroupruleIds(@Param("cloudId")String cloudId);

    Set<String> getSecuritygroupruleIdsBySecurityGroup(@Param("cloudId")String cloudId, @Param("securityGroupId")String securityGroupId);
}