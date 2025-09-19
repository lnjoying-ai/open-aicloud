package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpInstanceGroup;
import com.lnjoying.justice.cmp.db.model.TblCmpInstanceGroupExample;
import com.lnjoying.justice.cmp.db.model.TblCmpInstanceGroupKey;
import java.util.List;
import java.util.Set;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.NS_INSTANCE_GROUP)
public interface TblCmpInstanceGroupMapper {
    long countByExample(TblCmpInstanceGroupExample example);

    int deleteByExample(TblCmpInstanceGroupExample example);

    int deleteByPrimaryKey(TblCmpInstanceGroupKey key);

    int insert(TblCmpInstanceGroup record);

    int insertSelective(TblCmpInstanceGroup record);

    List<TblCmpInstanceGroup> selectByExample(TblCmpInstanceGroupExample example);

    TblCmpInstanceGroup selectByPrimaryKey(TblCmpInstanceGroupKey key);

    int updateByExampleSelective(@Param("record") TblCmpInstanceGroup record, @Param("example") TblCmpInstanceGroupExample example);

    int updateByExample(@Param("record") TblCmpInstanceGroup record, @Param("example") TblCmpInstanceGroupExample example);

    int updateByPrimaryKeySelective(TblCmpInstanceGroup record);

    int updateByPrimaryKey(TblCmpInstanceGroup record);

    Set<String> getInstanceGroupIds(@Param("cloudId")String cloudId);
}