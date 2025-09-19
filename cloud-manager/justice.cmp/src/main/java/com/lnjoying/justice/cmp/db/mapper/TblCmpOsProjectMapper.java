package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpOsProject;
import com.lnjoying.justice.cmp.db.model.TblCmpOsProjectExample;
import com.lnjoying.justice.cmp.db.model.TblCmpOsProjectKey;
import java.util.List;
import java.util.Set;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.OS_PROJECT)
public interface TblCmpOsProjectMapper {
    long countByExample(TblCmpOsProjectExample example);

    int deleteByExample(TblCmpOsProjectExample example);

    int deleteByPrimaryKey(TblCmpOsProjectKey key);

    int insert(TblCmpOsProject record);

    int insertSelective(TblCmpOsProject record);

    List<TblCmpOsProject> selectByExample(TblCmpOsProjectExample example);

    TblCmpOsProject selectByPrimaryKey(TblCmpOsProjectKey key);

    int updateByExampleSelective(@Param("record") TblCmpOsProject record, @Param("example") TblCmpOsProjectExample example);

    int updateByExample(@Param("record") TblCmpOsProject record, @Param("example") TblCmpOsProjectExample example);

    int updateByPrimaryKeySelective(TblCmpOsProject record);

    int updateByPrimaryKey(TblCmpOsProject record);

    Set<String> getProjectIds(@Param("cloudId")String cloudId, @Param("isDomain")Short isDomain);
}