package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpOsSecuritygroups;
import com.lnjoying.justice.cmp.db.model.TblCmpOsSecuritygroupsExample;
import com.lnjoying.justice.cmp.db.model.TblCmpOsSecuritygroupsKey;
import java.util.List;
import java.util.Set;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.OS_SECURITYGROUP)
public interface TblCmpOsSecuritygroupsMapper {
    long countByExample(TblCmpOsSecuritygroupsExample example);

    int deleteByExample(TblCmpOsSecuritygroupsExample example);

    int deleteByPrimaryKey(TblCmpOsSecuritygroupsKey key);

    int insert(TblCmpOsSecuritygroups record);

    int insertSelective(TblCmpOsSecuritygroups record);

    List<TblCmpOsSecuritygroups> selectByExample(TblCmpOsSecuritygroupsExample example);

    TblCmpOsSecuritygroups selectByPrimaryKey(TblCmpOsSecuritygroupsKey key);

    int updateByExampleSelective(@Param("record") TblCmpOsSecuritygroups record, @Param("example") TblCmpOsSecuritygroupsExample example);

    int updateByExample(@Param("record") TblCmpOsSecuritygroups record, @Param("example") TblCmpOsSecuritygroupsExample example);

    int updateByPrimaryKeySelective(TblCmpOsSecuritygroups record);

    int updateByPrimaryKey(TblCmpOsSecuritygroups record);

    Set<String> getSecuritygroupIds(@Param("cloudId")String cloudId);

    List<TblCmpOsSecuritygroups> getTblCmpSecuritygroupsByServer(@Param("cloudId")String cloudId, @Param("instanceUuid")String instanceUuid);
}