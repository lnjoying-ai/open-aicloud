package com.lnjoying.justice.cluster.manager.db.mapper;

import com.lnjoying.justice.cluster.manager.common.ClsResources;
import com.lnjoying.justice.cluster.manager.db.model.TblClusterTemplateInfo;
import com.lnjoying.justice.cluster.manager.db.model.TblClusterTemplateInfoExample;
import java.util.List;

import com.lnjoying.justice.cluster.manager.domain.dto.model.cluster.Cluster2Template;
import com.lnjoying.justice.cluster.manager.domain.dto.model.template.ClusterTemplateInfo;
import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(ClsResources.CLUSTER_TEMPLATE)
public interface TblClusterTemplateInfoMapper {
    long countByExample(TblClusterTemplateInfoExample example);

    int deleteByExample(TblClusterTemplateInfoExample example);

    int deleteByPrimaryKey(String id);

    int insert(TblClusterTemplateInfo record);

    int insertSelective(TblClusterTemplateInfo record);

    List<TblClusterTemplateInfo> selectByExample(TblClusterTemplateInfoExample example);

    TblClusterTemplateInfo selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TblClusterTemplateInfo record, @Param("example") TblClusterTemplateInfoExample example);

    int updateByExample(@Param("record") TblClusterTemplateInfo record, @Param("example") TblClusterTemplateInfoExample example);

    int updateByPrimaryKeySelective(TblClusterTemplateInfo record);

    int updateByPrimaryKey(TblClusterTemplateInfo record);

    /**
     * get a list of templates with version information
     * @param name
     * @param bp
     * @param owner
     * @return
     */
    List<ClusterTemplateInfo> selectAll(@Param("name")String name, @Param("clusterId")String clusterId, @Param("clusterType")String clusterType, @Param("bp")String bp,
                                        @Param("owner")String owner);

    /**
     * get a template with version information
     * @param id
     * @return
     */
    ClusterTemplateInfo  selectClusterTemplateInfoWithVersionsById(@Param("id")String id);

    /**
     * get export cluster template versions
     * @param clusterId
     * @return
     */
    List<TblClusterTemplateInfo.ClusterTemplateVersion> selectVersionsByClusterId(@Param("clusterId")String clusterId);

}