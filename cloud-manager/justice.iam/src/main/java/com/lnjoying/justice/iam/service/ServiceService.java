package com.lnjoying.justice.iam.service;

import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.iam.db.model.TblIamProject;
import com.lnjoying.justice.iam.db.model.TblIamService;
import com.lnjoying.justice.iam.db.repo.ServiceRepository;
import com.lnjoying.justice.iam.domain.dto.request.service.AddServiceReq;
import com.lnjoying.justice.iam.domain.dto.request.service.UpdateServiceReq;
import com.lnjoying.justice.iam.domain.dto.response.service.ServicesRsp;
import com.lnjoying.justice.iam.domain.model.IamService;
import com.lnjoying.justice.iam.domain.model.RecordStatus;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.micro.core.common.RandomName;
import com.micro.core.common.Utils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

import static com.lnjoying.justice.iam.common.constant.IamConstants.SUBTRACT;
import static com.lnjoying.justice.schema.common.ErrorCode.*;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/16 15:53
 */

@Slf4j
@Service
public class ServiceService
{

    @Autowired
    private ServiceRepository serviceRepository;

    public Object addService(AddServiceReq req)
    {
        if (checkServiceIamCodeExists(req.getIamCode()))
        {
            throw new WebSystemException(IAM_SERVICE_IAM_CODE_EXIST, ErrorLevel.ERROR);
        }

        TblIamService tblIamService = new TblIamService();
        BeanUtils.copyProperties(req, tblIamService);

        tblIamService.setId(Utils.assignUUId());
        tblIamService.setName(req.getIamCode());
        //tblIamService.setName("service" + SUBTRACT + RandomName.getRandomName(6));
        // default enable
        int enable = Objects.isNull(req.getEnable()) ? TblIamProject.ProjectEnableStatus.ENABLE.value() :  req.getEnable();
        tblIamService.setEnable(enable);
        tblIamService.setStatus(RecordStatus.NORMAL.value());
        tblIamService.setCreateTime(new Date());
        tblIamService.setUpdateTime(tblIamService.getCreateTime());

        serviceRepository.insertSelective(tblIamService);
        return tblIamService.getId();
    }


    public void updateService(String serviceId, UpdateServiceReq req)
    {
        if (!checkServiceExists(serviceId))
        {
            throw new WebSystemException(IAM_SERVICE_NOT_EXIST, ErrorLevel.ERROR);
        }

        TblIamService tblIamService = new TblIamService();
        BeanUtils.copyProperties(req, tblIamService);

        tblIamService.setId(serviceId);
        tblIamService.setUpdateTime(new Date());
        serviceRepository.updateByPrimaryKeySelective(tblIamService);
    }


    public ServicesRsp getServiceList(String name, Integer status, Integer pageSize, Integer pageNum)
    {
        ServicesRsp servicesRsp = serviceRepository.getServiceList(name, status, pageSize, pageNum);
        return servicesRsp;
    }


    public Object getService(String serviceId)
    {
        TblIamService tblIamService = serviceRepository.selectByPrimaryKey(serviceId);
        return IamService.of(tblIamService);
    }

    public void deleteService(String serviceId)
    {
        serviceRepository.deleteByPrimaryKey(serviceId);
    }


    public boolean checkServiceExists(String serviceId)
    {
        if (StringUtils.isBlank(serviceId))
        {
            return false;
        }
        int count = serviceRepository.countById(serviceId);
        return count > 0 ? true : false;
    }

    private boolean checkServiceNameExists(String serviceName)
    {
        int count = serviceRepository.countByName(serviceName);
        return count > 0 ? true : false;
    }

    private boolean checkServiceIamCodeExists(String iamCode)
    {
        int count = serviceRepository.countByIamCode(iamCode);
        return count > 0 ? true : false;
    }


}
