package com.lnjoying.justice.iam.service;

import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.iam.db.model.TblIamCommonResource;
import com.lnjoying.justice.iam.db.repo.ResourceRepository;
import com.lnjoying.justice.iam.domain.dto.request.resource.AddCommonResourceReq;
import com.lnjoying.justice.iam.domain.dto.request.resource.UpdateCommonResourceReq;
import com.lnjoying.justice.iam.domain.dto.request.service.UpdateServiceReq;
import com.lnjoying.justice.iam.domain.dto.response.resource.ResourceRsp;
import com.lnjoying.justice.iam.domain.dto.response.service.ServicesRsp;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.micro.core.common.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.lnjoying.justice.schema.common.ErrorCode.IAM_SERVICE_NOT_EXIST;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/16 19:07
 */


@Slf4j
@Service
public class CommonResourceService
{
    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private ServiceService serviceService;

    public Object addCommonResource(AddCommonResourceReq req)
    {
        if (!serviceService.checkServiceExists(req.getServiceId()))
        {
            throw new WebSystemException(IAM_SERVICE_NOT_EXIST, ErrorLevel.ERROR);
        }
        TblIamCommonResource tblIamCommonResource = new TblIamCommonResource();
        BeanUtils.copyProperties(req, tblIamCommonResource);
        tblIamCommonResource.setId(Utils.assignUUId());
        tblIamCommonResource.setCreateTime(new Date());
        tblIamCommonResource.setUpdateTime(tblIamCommonResource.getCreateTime());
        resourceRepository.insertSelective(tblIamCommonResource);
        return tblIamCommonResource.getId();
    }

    public ResourceRsp getCommonResourceList(String name, Integer status, Integer pageSize, Integer pageNum)
    {
        return resourceRepository.getCommonResourceList(name, status, pageSize, pageNum);
    }

    public Object getCommonResource(String resourceId, String queryBpId)
    {
        return resourceRepository.selectById(resourceId);
    }

    public void deleteCommonResource(String resourceId, String queryBpId)
    {
        resourceRepository.deleteByPrimaryKey(resourceId);
    }

    public void updateResource(String resourceId, UpdateCommonResourceReq req)
    {
        if (!serviceService.checkServiceExists(req.getServiceId()) && !checkResourceExists(resourceId))
        {
            log.error("service id or resource id not exist:{}");
            throw new WebSystemException(IAM_SERVICE_NOT_EXIST, ErrorLevel.ERROR);
        }
        TblIamCommonResource tblIamCommonResource = new TblIamCommonResource();
        BeanUtils.copyProperties(req, tblIamCommonResource);
        tblIamCommonResource.setId(resourceId);
        tblIamCommonResource.setUpdateTime(tblIamCommonResource.getCreateTime());
        resourceRepository.updateByPrimaryKeySelective(tblIamCommonResource);
    }

    public boolean checkResourceExists(String resourceId)
    {
        if (StringUtils.isBlank(resourceId))
        {
            return false;
        }
        int count = resourceRepository.countByResourceId(resourceId);
        return count > 0 ? true : false;
    }

}
