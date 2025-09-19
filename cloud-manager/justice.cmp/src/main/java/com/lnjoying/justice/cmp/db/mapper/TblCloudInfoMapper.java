package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCloudInfo;
import com.lnjoying.justice.cmp.db.model.TblCloudInfoExample;
import java.util.List;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import com.lnjoying.justice.schema.entity.stat.StatusMetrics;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.CLOUD)
public interface TblCloudInfoMapper {
    long countByExample(TblCloudInfoExample example);

    int deleteByExample(TblCloudInfoExample example);

    int deleteByPrimaryKey(String cloudId);

    int insert(TblCloudInfo record);

    int insertSelective(TblCloudInfo record);

    List<TblCloudInfo> selectByExample(TblCloudInfoExample example);

    TblCloudInfo selectByPrimaryKey(String cloudId);

    int updateByExampleSelective(@Param("record") TblCloudInfo record, @Param("example") TblCloudInfoExample example);

    int updateByExample(@Param("record") TblCloudInfo record, @Param("example") TblCloudInfoExample example);

    int updateByPrimaryKeySelective(TblCloudInfo record);

    int updateByPrimaryKey(TblCloudInfo record);

    List<StatusMetrics> getCloudStatusMetrics(@Param("userId")String userId, @Param("bpId")String bpId);
}