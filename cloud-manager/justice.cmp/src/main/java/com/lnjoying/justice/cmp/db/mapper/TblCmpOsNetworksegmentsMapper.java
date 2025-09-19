package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.db.model.TblCmpOsNetworksegments;
import com.lnjoying.justice.cmp.db.model.TblCmpOsNetworksegmentsExample;
import com.lnjoying.justice.cmp.db.model.TblCmpOsNetworksegmentsKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblCmpOsNetworksegmentsMapper {
    long countByExample(TblCmpOsNetworksegmentsExample example);

    int deleteByExample(TblCmpOsNetworksegmentsExample example);

    int deleteByPrimaryKey(TblCmpOsNetworksegmentsKey key);

    int insert(TblCmpOsNetworksegments record);

    int insertSelective(TblCmpOsNetworksegments record);

    List<TblCmpOsNetworksegments> selectByExample(TblCmpOsNetworksegmentsExample example);

    TblCmpOsNetworksegments selectByPrimaryKey(TblCmpOsNetworksegmentsKey key);

    int updateByExampleSelective(@Param("record") TblCmpOsNetworksegments record, @Param("example") TblCmpOsNetworksegmentsExample example);

    int updateByExample(@Param("record") TblCmpOsNetworksegments record, @Param("example") TblCmpOsNetworksegmentsExample example);

    int updateByPrimaryKeySelective(TblCmpOsNetworksegments record);

    int updateByPrimaryKey(TblCmpOsNetworksegments record);
}