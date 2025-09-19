package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpOsIpallocationpools;
import com.lnjoying.justice.cmp.db.model.TblCmpOsIpallocationpoolsExample;
import com.lnjoying.justice.cmp.db.model.TblCmpOsIpallocationpoolsKey;
import java.util.List;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.OS_IPALLOCATIONPOOL)
public interface TblCmpOsIpallocationpoolsMapper {
    long countByExample(TblCmpOsIpallocationpoolsExample example);

    int deleteByExample(TblCmpOsIpallocationpoolsExample example);

    int deleteByPrimaryKey(TblCmpOsIpallocationpoolsKey key);

    int insert(TblCmpOsIpallocationpools record);

    int insertSelective(TblCmpOsIpallocationpools record);

    List<TblCmpOsIpallocationpools> selectByExample(TblCmpOsIpallocationpoolsExample example);

    TblCmpOsIpallocationpools selectByPrimaryKey(TblCmpOsIpallocationpoolsKey key);

    int updateByExampleSelective(@Param("record") TblCmpOsIpallocationpools record, @Param("example") TblCmpOsIpallocationpoolsExample example);

    int updateByExample(@Param("record") TblCmpOsIpallocationpools record, @Param("example") TblCmpOsIpallocationpoolsExample example);

    int updateByPrimaryKeySelective(TblCmpOsIpallocationpools record);

    int updateByPrimaryKey(TblCmpOsIpallocationpools record);
}