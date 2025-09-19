package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpPortMap;
import com.lnjoying.justice.cmp.db.model.TblCmpPortMapExample;
import com.lnjoying.justice.cmp.db.model.TblCmpPortMapKey;
import java.util.List;
import java.util.Set;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.NS_PORT_MAP)
public interface TblCmpPortMapMapper {
    long countByExample(TblCmpPortMapExample example);

    int deleteByExample(TblCmpPortMapExample example);

    int deleteByPrimaryKey(TblCmpPortMapKey key);

    int insert(TblCmpPortMap record);

    int insertSelective(TblCmpPortMap record);

    List<TblCmpPortMap> selectByExample(TblCmpPortMapExample example);

    TblCmpPortMap selectByPrimaryKey(TblCmpPortMapKey key);

    int updateByExampleSelective(@Param("record") TblCmpPortMap record, @Param("example") TblCmpPortMapExample example);

    int updateByExample(@Param("record") TblCmpPortMap record, @Param("example") TblCmpPortMapExample example);

    int updateByPrimaryKeySelective(TblCmpPortMap record);

    int updateByPrimaryKey(TblCmpPortMap record);

    Set<String> getPortMapIds(@Param("cloudId")String cloudId, @Param("eipMapId")String eipMapId);
}