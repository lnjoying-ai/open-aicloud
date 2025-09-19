package com.lnjoying.justice.cluster.manager.db.mapper;

import com.lnjoying.justice.cluster.manager.db.model.TblClusterExtmplInfo;
import com.lnjoying.justice.cluster.manager.db.model.TblClusterExtmplInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblClusterExtmplInfoMapper {
    long countByExample(TblClusterExtmplInfoExample example);

    int deleteByExample(TblClusterExtmplInfoExample example);

    int insert(TblClusterExtmplInfo record);

    int insertSelective(TblClusterExtmplInfo record);

    List<TblClusterExtmplInfo> selectByExample(TblClusterExtmplInfoExample example);

    int updateByExampleSelective(@Param("record") TblClusterExtmplInfo record, @Param("example") TblClusterExtmplInfoExample example);

    int updateByExample(@Param("record") TblClusterExtmplInfo record, @Param("example") TblClusterExtmplInfoExample example);
}