package com.lnjoying.justice.cluster.manager.db.mapper;

import com.lnjoying.justice.cluster.manager.common.ClsResources;
import com.lnjoying.justice.cluster.manager.db.model.TblClusterInfo;
import com.lnjoying.justice.cluster.manager.db.model.TblClusterInfoExample;
import java.util.List;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import com.lnjoying.justice.schema.entity.stat.StatusMetrics;
import io.swagger.annotations.ApiModel;
import org.apache.ibatis.annotations.Param;

@ApiModel(value = "TblClusterInfo")
@MapperModel(ClsResources.CLUSTER_INFO)
public interface TblClusterInfoMapper {
    long countByExample(TblClusterInfoExample example);

    int deleteByExample(TblClusterInfoExample example);

    int deleteByPrimaryKey(String id);

    int insert(TblClusterInfo record);

    int insertSelective(TblClusterInfo record);

    List<TblClusterInfo> selectByExample(TblClusterInfoExample example);

    TblClusterInfo selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TblClusterInfo record, @Param("example") TblClusterInfoExample example);

    int updateByExample(@Param("record") TblClusterInfo record, @Param("example") TblClusterInfoExample example);

    int updateByPrimaryKeySelective(TblClusterInfo record);

    int updateByPrimaryKey(TblClusterInfo record);

    List<String> selectClusterNameById(@Param("id")String id);

    List<StatusMetrics> getClusterStatusMetrics(@Param("userId")String userId, @Param("bpId")String bpId);
}