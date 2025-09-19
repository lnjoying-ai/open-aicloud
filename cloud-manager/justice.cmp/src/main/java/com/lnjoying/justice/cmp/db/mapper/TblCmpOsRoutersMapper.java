package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpEsRouters;
import com.lnjoying.justice.cmp.db.model.TblCmpOsRouters;
import com.lnjoying.justice.cmp.db.model.TblCmpOsRoutersExample;
import com.lnjoying.justice.cmp.db.model.TblCmpOsRoutersKey;
import java.util.List;
import java.util.Set;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.OS_ROUTER)
public interface TblCmpOsRoutersMapper {
    long countByExample(TblCmpOsRoutersExample example);

    int deleteByExample(TblCmpOsRoutersExample example);

    int deleteByPrimaryKey(TblCmpOsRoutersKey key);

    int insert(TblCmpOsRouters record);

    int insertSelective(TblCmpOsRouters record);

    List<TblCmpOsRouters> selectByExample(TblCmpOsRoutersExample example);

    TblCmpOsRouters selectByPrimaryKey(TblCmpOsRoutersKey key);

    int updateByExampleSelective(@Param("record") TblCmpOsRouters record, @Param("example") TblCmpOsRoutersExample example);

    int updateByExample(@Param("record") TblCmpOsRouters record, @Param("example") TblCmpOsRoutersExample example);

    int updateByPrimaryKeySelective(TblCmpOsRouters record);

    int updateByPrimaryKey(TblCmpOsRouters record);

    Set<String> getRouterIds(@Param("cloudId")String cloudId);

    int updateByPrimaryKeyEeSelective(TblCmpOsRouters record);

    List<TblCmpEsRouters> selectByExampleWithFirewall(@Param("example")TblCmpOsRoutersExample example, @Param("cloudId")String cloudId, @Param("firewallId")String firewallId);
}