package com.lnjoying.justice.ecrm.db.mapper;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import com.lnjoying.justice.ecrm.common.constant.EcrmResources;
import com.lnjoying.justice.ecrm.db.model.TblRegionInfo;
import com.lnjoying.justice.ecrm.db.model.TblRegionInfoExample;
import java.util.List;
import java.util.Set;

import com.lnjoying.justice.ecrm.domain.dto.model.RegionNode;
import org.apache.ibatis.annotations.Param;

@MapperModel(EcrmResources.REGION)
public interface TblRegionInfoMapper {
    long countByExample(TblRegionInfoExample example);

    int deleteByExample(TblRegionInfoExample example);

    int deleteByPrimaryKey(String regionId);

    int insert(TblRegionInfo record);

    int insertSelective(TblRegionInfo record);

    List<TblRegionInfo> selectByExample(TblRegionInfoExample example);

    TblRegionInfo selectByPrimaryKey(String regionId);

    int updateByExampleSelective(@Param("record") TblRegionInfo record, @Param("example") TblRegionInfoExample example);

    int updateByExample(@Param("record") TblRegionInfo record, @Param("example") TblRegionInfoExample example);

    int updateByPrimaryKeySelective(TblRegionInfo record);

    int updateByPrimaryKey(TblRegionInfo record);

    List<RegionNode> selectAllNodesByRegionId(@Param("regionId")String regionId);

    Set<String> selectDistinctRegionId();

}