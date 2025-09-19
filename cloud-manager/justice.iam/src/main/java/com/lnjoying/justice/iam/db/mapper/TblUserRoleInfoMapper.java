package com.lnjoying.justice.iam.db.mapper;

import com.lnjoying.justice.iam.db.model.TblUserRoleInfoExample;
import com.lnjoying.justice.iam.db.model.TblUserRoleInfoKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TblUserRoleInfoMapper {
    long countByExample(TblUserRoleInfoExample example);

    int deleteByExample(TblUserRoleInfoExample example);

    int deleteByPrimaryKey(TblUserRoleInfoKey key);

    int insert(TblUserRoleInfoKey record);

    int insertSelective(TblUserRoleInfoKey record);

    List<TblUserRoleInfoKey> selectByExample(TblUserRoleInfoExample example);

    int updateByExampleSelective(@Param("record") TblUserRoleInfoKey record, @Param("example") TblUserRoleInfoExample example);

    int updateByExample(@Param("record") TblUserRoleInfoKey record, @Param("example") TblUserRoleInfoExample example);
}