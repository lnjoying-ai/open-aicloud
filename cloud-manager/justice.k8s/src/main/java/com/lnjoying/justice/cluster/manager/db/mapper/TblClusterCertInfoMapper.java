package com.lnjoying.justice.cluster.manager.db.mapper;

import com.lnjoying.justice.cluster.manager.db.model.TblClusterCertInfo;
import com.lnjoying.justice.cluster.manager.db.model.TblClusterCertInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblClusterCertInfoMapper {
    long countByExample(TblClusterCertInfoExample example);

    int deleteByExample(TblClusterCertInfoExample example);

    int deleteByPrimaryKey(String clusterId);

    int insert(TblClusterCertInfo record);

    int insertSelective(TblClusterCertInfo record);

    List<TblClusterCertInfo> selectByExample(TblClusterCertInfoExample example);

    TblClusterCertInfo selectByPrimaryKey(String clusterId);

    int updateByExampleSelective(@Param("record") TblClusterCertInfo record, @Param("example") TblClusterCertInfoExample example);

    int updateByExample(@Param("record") TblClusterCertInfo record, @Param("example") TblClusterCertInfoExample example);

    int updateByPrimaryKeySelective(TblClusterCertInfo record);

    int updateByPrimaryKey(TblClusterCertInfo record);
}