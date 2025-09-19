package com.lnjoying.justice.cluster.manager.service.template.impl;

import com.lnjoying.justice.cluster.manager.common.ClusterType;
import com.lnjoying.justice.cluster.manager.db.model.TblClusterInfo;
import com.lnjoying.justice.cluster.manager.db.model.TblClusterTemplateInfo;
import com.lnjoying.justice.cluster.manager.db.model.TblClusterTmplVerInfo;
import com.lnjoying.justice.cluster.manager.db.repo.ClusterRepo;
import com.lnjoying.justice.cluster.manager.db.repo.ClusterTemplateRepo;
import com.lnjoying.justice.cluster.manager.domain.dto.model.template.*;
import com.lnjoying.justice.cluster.manager.domain.dto.req.cluster.ExportClusterTemplateReqDto;
import com.lnjoying.justice.cluster.manager.domain.dto.rsp.cluster.ClusterTemplatesRspDto;
import com.lnjoying.justice.cluster.manager.service.template.ClusterTemplateService;
import com.lnjoying.justice.cluster.manager.util.ClsUtils;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.micro.core.common.Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.lnjoying.justice.cluster.manager.db.model.TblClusterTemplateInfo.TemplateType.FROM_CLUSTER;
import static com.lnjoying.justice.schema.common.ErrorCode.*;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/1/26 10:53
 */
@Service
public class ClusterTemplateServiceImpl implements ClusterTemplateService
{
    @Autowired
    private ClusterTemplateRepo clusterTemplateRepo;

    @Autowired
    private ClusterRepo clusterRepo;

    @Override
    public String createTemplate(AddTemplateInfoReq req)
    {
        List<TblClusterTemplateInfo> templateInfos = clusterTemplateRepo.getTemplateInfoByName(req.getName());
        if (!CollectionUtils.isEmpty(templateInfos))
        {
            throw new WebSystemException(CLUSTER_K8S_TEMPLATE_NAME_EXIST, ErrorLevel.ERROR);
        }

        TblClusterTemplateInfo tblClusterTemplateInfo = new TblClusterTemplateInfo();
        BeanUtils.copyProperties(req, tblClusterTemplateInfo);
        tblClusterTemplateInfo.setId(Utils.assignUUId());
        tblClusterTemplateInfo.setCreateTime(new Date());
        tblClusterTemplateInfo.setUpdateTime(tblClusterTemplateInfo.getCreateTime());
        tblClusterTemplateInfo.setMembers(ClsUtils.toJson(req.getMembers()));
        tblClusterTemplateInfo.setTags(ClsUtils.toJson(req.getTags()));
        tblClusterTemplateInfo.setClusterType(req.getClusterType());
        clusterTemplateRepo.insertSelective(tblClusterTemplateInfo);
        return tblClusterTemplateInfo.getId();
    }

    @Override
    public void updateTemplate(UpdateTemplateInfoReq req)
    {
        clusterTemplateRepo.checkClusterTemplateInfo(req.getId(), req.getBp(), req.getCreator());

        TblClusterTemplateInfo tblClusterTemplateInfo = new TblClusterTemplateInfo();
        tblClusterTemplateInfo.setMembers(ClsUtils.toJson(req.getMembers()));
        tblClusterTemplateInfo.setTags(ClsUtils.toJson(req.getTags()));
        tblClusterTemplateInfo.setDescription(req.getDescription());
        tblClusterTemplateInfo.setId(req.getId());
        tblClusterTemplateInfo.setUpdateTime(new Date());
        tblClusterTemplateInfo.setName(req.getName());
        clusterTemplateRepo.updateByPrimaryKeySelective(tblClusterTemplateInfo);
    }

    @Override
    public void deleteTemplate(String templateId, String bpId, String userId)
    {
        clusterTemplateRepo.checkClusterTemplateInfo(templateId, bpId, userId);

        if (clusterTemplateRepo.hasAssociatedResources(templateId))
        {
           throw new WebSystemException(CLUSTER_K8S_TEMPLATE_ASSOCIATED_RESOURCES, ErrorLevel.ERROR);
        }

        clusterTemplateRepo.deleteByPrimaryKey(templateId);
    }

    @Override
    public String createTemplateVersions(AddTemplateVersionReq versionReq)
    {
        TblClusterTemplateInfo tblClusterTemplateInfo = clusterTemplateRepo.checkClusterTemplateInfo(versionReq.getTemplateId(), versionReq.getBp(), versionReq.getOwner());
        clusterTemplateRepo.checkClusterTemplateVersions(versionReq.getTemplateId(), versionReq.getVersion());

        TblClusterTmplVerInfo tblClusterTmplVerInfo = new TblClusterTmplVerInfo();
        BeanUtils.copyProperties(versionReq, tblClusterTmplVerInfo);
        tblClusterTmplVerInfo.setVersionId(Utils.assignUUId());
        if (tblClusterTemplateInfo.getClusterType().equals(ClusterType.K8S))
        {
            if (versionReq.getJkeConfig() == null)
            {
                throw new WebSystemException(ErrorCode.CLUSTER_K8S_JKE_ENGINE_NULL, ErrorLevel.INFO);
            }
            tblClusterTmplVerInfo.setClusterEngineConfig(ClsUtils.toJson(versionReq.getJkeConfig()));
        }
        else if (tblClusterTemplateInfo.getClusterType().equals(ClusterType.K3S))
        {
            if (versionReq.getK3sConfig() == null)
            {
                throw new WebSystemException(ErrorCode.CLUSTER_K3S_JKE_ENGINE_NULL, ErrorLevel.INFO);
            }
            tblClusterTmplVerInfo.setClusterEngineConfig(ClsUtils.toJson(versionReq.getK3sConfig()));
        }
        tblClusterTmplVerInfo.setTags(ClsUtils.toJson(versionReq.getTags()));
        tblClusterTmplVerInfo.setCreateTime(new Date());
        tblClusterTmplVerInfo.setUpdateTime(tblClusterTmplVerInfo.getCreateTime());
        clusterTemplateRepo.insertClusterTmplVerInfoSelective(tblClusterTmplVerInfo);
        return tblClusterTmplVerInfo.getVersionId();
    }

    @Override
    public void updateTemplateVersions(UpdateTemplateVersionReq versionReq)
    {
        List<TblClusterTmplVerInfo> tblClusterTmplVerInfos = clusterTemplateRepo.checkClusterTemplateVersions(versionReq.getVersionId());
        TblClusterTemplateInfo tblClusterTemplateInfo = clusterTemplateRepo.checkClusterTemplateInfo(tblClusterTmplVerInfos.get(0).getTemplateId(), versionReq.getBp(), versionReq.getOwner());

        TblClusterTmplVerInfo tblClusterTmplVerInfo = new TblClusterTmplVerInfo();
        tblClusterTmplVerInfo.setVersionId(versionReq.getVersionId());
        tblClusterTmplVerInfo.setDescription(versionReq.getDescription());
        if (tblClusterTemplateInfo.getClusterType().equals(ClusterType.K8S))
        {
            if (versionReq.getJkeConfig() != null)
            {
                tblClusterTmplVerInfo.setClusterEngineConfig(ClsUtils.toJson(versionReq.getJkeConfig()));
            }
        }
        else if (tblClusterTemplateInfo.getClusterType().equals(ClusterType.K3S))
        {
            if (versionReq.getK3sConfig() != null)
            {
                tblClusterTmplVerInfo.setClusterEngineConfig(ClsUtils.toJson(versionReq.getK3sConfig()));
            }
        }
        tblClusterTmplVerInfo.setEnable(versionReq.isEnable());
        tblClusterTmplVerInfo.setTags(ClsUtils.toJson(versionReq.getTags()));
        tblClusterTmplVerInfo.setUpdateTime(new Date());
        clusterTemplateRepo.updateClusterTmplVerInfoByPrimaryKeySelective(tblClusterTmplVerInfo);
    }

    @Override
    public void deleteTemplateVersions(String versionId)
    {
        clusterTemplateRepo.checkClusterTemplateVersions(versionId);
        clusterTemplateRepo.deleteClusterTmplVerInfoByPrimaryKey(versionId);
    }

    @Override
    public ClusterTemplatesRspDto getClusterTemplates(String name, String clusterId, String clusterType, String bpId, String owner, Integer pageNum, Integer pageSize)
    {
        return clusterTemplateRepo.getClusterTemplates(name, clusterId, clusterType, bpId, owner, pageNum, pageSize);
    }

    @Override
    public ClusterTemplateInfo getClusterTemplate(String templateId, String bpId, String userId)
    {
        clusterTemplateRepo.checkClusterTemplateInfo(templateId, bpId, userId);
        return clusterTemplateRepo.getClusterTemplate(templateId);
    }

    @Override
    public ClusterTemplateVersionInfoDetail getTemplateVersion(String versionId)
    {
        ClusterTemplateVersionInfo clusterTemplateVersionInfo = clusterTemplateRepo.selectTemplateVerInfoByPrimaryKey(versionId);
        if (Objects.nonNull(clusterTemplateVersionInfo))
        {
            ClusterTemplateVersionInfoDetail detail = new ClusterTemplateVersionInfoDetail();
            BeanUtils.copyProperties(clusterTemplateVersionInfo, detail);
            String templateId = clusterTemplateVersionInfo.getTemplateId();
            ClusterTemplateInfo clusterTemplate = clusterTemplateRepo.getClusterTemplate(templateId);
            if (Objects.nonNull(clusterTemplate))
            {
                detail.setTemplateName(clusterTemplate.getName());
            }
            detail.setClusterEngineConfig(clusterTemplate.getClusterType());

            return detail;
        }

        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void exportClusterTemplateFromCluster(ExportClusterTemplateReqDto req)
    {
        TblClusterTemplateInfo clusterTemplate = clusterTemplateRepo.getClusterTemplateByClusterIdAndTemplateName(req.getClusterId(), req.getTemplateName());
        if (Objects.isNull(clusterTemplate))
        {
            createTemplateFromCluster(req);
            return;
        }

        List<TblClusterTmplVerInfo> clusterTemplateVersions = clusterTemplateRepo.getClusterTemplateVersions(clusterTemplate.getId(), req.getTemplateVersion());
        if (CollectionUtils.isEmpty(clusterTemplateVersions))
        {
            createTemplateVersion(req, clusterTemplate);
        }
        else
        {
            if (!req.getOverwrite())
            {
                throw new WebSystemException(CLUSTER_K8S_TEMPLATE_VERSION_EXIST, ErrorLevel.ERROR);
            }
            TblClusterTmplVerInfo tblClusterTmplVerInfo = clusterTemplateVersions.get(0);
            updateTemplateVersions(req, clusterTemplate, tblClusterTmplVerInfo.getVersionId());
        }
        updateTemplate(req, clusterTemplate);
    }

    private void updateTemplate(ExportClusterTemplateReqDto req, TblClusterTemplateInfo clusterTemplate)
    {
        TblClusterTemplateInfo tblClusterTemplateInfo = new TblClusterTemplateInfo();
        tblClusterTemplateInfo.setMembers(ClsUtils.toJson(req.getMembers()));
        tblClusterTemplateInfo.setTags(ClsUtils.toJson(req.getTags()));
        tblClusterTemplateInfo.setName(req.getTemplateName());
        tblClusterTemplateInfo.setDescription(req.getTemplateDesc());
        tblClusterTemplateInfo.setId(clusterTemplate.getId());
        tblClusterTemplateInfo.setUpdateTime(new Date());
        clusterTemplateRepo.updateByPrimaryKeySelective(tblClusterTemplateInfo);
    }

    private void updateTemplateVersions(ExportClusterTemplateReqDto req, TblClusterTemplateInfo clusterTemplate, String versionId)
    {
        TblClusterTmplVerInfo tblClusterTmplVerInfo = new TblClusterTmplVerInfo();
        tblClusterTmplVerInfo.setVersionId(versionId);
        tblClusterTmplVerInfo.setDescription(req.getTemplateDesc());
        setClusterEngineConfigFromCluster(req.getClusterId(), tblClusterTmplVerInfo);
        tblClusterTmplVerInfo.setTags(ClsUtils.toJson(req.getTags()));
        tblClusterTmplVerInfo.setUpdateTime(new Date());
        clusterTemplateRepo.updateClusterTmplVerInfoByPrimaryKeySelective(tblClusterTmplVerInfo);

    }

    private void createTemplateFromCluster(ExportClusterTemplateReqDto req)
    {
        // insert template
        TblClusterTemplateInfo tblClusterTemplateInfo = new TblClusterTemplateInfo();
        BeanUtils.copyProperties(req, tblClusterTemplateInfo);
        tblClusterTemplateInfo.setId(Utils.assignUUId());
        tblClusterTemplateInfo.setCreateTime(new Date());
        tblClusterTemplateInfo.setName(req.getTemplateName());
        tblClusterTemplateInfo.setDescription(req.getTemplateDesc());
        tblClusterTemplateInfo.setUpdateTime(tblClusterTemplateInfo.getCreateTime());
        tblClusterTemplateInfo.setMembers(ClsUtils.toJson(req.getMembers()));
        tblClusterTemplateInfo.setTags(ClsUtils.toJson(req.getTags()));
        tblClusterTemplateInfo.setType(FROM_CLUSTER.getValue());

        TblClusterInfo cluster = clusterRepo.getCluster(req.getClusterId());
        if (Objects.nonNull(cluster))
        {
            tblClusterTemplateInfo.setClusterType(cluster.getClusterType());
        }
        else
        {
            tblClusterTemplateInfo.setClusterType(ClusterType.K8S);
        }

        clusterTemplateRepo.insertSelective(tblClusterTemplateInfo);

        // insert template version
        createTemplateVersion(req, tblClusterTemplateInfo);
    }

    private void createTemplateVersion(ExportClusterTemplateReqDto req, TblClusterTemplateInfo tblClusterTemplateInfo)
    {
        TblClusterTmplVerInfo tblClusterTmplVerInfo = new TblClusterTmplVerInfo();
        BeanUtils.copyProperties(req, tblClusterTmplVerInfo);
        tblClusterTmplVerInfo.setTemplateId(tblClusterTemplateInfo.getId());
        tblClusterTmplVerInfo.setVersion(req.getTemplateVersion());
        tblClusterTmplVerInfo.setDescription(req.getTemplateDesc());
        tblClusterTmplVerInfo.setVersionId(Utils.assignUUId());
        setClusterEngineConfigFromCluster(req.getClusterId(), tblClusterTmplVerInfo);
        tblClusterTmplVerInfo.setEnable(true);
        tblClusterTmplVerInfo.setCreateTime(new Date());
        tblClusterTmplVerInfo.setUpdateTime(tblClusterTmplVerInfo.getCreateTime());
        clusterTemplateRepo.insertClusterTmplVerInfoSelective(tblClusterTmplVerInfo);
    }

    private void setClusterEngineConfigFromCluster(String clusterId, TblClusterTmplVerInfo tblClusterTmplVerInfo)
    {
        TblClusterInfo cluster = clusterRepo.getCluster(clusterId);
        if (Objects.nonNull(cluster))
        {
            tblClusterTmplVerInfo.setClusterEngineConfig(cluster.getClusterEngineConfig());
        }
        else
        {
            tblClusterTmplVerInfo.setClusterEngineConfig("{}");
        }
    }
}
