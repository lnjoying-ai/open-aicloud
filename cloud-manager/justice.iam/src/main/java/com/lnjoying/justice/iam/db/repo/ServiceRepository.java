package com.lnjoying.justice.iam.db.repo;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lnjoying.justice.iam.db.mapper.TblIamServiceMapper;
import com.lnjoying.justice.iam.db.model.TblIamService;
import com.lnjoying.justice.iam.domain.dto.response.service.ServicesRsp;
import com.lnjoying.justice.iam.domain.model.IamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/16 16:01
 */

@Repository
public class ServiceRepository
{

    @Autowired
    private TblIamServiceMapper serviceMapper;


    public int insertSelective(TblIamService record)
    {
        return serviceMapper.insertSelective(record);
    }

    public int updateByPrimaryKeySelective(TblIamService record)
    {
        return serviceMapper.updateByPrimaryKeySelective(record);
    }

    public ServicesRsp getServiceList(String name, Integer status, Integer pageSize, Integer pageNum)
    {
        PageHelper.startPage(pageNum, pageSize);
        List<TblIamService> tblIamServices = serviceMapper.selectAll(name, status);
        PageInfo<TblIamService> pageInfo = new PageInfo<>(tblIamServices);
        return ServicesRsp.builder().totalNum(pageInfo.getTotal())
                .services(tblIamServices.stream().map(IamService::of).collect(Collectors.toList())).build();
    }

    public List<TblIamService> selectAll(String name)
    {
        return serviceMapper.selectAll(name, TblIamService.ServiceEnableStatus.ENABLE.value());
    }

    public TblIamService selectByPrimaryKey(String id)
    {
        return serviceMapper.selectByPrimaryKey(id);
    }

    public int deleteByPrimaryKey(String id)
    {
        return serviceMapper.deleteByPrimaryKey(id);
    }

    public int countByName(String serviceName)
    {
        Integer count = serviceMapper.countByName(serviceName);
        return Objects.nonNull(count) ? count.intValue() : 0;
    }

    public int countByIamCode(String iamCode)
    {
        Integer count = serviceMapper.countByIamCode(iamCode);
        return Objects.nonNull(count) ? count.intValue() : 0;
    }

    public int countById(String serviceId)
    {
        Integer count = serviceMapper.countById(serviceId);
        return Objects.nonNull(count) ? count.intValue() : 0;
    }

    public String selectIdByName(String name)
    {
        return serviceMapper.selectIdByName(name);
    }

    public String selectServiceIdByIamCode(String iamCode)
    {
        TblIamService tblIamService = serviceMapper.selectByIamCode(iamCode);
        return Optional.ofNullable(tblIamService).map(service -> service.getId()).orElse("");
    }

    public TblIamService selectByIamCode(String iamCode)
    {
        TblIamService tblIamService = serviceMapper.selectByIamCode(iamCode);
        return tblIamService;
    }

}
