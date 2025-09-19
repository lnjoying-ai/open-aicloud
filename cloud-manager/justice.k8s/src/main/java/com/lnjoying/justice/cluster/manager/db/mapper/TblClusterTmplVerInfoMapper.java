package com.lnjoying.justice.cluster.manager.db.mapper;

import com.lnjoying.justice.cluster.manager.db.model.TblClusterTmplVerInfo;
import com.lnjoying.justice.cluster.manager.db.model.TblClusterTmplVerInfoExample;
import java.util.List;

import com.lnjoying.justice.cluster.manager.domain.dto.model.template.ClusterTemplateVersionInfo;
import org.apache.ibatis.annotations.Param;

public interface TblClusterTmplVerInfoMapper {
    long countByExample(TblClusterTmplVerInfoExample example);

    int deleteByExample(TblClusterTmplVerInfoExample example);

    int deleteByPrimaryKey(String versionId);

    int insert(TblClusterTmplVerInfo record);

    int insertSelective(TblClusterTmplVerInfo record);

    List<TblClusterTmplVerInfo> selectByExample(TblClusterTmplVerInfoExample example);

    TblClusterTmplVerInfo selectByPrimaryKey(String versionId);

    int updateByExampleSelective(@Param("record") TblClusterTmplVerInfo record, @Param("example") TblClusterTmplVerInfoExample example);

    int updateByExample(@Param("record") TblClusterTmplVerInfo record, @Param("example") TblClusterTmplVerInfoExample example);

    int updateByPrimaryKeySelective(TblClusterTmplVerInfo record);

    int updateByPrimaryKey(TblClusterTmplVerInfo record);

    List<ClusterTemplateVersionInfo> selectAllByTemplateId(@Param("templateId")String templateId);

    Integer countByTemplateId(@Param("templateId")String templateId);

    ClusterTemplateVersionInfo selectOneByPrimaryKey(String versionId);
}