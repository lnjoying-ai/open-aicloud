package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpNicInfo;
import com.lnjoying.justice.cmp.db.model.TblCmpNicInfoExample;
import com.lnjoying.justice.cmp.db.model.TblCmpNicInfoKey;
import java.util.List;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.NS_NIC)
public interface TblCmpNicInfoMapper {
    long countByExample(TblCmpNicInfoExample example);

    int deleteByExample(TblCmpNicInfoExample example);

    int deleteByPrimaryKey(TblCmpNicInfoKey key);

    int insert(TblCmpNicInfo record);

    int insertSelective(TblCmpNicInfo record);

    List<TblCmpNicInfo> selectByExample(TblCmpNicInfoExample example);

    TblCmpNicInfo selectByPrimaryKey(TblCmpNicInfoKey key);

    int updateByExampleSelective(@Param("record") TblCmpNicInfo record, @Param("example") TblCmpNicInfoExample example);

    int updateByExample(@Param("record") TblCmpNicInfo record, @Param("example") TblCmpNicInfoExample example);

    int updateByPrimaryKeySelective(TblCmpNicInfo record);

    int updateByPrimaryKey(TblCmpNicInfo record);
}