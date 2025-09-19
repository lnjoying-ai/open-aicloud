package com.lnjoying.justice.ecrm.db.mapper;

import com.lnjoying.justice.ecrm.db.model.TblRegionBindInfo;
import com.lnjoying.justice.ecrm.db.model.TblRegionBindInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblRegionBindInfoMapper {
    long countByExample(TblRegionBindInfoExample example);

    int deleteByExample(TblRegionBindInfoExample example);

    int insert(TblRegionBindInfo record);

    int insertSelective(TblRegionBindInfo record);

    List<TblRegionBindInfo> selectByExample(TblRegionBindInfoExample example);

    int updateByExampleSelective(@Param("record") TblRegionBindInfo record, @Param("example") TblRegionBindInfoExample example);

    int updateByExample(@Param("record") TblRegionBindInfo record, @Param("example") TblRegionBindInfoExample example);
}