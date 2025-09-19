package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.db.model.TblCmpOsExtradhcpopts;
import com.lnjoying.justice.cmp.db.model.TblCmpOsExtradhcpoptsExample;
import com.lnjoying.justice.cmp.db.model.TblCmpOsExtradhcpoptsKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblCmpOsExtradhcpoptsMapper {
    long countByExample(TblCmpOsExtradhcpoptsExample example);

    int deleteByExample(TblCmpOsExtradhcpoptsExample example);

    int deleteByPrimaryKey(TblCmpOsExtradhcpoptsKey key);

    int insert(TblCmpOsExtradhcpopts record);

    int insertSelective(TblCmpOsExtradhcpopts record);

    List<TblCmpOsExtradhcpopts> selectByExample(TblCmpOsExtradhcpoptsExample example);

    TblCmpOsExtradhcpopts selectByPrimaryKey(TblCmpOsExtradhcpoptsKey key);

    int updateByExampleSelective(@Param("record") TblCmpOsExtradhcpopts record, @Param("example") TblCmpOsExtradhcpoptsExample example);

    int updateByExample(@Param("record") TblCmpOsExtradhcpopts record, @Param("example") TblCmpOsExtradhcpoptsExample example);

    int updateByPrimaryKeySelective(TblCmpOsExtradhcpopts record);

    int updateByPrimaryKey(TblCmpOsExtradhcpopts record);
}