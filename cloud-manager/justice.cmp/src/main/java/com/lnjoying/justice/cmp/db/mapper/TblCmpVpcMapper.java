package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpVpc;
import com.lnjoying.justice.cmp.db.model.TblCmpVpcExample;
import com.lnjoying.justice.cmp.db.model.TblCmpVpcKey;
import java.util.List;
import java.util.Set;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.NS_VPC)
public interface TblCmpVpcMapper {
    long countByExample(TblCmpVpcExample example);

    int deleteByExample(TblCmpVpcExample example);

    int deleteByPrimaryKey(TblCmpVpcKey key);

    int insert(TblCmpVpc record);

    int insertSelective(TblCmpVpc record);

    List<TblCmpVpc> selectByExample(TblCmpVpcExample example);

    TblCmpVpc selectByPrimaryKey(TblCmpVpcKey key);

    int updateByExampleSelective(@Param("record") TblCmpVpc record, @Param("example") TblCmpVpcExample example);

    int updateByExample(@Param("record") TblCmpVpc record, @Param("example") TblCmpVpcExample example);

    int updateByPrimaryKeySelective(TblCmpVpc record);

    int updateByPrimaryKey(TblCmpVpc record);

    Set<String> getVpcIds(@Param("cloudId")String cloudId);

    int updateByEeIdSelective(TblCmpVpc record);

    TblCmpVpc selectByEeId(@Param("cloudId") String cloudId, @Param("eeId") String eeId);
}