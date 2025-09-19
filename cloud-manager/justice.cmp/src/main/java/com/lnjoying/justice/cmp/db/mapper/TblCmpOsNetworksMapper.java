package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpOsNetworks;
import com.lnjoying.justice.cmp.db.model.TblCmpOsNetworksExample;
import com.lnjoying.justice.cmp.db.model.TblCmpOsNetworksKey;
import java.util.List;
import java.util.Set;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.OS_NETWORK)
public interface TblCmpOsNetworksMapper {
    long countByExample(TblCmpOsNetworksExample example);

    int deleteByExample(TblCmpOsNetworksExample example);

    int deleteByPrimaryKey(TblCmpOsNetworksKey key);

    int insert(TblCmpOsNetworks record);

    int insertSelective(TblCmpOsNetworks record);

    List<TblCmpOsNetworks> selectByExample(TblCmpOsNetworksExample example);

    TblCmpOsNetworks selectByPrimaryKey(TblCmpOsNetworksKey key);

    int updateByExampleSelective(@Param("record") TblCmpOsNetworks record, @Param("example") TblCmpOsNetworksExample example);

    int updateByExample(@Param("record") TblCmpOsNetworks record, @Param("example") TblCmpOsNetworksExample example);

    int updateByPrimaryKeySelective(TblCmpOsNetworks record);

    int updateByPrimaryKey(TblCmpOsNetworks record);

    Set<String> getNetworkIds(@Param("cloudId")String cloudId);

    List<TblCmpOsNetworks> selectByExampleWithShared(@Param("example")TblCmpOsNetworksExample example, @Param("projectId")String projectId, @Param("userId")String userId);
}