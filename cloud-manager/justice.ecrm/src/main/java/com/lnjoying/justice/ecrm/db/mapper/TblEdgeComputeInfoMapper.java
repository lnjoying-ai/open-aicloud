package com.lnjoying.justice.ecrm.db.mapper;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import com.lnjoying.justice.ecrm.db.model.TblEdgeComputeInfo;
import com.lnjoying.justice.ecrm.db.model.TblEdgeComputeInfoExample;
import java.util.List;

import com.lnjoying.justice.ecrm.domain.EdgeStatusCounts;
import com.lnjoying.justice.schema.entity.stat.StatusMetrics;
import com.lnjoying.justice.schema.service.ecrm.RegionResourceService;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = "edge", method = "selectByPrimaryKeyWithLabelValue")
public interface TblEdgeComputeInfoMapper {
    long countByExample(TblEdgeComputeInfoExample example);

    int deleteByExample(TblEdgeComputeInfoExample example);

    int deleteByPrimaryKey(String nodeId);

    int insert(TblEdgeComputeInfo record);

    int insertSelective(TblEdgeComputeInfo record);

    List<TblEdgeComputeInfo> selectByExample(TblEdgeComputeInfoExample example);

    TblEdgeComputeInfo selectByPrimaryKey(String nodeId);

    int updateByExampleSelective(@Param("record") TblEdgeComputeInfo record, @Param("example") TblEdgeComputeInfoExample example);

    int updateByExample(@Param("record") TblEdgeComputeInfo record, @Param("example") TblEdgeComputeInfoExample example);

    int updateByPrimaryKeySelective(TblEdgeComputeInfo record);

    int updateByPrimaryKey(TblEdgeComputeInfo record);


    RegionResourceService.NodeInfo selectByNodeId(@Param("nodeId") String nodeId);

    Integer countByBpIdAndUserId(@Param("bpId")String bpId,@Param("userId")String userId);

    TblEdgeComputeInfo selectByPrimaryKeyWithLabelValue(String nodeId);

    List<StatusMetrics> getEdgeStatusMetrics(@Param("userId")String userId, @Param("bpId")String bpId);

    List<EdgeStatusCounts> countByOnlineStatus();


}