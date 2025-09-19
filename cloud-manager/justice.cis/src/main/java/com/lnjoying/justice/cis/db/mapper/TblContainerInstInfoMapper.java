package com.lnjoying.justice.cis.db.mapper;

import com.lnjoying.justice.cis.common.constant.CisResources;
import com.lnjoying.justice.cis.db.model.TblContainerInstInfo;
import com.lnjoying.justice.cis.db.model.TblContainerInstInfoExample;

import java.util.Collection;
import java.util.List;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import com.lnjoying.justice.schema.entity.stat.StatusMetrics;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CisResources.CONTAINER)
public interface TblContainerInstInfoMapper {
    long countByExample(TblContainerInstInfoExample example);

    int deleteByExample(TblContainerInstInfoExample example);

    int deleteByPrimaryKey(String instId);

    int insert(TblContainerInstInfo record);

    int insertSelective(TblContainerInstInfo record);

    List<TblContainerInstInfo> selectByExample(TblContainerInstInfoExample example);

    TblContainerInstInfo selectByPrimaryKey(String instId);

    int updateByExampleSelective(@Param("record") TblContainerInstInfo record, @Param("example") TblContainerInstInfoExample example);

    int updateByExample(@Param("record") TblContainerInstInfo record, @Param("example") TblContainerInstInfoExample example);

    int updateByPrimaryKeySelective(TblContainerInstInfo record);

    int updateByPrimaryKey(TblContainerInstInfo record);

    List<StatusMetrics> getContainerInstanceStatusMetrics(@Param("statusExcludeCollection") Collection<Integer> statusExcludeCollection, @Param("bpId")String bpId,
                                                  @Param("userId")String userId);

    TblContainerInstInfo selectByInstIdOrRefId(@Param("uniqueId")String uniqueId);


}