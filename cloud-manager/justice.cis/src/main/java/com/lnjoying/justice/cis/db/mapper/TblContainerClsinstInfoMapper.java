package com.lnjoying.justice.cis.db.mapper;

import com.lnjoying.justice.cis.db.model.TblContainerClsinstInfo;
import com.lnjoying.justice.cis.db.model.TblContainerClsinstInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblContainerClsinstInfoMapper {
    long countByExample(TblContainerClsinstInfoExample example);

    int deleteByExample(TblContainerClsinstInfoExample example);

    int deleteByPrimaryKey(String instId);

    int insert(TblContainerClsinstInfo record);

    int insertSelective(TblContainerClsinstInfo record);

    List<TblContainerClsinstInfo> selectByExample(TblContainerClsinstInfoExample example);

    TblContainerClsinstInfo selectByPrimaryKey(String instId);

    int updateByExampleSelective(@Param("record") TblContainerClsinstInfo record, @Param("example") TblContainerClsinstInfoExample example);

    int updateByExample(@Param("record") TblContainerClsinstInfo record, @Param("example") TblContainerClsinstInfoExample example);

    int updateByPrimaryKeySelective(TblContainerClsinstInfo record);

    int updateByPrimaryKey(TblContainerClsinstInfo record);
}