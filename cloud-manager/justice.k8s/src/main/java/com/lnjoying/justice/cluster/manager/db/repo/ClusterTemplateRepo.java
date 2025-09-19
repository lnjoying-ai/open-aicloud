package com.lnjoying.justice.cluster.manager.db.repo;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lnjoying.justice.cluster.manager.db.mapper.TblClusterTemplateInfoMapper;
import com.lnjoying.justice.cluster.manager.db.mapper.TblClusterTmplVerInfoMapper;
import com.lnjoying.justice.cluster.manager.db.model.TblClusterTemplateInfo;
import com.lnjoying.justice.cluster.manager.db.model.TblClusterTemplateInfoExample;
import com.lnjoying.justice.cluster.manager.db.model.TblClusterTmplVerInfo;
import com.lnjoying.justice.cluster.manager.db.model.TblClusterTmplVerInfoExample;
import com.lnjoying.justice.cluster.manager.domain.dto.model.template.ClusterTemplateInfo;
import com.lnjoying.justice.cluster.manager.domain.dto.model.template.ClusterTemplateVersionInfo;
import com.lnjoying.justice.cluster.manager.domain.dto.rsp.cluster.ClusterTemplatesRspDto;
import com.lnjoying.justice.cluster.manager.service.rpc.CombRpcService;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.lnjoying.justice.schema.common.ErrorCode.*;
import static com.lnjoying.justice.schema.common.ErrorLevel.ERROR;
import static com.lnjoying.justice.schema.common.ErrorLevel.INFO;

/**
 * @Author: Regulus
 * @Date: 12/13/21 11:46 AM
 * @Description:
 */
@Component
@Slf4j
public class ClusterTemplateRepo
{
    @Autowired
    private TblClusterTmplVerInfoMapper templateVerInfoMapper;

    @Autowired
    private TblClusterTemplateInfoMapper templateInfoMapper;

    @Autowired
    private CombRpcService combRpcService;
    
    public String getTemplateVersion(String tempVerId)
    {
        TblClusterTmplVerInfo tblClusterTmplVerInfo = templateVerInfoMapper.selectByPrimaryKey(tempVerId);
        return tblClusterTmplVerInfo.getClusterEngineConfig();
    }

    public TblClusterTmplVerInfo getTemplateVersionInfo(String tempVerId)
    {
        TblClusterTmplVerInfo tblClusterTmplVerInfo = templateVerInfoMapper.selectByPrimaryKey(tempVerId);
        return tblClusterTmplVerInfo;
    }

    public int insertSelective(TblClusterTemplateInfo record){
       return  templateInfoMapper.insertSelective(record);
    }

    public List<TblClusterTemplateInfo> getTemplateInfoByName(String templateName){
        Assert.hasText(templateName, "The given templateName must not be null!");
        TblClusterTemplateInfoExample tblClusterTemplateInfoExample = new TblClusterTemplateInfoExample();
        TblClusterTemplateInfoExample.Criteria criteria = tblClusterTemplateInfoExample.createCriteria();
        criteria.andNameEqualTo(templateName);
        return templateInfoMapper.selectByExample(tblClusterTemplateInfoExample);
    }

    public int updateByPrimaryKeySelective(TblClusterTemplateInfo record){
        return  templateInfoMapper.updateByPrimaryKeySelective(record);
    }

    public TblClusterTemplateInfo getTblClusterTemplateInfoById(String id, String ownerBpId, String ownerUserId){
        Assert.hasText(id, "The given template id must not be null!");
        TblClusterTemplateInfo tblClusterTemplateInfo = templateInfoMapper.selectByPrimaryKey(id);
        if (Objects.isNull(tblClusterTemplateInfo))
        {
            throw new WebSystemException(CLUSTER_K8S_TEMPLATE_NOT_EXIST, ErrorLevel.ERROR);
        }

        if (StringUtils.isNotBlank(ownerBpId))
        {
            if (!ownerBpId.equals(tblClusterTemplateInfo.getBp()))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, INFO);
            }
        }

        if (StringUtils.isNotBlank(ownerUserId))
        {
            if (!ownerUserId.equals(tblClusterTemplateInfo.getOwner()))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, INFO);
            }
        }

        return tblClusterTemplateInfo;
    }

    public TblClusterTemplateInfo checkClusterTemplateInfo(String id, String ownerBpId, String ownerUserId)
    {
        return getTblClusterTemplateInfoById(id, ownerBpId, ownerUserId);
    }

    public boolean hasAssociatedResources(String id)
    {
        Assert.hasText(id, "The given template id must not be null!");
        TblClusterTmplVerInfoExample tblClusterTmplVerInfoExample = new TblClusterTmplVerInfoExample();
        TblClusterTmplVerInfoExample.Criteria criteria = tblClusterTmplVerInfoExample.createCriteria();
        criteria.andTemplateIdEqualTo(id);
        List<TblClusterTmplVerInfo> tblClusterTmplVerInfos = templateVerInfoMapper.selectByExample(tblClusterTmplVerInfoExample);
        return !CollectionUtils.isEmpty(tblClusterTmplVerInfos);
    }

    public int deleteByPrimaryKey(String id){
        return templateInfoMapper.deleteByPrimaryKey(id);
    }

    public ClusterTemplatesRspDto getClusterTemplates(String name, String clusterId, String clusterType, String bpId, String owner, Integer pageNum, Integer pageSize)
    {
        PageHelper.startPage(pageNum, pageSize);
        List<ClusterTemplateInfo> clusterTemplateInfos = templateInfoMapper.selectAll(name, clusterId, clusterType, bpId, owner);
        PageInfo<ClusterTemplateInfo> pageInfo = new PageInfo<>(clusterTemplateInfos);

        return ClusterTemplatesRspDto.builder().totalNum(pageInfo.getTotal())
                .templates(clusterTemplateInfos.stream().map(infos -> {setClusterEngineConfig(infos); return assembleUserInfo(infos);}).collect(Collectors.toList())).build();
    }

    public ClusterTemplateInfo getClusterTemplate(String templateId)
    {
        ClusterTemplateInfo clusterTemplateInfo = templateInfoMapper.selectClusterTemplateInfoWithVersionsById(templateId);
        setClusterEngineConfig(clusterTemplateInfo);
        return assembleUserInfo(clusterTemplateInfo);
    }

    public  List<TblClusterTmplVerInfo> getClusterTemplateVersions(String templateId, String version)
    {
        TblClusterTmplVerInfoExample tblClusterTmplVerInfoExample = new TblClusterTmplVerInfoExample();
        TblClusterTmplVerInfoExample.Criteria criteria = tblClusterTmplVerInfoExample.createCriteria();
        criteria.andTemplateIdEqualTo(templateId);
        criteria.andVersionEqualTo(version);
        List<TblClusterTmplVerInfo> tblClusterTmplVerInfos = templateVerInfoMapper.selectByExample(tblClusterTmplVerInfoExample);
        return tblClusterTmplVerInfos;
    }

    public ClusterTemplateVersionInfo selectTemplateVerInfoByPrimaryKey(String versionId){
        Assert.hasText(versionId, "The given template id must not be null!");
        return templateVerInfoMapper.selectOneByPrimaryKey(versionId);
    }

    public  List<TblClusterTmplVerInfo> getClusterTemplateVersions(String versionId)
    {
        TblClusterTmplVerInfoExample tblClusterTmplVerInfoExample = new TblClusterTmplVerInfoExample();
        TblClusterTmplVerInfoExample.Criteria criteria = tblClusterTmplVerInfoExample.createCriteria();
        criteria.andVersionIdEqualTo(versionId);
        List<TblClusterTmplVerInfo> tblClusterTmplVerInfos = templateVerInfoMapper.selectByExample(tblClusterTmplVerInfoExample);
        return tblClusterTmplVerInfos;
    }

    public void checkClusterTemplateVersions(String templateId, String version)
    {
        List<TblClusterTmplVerInfo> templateVersions = getClusterTemplateVersions(templateId, version);
        if (!CollectionUtils.isEmpty(templateVersions))
        {
            throw new WebSystemException(CLUSTER_K8S_TEMPLATE_VERSIONS_DUP, ERROR);
        }
    }

    public List<TblClusterTmplVerInfo> checkClusterTemplateVersions(String versionId)
    {
        List<TblClusterTmplVerInfo> templateVersions = getClusterTemplateVersions(versionId);
        if (CollectionUtils.isEmpty(templateVersions))
        {
           throw new WebSystemException(CLUSTER_K8S_TEMPLATE_VERSION_NOT_EXIST, ERROR);
        }
        return templateVersions;
    }

    public int insertClusterTmplVerInfoSelective(TblClusterTmplVerInfo record){
        return templateVerInfoMapper.insertSelective(record);
    }

    public int updateClusterTmplVerInfoByPrimaryKeySelective(TblClusterTmplVerInfo record){
        return templateVerInfoMapper.updateByPrimaryKeySelective(record);
    }

    public int deleteClusterTmplVerInfoByPrimaryKey(String versionId){
        return templateVerInfoMapper.deleteByPrimaryKey(versionId);
    }

    public TblClusterTemplateInfo getClusterTemplateByClusterIdAndTemplateName(String clusterId, String templateName)
    {
        TblClusterTemplateInfoExample tblClusterTemplateInfoExample = new TblClusterTemplateInfoExample();
        TblClusterTemplateInfoExample.Criteria criteria = tblClusterTemplateInfoExample.createCriteria();
        criteria.andClusterIdEqualTo(clusterId);
        criteria.andNameEqualTo(templateName);
        List<TblClusterTemplateInfo> tblClusterTemplateInfos = templateInfoMapper.selectByExample(tblClusterTemplateInfoExample);
        if (!CollectionUtils.isEmpty(tblClusterTemplateInfos))
        {
            // One-to-one correspondence between cluster id and record
            return tblClusterTemplateInfos.get(0);
        }

        return null;
    }

    private ClusterTemplateInfo assembleUserInfo(ClusterTemplateInfo templateInfo)
    {
        String recordBpId = templateInfo.getBp();
        String recordBpName = "";
        if (StringUtils.isNotBlank(recordBpId))
        {
            try
            {
                recordBpName = combRpcService.getUmsService().getBpNameByBpId(recordBpId);
            }
            catch (Exception e)
            {
                log.error("get bp name failed");
            }
        }
        templateInfo.setBpName(recordBpName);

        String recordUserId = templateInfo.getOwner();
        String recordUserName = "";
        if (StringUtils.isNotBlank(recordUserId))
        {
            try
            {
                recordUserName = combRpcService.getUmsService().getUserNameByUserId(recordUserId);
            }
            catch (Exception e)
            {
                log.error("get user name failed");
            }
        }
        templateInfo.setOwnerName(recordUserName);
        return templateInfo;
    }

    private void setClusterEngineConfig(ClusterTemplateInfo templateInfo)
    {
        templateInfo.getVersions().forEach(version -> version.setClusterEngineConfig(templateInfo.getClusterType()));
    }
}
