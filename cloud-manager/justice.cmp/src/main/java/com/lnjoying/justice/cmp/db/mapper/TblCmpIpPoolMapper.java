package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpIpPool;
import com.lnjoying.justice.cmp.db.model.TblCmpIpPoolExample;
import com.lnjoying.justice.cmp.db.model.TblCmpIpPoolKey;
import java.util.List;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.NS_IP_POOL)
public interface TblCmpIpPoolMapper {
    long countByExample(TblCmpIpPoolExample example);

    int deleteByExample(TblCmpIpPoolExample example);

    int deleteByPrimaryKey(TblCmpIpPoolKey key);

    int insert(TblCmpIpPool record);

    int insertSelective(TblCmpIpPool record);

    List<TblCmpIpPool> selectByExample(TblCmpIpPoolExample example);

    TblCmpIpPool selectByPrimaryKey(TblCmpIpPoolKey key);

    int updateByExampleSelective(@Param("record") TblCmpIpPool record, @Param("example") TblCmpIpPoolExample example);

    int updateByExample(@Param("record") TblCmpIpPool record, @Param("example") TblCmpIpPoolExample example);

    int updateByPrimaryKeySelective(TblCmpIpPool record);

    int updateByPrimaryKey(TblCmpIpPool record);
}