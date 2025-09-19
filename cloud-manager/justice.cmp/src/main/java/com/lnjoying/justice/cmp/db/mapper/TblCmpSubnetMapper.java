package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpSubnet;
import com.lnjoying.justice.cmp.db.model.TblCmpSubnetExample;
import com.lnjoying.justice.cmp.db.model.TblCmpSubnetKey;
import java.util.List;
import java.util.Set;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.NS_SUBNET)
public interface TblCmpSubnetMapper {
    long countByExample(TblCmpSubnetExample example);

    int deleteByExample(TblCmpSubnetExample example);

    int deleteByPrimaryKey(TblCmpSubnetKey key);

    int insert(TblCmpSubnet record);

    int insertSelective(TblCmpSubnet record);

    List<TblCmpSubnet> selectByExample(TblCmpSubnetExample example);

    TblCmpSubnet selectByPrimaryKey(TblCmpSubnetKey key);

    int updateByExampleSelective(@Param("record") TblCmpSubnet record, @Param("example") TblCmpSubnetExample example);

    int updateByExample(@Param("record") TblCmpSubnet record, @Param("example") TblCmpSubnetExample example);

    int updateByPrimaryKeySelective(TblCmpSubnet record);

    int updateByPrimaryKey(TblCmpSubnet record);

    Set<String> getSubnetIds(@Param("cloudId")String cloudId);

    int updateByEeIdSelective(TblCmpSubnet record);

    TblCmpSubnet selectByEeId(@Param("cloudId") String cloudId, @Param("eeId") String eeId);
}