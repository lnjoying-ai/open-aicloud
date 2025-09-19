package com.lnjoying.justice.iam.rpcserviceimpl;

import com.lnjoying.justice.iam.domain.dto.request.project.AddProjectReq;
import com.lnjoying.justice.iam.service.ProjectService;
import com.lnjoying.justice.schema.entity.RpcResult;
import com.lnjoying.justice.schema.service.iam.IamProjectService;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.servicecomb.provider.pojo.RpcSchema;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import static com.lnjoying.justice.schema.service.iam.IamProjectService.ResultCode.SUCCESS;
import static com.lnjoying.justice.schema.service.iam.IamProjectService.ResultCode.SYSTEM_ERROR;


/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/5/8 10:09
 */

@RpcSchema(schemaId = "iamProjectService")
@Slf4j
public class IamProjectServiceImpl implements IamProjectService
{

    @Autowired
    private ProjectService projectService;

    @Override
    public RpcResult addProject(@ApiParam(name = "projectReq")ProjectReq projectReq)
    {

        try
        {
            AddProjectReq addProjectReq = new AddProjectReq();
            BeanUtils.copyProperties(projectReq, addProjectReq);
            String projectId = (String)projectService.addProject(addProjectReq);
            return new RpcResult(SUCCESS.getCode(), "success", projectId);
        }
        catch (Exception e)
        {
            log.error("create project failed, error:{}", e);
            return new RpcResult(SYSTEM_ERROR.getCode(), "create project failed," + e.getMessage(), null);
        }

    }
}
