package com.lnjoying.justice.aos.db.mapper;

import com.lnjoying.justice.aos.db.model.TblStackInstInfo;
import com.lnjoying.justice.aos.db.model.TblStackInstInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblStackInstInfoMapper {
    long countByExample(TblStackInstInfoExample example);

    int deleteByExample(TblStackInstInfoExample example);

    int deleteByPrimaryKey(String instId);

    int insert(TblStackInstInfo record);

    int insertSelective(TblStackInstInfo record);

    List<TblStackInstInfo> selectByExample(TblStackInstInfoExample example);

    TblStackInstInfo selectByPrimaryKey(String instId);

    int updateByExampleSelective(@Param("record") TblStackInstInfo record, @Param("example") TblStackInstInfoExample example);

    int updateByExample(@Param("record") TblStackInstInfo record, @Param("example") TblStackInstInfoExample example);

    int updateByPrimaryKeySelective(TblStackInstInfo record);

    int updateByPrimaryKey(TblStackInstInfo record);
}