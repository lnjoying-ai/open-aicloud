package com.lnjoying.justice.iam.db.mapper;
import java.util.Date;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import com.lnjoying.justice.iam.common.constant.IamResources;
import com.lnjoying.justice.iam.db.model.TblBpInfo;
import com.lnjoying.justice.iam.db.model.TblBpInfoExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MapperModel(IamResources.BP)
public interface TblBpInfoMapper {
    long countByExample(TblBpInfoExample example);

    int deleteByExample(TblBpInfoExample example);

    int deleteByPrimaryKey(String bpId);

    int insert(TblBpInfo record);

    int insertSelective(TblBpInfo record);

    List<TblBpInfo> selectByExample(TblBpInfoExample example);

    TblBpInfo selectByPrimaryKey(String bpId);

    int updateByExampleSelective(@Param("record") TblBpInfo record, @Param("example") TblBpInfoExample example);

    int updateByExample(@Param("record") TblBpInfo record, @Param("example") TblBpInfoExample example);

    int updateByPrimaryKeySelective(TblBpInfo record);

    int updateByPrimaryKey(TblBpInfo record);

    TblBpInfo selectByBpName(@Param("bpName")String bpName);

    List<TblBpInfo> selectAllByStatus(@Param("status")Integer status);

    List<TblBpInfo> selectAllByCreateTime(@Param("createTime")Date createTime);



}