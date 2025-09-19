package com.lnjoying.justice.iam.db.repo;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lnjoying.justice.iam.db.mapper.TblIamActionMapper;
import com.lnjoying.justice.iam.db.mapper.TblIamCommonResourceMapper;
import com.lnjoying.justice.iam.db.mapper.TblIamResourceAttrMapper;
import com.lnjoying.justice.iam.db.model.*;
import com.lnjoying.justice.iam.domain.dto.response.resource.ResourceRsp;
import com.lnjoying.justice.iam.domain.dto.response.resource.TblIamCommonResourceDetail;
import com.lnjoying.justice.iam.domain.dto.response.resource.TblIamCommonResourceSummary;
import com.lnjoying.justice.iam.domain.model.ActionsCache;
import com.lnjoying.justice.iam.domain.model.IamResource;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/1 10:13
 */

@Repository
public class ResourceRepository
{

    @Autowired
    private TblIamActionMapper actionMapper;

    @Autowired
    private TblIamCommonResourceMapper commonResourceMapper;

    @Autowired
    private TblIamResourceAttrMapper resourceAttrMapper;

    public List<TblIamAction> selectAllActions()
    {
        return actionMapper.selectAll();
    }

    public ResourceRsp getCommonResourceList(String name, Integer status, Integer pageSize, Integer pageNum)
    {
        PageHelper.startPage(pageNum, pageSize);
        List<TblIamCommonResourceSummary> tblIamCommonResources = commonResourceMapper.selectAllByName(name);
        PageInfo<TblIamCommonResourceSummary> pageInfo = new PageInfo<>(tblIamCommonResources);
        return ResourceRsp.builder().totalNum(pageInfo.getTotal())
                .services(tblIamCommonResources.stream().map(IamResource::of).collect(Collectors.toList())).build();
    }

    public IamResource selectById(String id)
    {
        TblIamCommonResourceDetail tblIamCommonResourceDetail = commonResourceMapper.selectById(id);
        if (Objects.nonNull(tblIamCommonResourceDetail))
        {
            return IamResource.of(tblIamCommonResourceDetail);
        }

        return null;
    }

    public int deleteByPrimaryKey(String id)
    {
        return commonResourceMapper.deleteByPrimaryKey(id);
    }

    public TblIamCommonResource selectByServiceIdAndName(String serviceId, String name)
    {
        return commonResourceMapper.selectByServiceIdAndName(serviceId, name);
    }

    public int insertSelective(TblIamCommonResource record)
    {
        return commonResourceMapper.insertSelective(record);
    }

    public int updateByPrimaryKeySelective(TblIamCommonResource record)
    {
        return commonResourceMapper.updateByPrimaryKeySelective(record);
    }

    public Integer countByResourceId(String resourceId)
    {
        return resourceAttrMapper.countByResourceId(resourceId);
    }

    public 	int deleteByResourceId(String resourceId)
    {
        Assert.notNull(resourceId, "resource id is required");
        return resourceAttrMapper.deleteByResourceId(resourceId);
    }

    public int batchInsertResourceAttrs(List<TblIamResourceAttr> list)
    {
        return resourceAttrMapper.batchInsert(list);
    }

    public  List<TblIamCommonResourceSummary> selectAllResources()
    {
        return commonResourceMapper.selectAll();
    }

    public int insertActionsSelective(TblIamAction record)
    {
        return actionMapper.insertSelective(record);
    }

    public int updateAction(TblIamAction record)
    {
        return actionMapper.updateByPrimaryKeySelective(record);
    }

    public TblIamAction selectActionByName(String actionName)
    {
        return actionMapper.selectByActionName(actionName);
    }

    public TblIamAction selectByResourceIdAndActionName(String resourceId, String actionName)
    {
        return actionMapper.selectByResourceIdAndActionName(resourceId, actionName);
    }

    public List<ActionsCache.ServiceAction> selectAllServiceActions()
    {
        return actionMapper.selectAllServiceActions();
    }
}
