package com.lnjoying.justice.iam.db.mapper;
import java.util.Collection;

import com.lnjoying.justice.iam.db.model.TblRoleInfo;
import com.lnjoying.justice.iam.db.model.TblRoleInfoExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TblRoleInfoMapper {
    long countByExample(TblRoleInfoExample example);

    int deleteByExample(TblRoleInfoExample example);

    int deleteByPrimaryKey(Long roleId);

    int insert(TblRoleInfo record);

    int insertSelective(TblRoleInfo record);

    List<TblRoleInfo> selectByExample(TblRoleInfoExample example);

    TblRoleInfo selectByPrimaryKey(Long roleId);

    int updateByExampleSelective(@Param("record") TblRoleInfo record, @Param("example") TblRoleInfoExample example);

    int updateByExample(@Param("record") TblRoleInfo record, @Param("example") TblRoleInfoExample example);

    int updateByPrimaryKeySelective(TblRoleInfo record);

    int updateByPrimaryKey(TblRoleInfo record);

    int countByRoleIdIn(@Param("roleIdCollection")Collection<Long> roleIds);


}