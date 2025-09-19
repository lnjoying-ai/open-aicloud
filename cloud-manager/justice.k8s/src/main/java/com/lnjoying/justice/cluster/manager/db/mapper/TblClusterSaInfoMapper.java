package com.lnjoying.justice.cluster.manager.db.mapper;

import com.lnjoying.justice.cluster.manager.db.model.TblClusterSaInfo;
import com.lnjoying.justice.cluster.manager.db.model.TblClusterSaInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblClusterSaInfoMapper {
    long countByExample(TblClusterSaInfoExample example);

    int deleteByExample(TblClusterSaInfoExample example);

    int deleteByPrimaryKey(String clusterId);

    int insert(TblClusterSaInfo record);

    int insertSelective(TblClusterSaInfo record);

    List<TblClusterSaInfo> selectByExample(TblClusterSaInfoExample example);

    TblClusterSaInfo selectByPrimaryKey(String clusterId);

    int updateByExampleSelective(@Param("record") TblClusterSaInfo record, @Param("example") TblClusterSaInfoExample example);

    int updateByExample(@Param("record") TblClusterSaInfo record, @Param("example") TblClusterSaInfoExample example);

    int updateByPrimaryKeySelective(TblClusterSaInfo record);

    int updateByPrimaryKey(TblClusterSaInfo record);

    String selectSecretTokenByClusterId(@Param("clusterId")String clusterId);

}