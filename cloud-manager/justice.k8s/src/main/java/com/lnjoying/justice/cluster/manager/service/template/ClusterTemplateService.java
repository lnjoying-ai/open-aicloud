package com.lnjoying.justice.cluster.manager.service.template;

import com.lnjoying.justice.cluster.manager.domain.dto.model.template.*;
import com.lnjoying.justice.cluster.manager.domain.dto.req.cluster.ExportClusterTemplateReqDto;
import com.lnjoying.justice.cluster.manager.domain.dto.rsp.cluster.ClusterTemplatesRspDto;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/1/26 10:43
 */

public interface ClusterTemplateService
{
    /**
     * create template
     * @param req
     * @return
     */
    String createTemplate(AddTemplateInfoReq req);

    /**
     * update template
     * @param templateInfoReq
     * @return
     */
    void updateTemplate(UpdateTemplateInfoReq templateInfoReq);

    /**
     * delete template
     * @param templateId
     * @param bpId
     * @param userId
     */
    void deleteTemplate(String templateId, String bpId, String userId);

    /**
     * create template version
     * @param versionReq
     * @return
     */
    String createTemplateVersions(AddTemplateVersionReq versionReq);

    /**
     * update template version
     * @param versionReq
     */
    void updateTemplateVersions(UpdateTemplateVersionReq versionReq);

    /**
     * delete template version
     * @param versionId
     */
    void deleteTemplateVersions(String versionId);

    /**
     * get templates
     * @param name
     * @param clusterId
     * @param clusterType
     * @param bpId
     * @param owner
     * @param pageNum
     * @param pageSize
     * @return
     */
    ClusterTemplatesRspDto getClusterTemplates(String name, String clusterId, String clusterType, String bpId, String owner, Integer pageNum, Integer pageSize);

    /**
     * get template
     * @param templateId
     * @param bpId
     * @param userId
     * @return
     */
    ClusterTemplateInfo getClusterTemplate(String templateId, String bpId, String userId);

    /**
     * get template version
     * @param versionId
     * @return
     */
    ClusterTemplateVersionInfoDetail getTemplateVersion(String versionId);

    /**
     * export template from cluster
     *
     * @param exportClusterTemplateReq@return
     */
    void exportClusterTemplateFromCluster(ExportClusterTemplateReqDto exportClusterTemplateReq);
}
