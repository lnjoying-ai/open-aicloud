package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.db.model.TblCmpBaremetalCluster;
import com.lnjoying.justice.cmp.db.model.TblCmpBaremetalClusterExample;
import com.lnjoying.justice.cmp.db.model.TblCmpBaremetalClusterKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblCmpBaremetalClusterMapper {
    long countByExample(TblCmpBaremetalClusterExample example);

    int deleteByExample(TblCmpBaremetalClusterExample example);

    int deleteByPrimaryKey(TblCmpBaremetalClusterKey key);

    int insert(TblCmpBaremetalCluster record);

    int insertSelective(TblCmpBaremetalCluster record);

    List<TblCmpBaremetalCluster> selectByExample(TblCmpBaremetalClusterExample example);

    TblCmpBaremetalCluster selectByPrimaryKey(TblCmpBaremetalClusterKey key);

    int updateByExampleSelective(@Param("record") TblCmpBaremetalCluster record, @Param("example") TblCmpBaremetalClusterExample example);

    int updateByExample(@Param("record") TblCmpBaremetalCluster record, @Param("example") TblCmpBaremetalClusterExample example);

    int updateByPrimaryKeySelective(TblCmpBaremetalCluster record);

    int updateByPrimaryKey(TblCmpBaremetalCluster record);
}