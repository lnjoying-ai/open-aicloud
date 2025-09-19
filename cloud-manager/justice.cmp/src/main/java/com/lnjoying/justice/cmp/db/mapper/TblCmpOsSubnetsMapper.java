package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpOsSubnets;
import com.lnjoying.justice.cmp.db.model.TblCmpOsSubnetsExample;
import com.lnjoying.justice.cmp.db.model.TblCmpOsSubnetsKey;
import java.util.List;
import java.util.Set;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.OS_SUBNET)
public interface TblCmpOsSubnetsMapper {
    long countByExample(TblCmpOsSubnetsExample example);

    int deleteByExample(TblCmpOsSubnetsExample example);

    int deleteByPrimaryKey(TblCmpOsSubnetsKey key);

    int insert(TblCmpOsSubnets record);

    int insertSelective(TblCmpOsSubnets record);

    List<TblCmpOsSubnets> selectByExample(TblCmpOsSubnetsExample example);

    TblCmpOsSubnets selectByPrimaryKey(TblCmpOsSubnetsKey key);

    int updateByExampleSelective(@Param("record") TblCmpOsSubnets record, @Param("example") TblCmpOsSubnetsExample example);

    int updateByExample(@Param("record") TblCmpOsSubnets record, @Param("example") TblCmpOsSubnetsExample example);

    int updateByPrimaryKeySelective(TblCmpOsSubnets record);

    int updateByPrimaryKey(TblCmpOsSubnets record);

    Set<String> getSubnetIds(@Param("cloudId")String cloudId);

    List<TblCmpOsSubnets> selectByExampleWithShared(@Param("example")TblCmpOsSubnetsExample example, @Param("cloudId")String cloudId, @Param("projectId")String projectId, @Param("userId")String userId);
}