package com.lnjoying.justice.aos.service.rpcserviceimpl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import com.lnjoying.justice.aos.common.StackState;
import com.lnjoying.justice.aos.db.model.TblStackSpecInfo;
import com.lnjoying.justice.aos.db.model.TblStackTemplateInfo;
import com.lnjoying.justice.aos.db.repo.StackRepository;
import com.lnjoying.justice.schema.entity.aos.AddStackReq;
import com.lnjoying.justice.aos.domain.dto.req.AddTemplateReq;
import com.lnjoying.justice.aos.domain.model.ConfigVariable;
import com.lnjoying.justice.aos.facade.StackServiceFacade;
import com.lnjoying.justice.aos.facade.TemplateServiceFacade;
import com.lnjoying.justice.aos.util.AosTemplateUtil;
import com.lnjoying.justice.commonweb.util.JacksonUtils;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.entity.RpcResult;
import com.lnjoying.justice.schema.entity.dev.TargetNode;
import com.lnjoying.justice.schema.service.aos.AosTemplateService;
import com.micro.core.common.Utils;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.servicecomb.provider.pojo.RpcSchema;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.lnjoying.justice.aos.service.rpcserviceimpl.AosTemplateServiceImpl.ResultCode.CREATE_STACK_ERROR;
import static com.lnjoying.justice.aos.util.StackUtils.*;
import static com.lnjoying.justice.schema.common.ErrorCode.TEMPLATE_CREATE_FAILED;
import static com.lnjoying.justice.schema.service.iam.CommonResourceFeederService.ResultCode.SUCCESS;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/10/27 20:38
 */

@Slf4j
@RpcSchema(schemaId = "aosTemplateService")
public class AosTemplateServiceImpl implements AosTemplateService
{

    @Autowired
    private TemplateServiceFacade templateServiceFacade;

    @Autowired
    private StackServiceFacade stackServiceFacade;

    @Autowired
    private StackRepository stackRepo;;

    @Override
    public int countStackByName(@ApiParam(name = "stackTemplateName") String stackTemplateName)
    {
        List<TblStackTemplateInfo> tblStackTemplateInfos = templateServiceFacade.selectBasicInfoByName(stackTemplateName);
        if (!CollectionUtils.isEmpty(tblStackTemplateInfos))
        {
            return tblStackTemplateInfos.size();
        }
        return 0;
    }

    @Override
    public int addStackTemplate(@ApiParam(name = "req")StackTemplateReq req)
    {
        try
        {
            AddTemplateReq addTemplateReq = new AddTemplateReq();
            BeanUtils.copyProperties(req, addTemplateReq);
            templateServiceFacade.addTemplate(addTemplateReq, req.getBpId(), null, req.getUserId());
            return ErrorCode.SUCCESS.getCode();
        }
        catch (Exception e)
        {
            log.error("add stack template error:{}", e);
            return TEMPLATE_CREATE_FAILED.getCode();
        }

    }

    @Override
    public StackTemplateParamsReq getStackTemplateParamsByName(@ApiParam(name = "stackTemplateName") String stackTemplateName)
    {
        try
        {
            List<TblStackTemplateInfo> tblStackTemplateInfos = templateServiceFacade.selectBasicInfoByName(stackTemplateName);
            if (!CollectionUtils.isEmpty(tblStackTemplateInfos))
            {
                StackTemplateParamsReq req = new StackTemplateParamsReq();
                TblStackTemplateInfo tblStackTemplateInfo = tblStackTemplateInfos.get(0);
                req.setStackTemplateId(tblStackTemplateInfo.getRootId());
                req.setStackTemplateVersionId(tblStackTemplateInfo.getTemplateId());
                req.setJusticeCompose(tblStackTemplateInfo.getJusticeCompose());
                req.setShowInputs(tblStackTemplateInfo.getShowInputs());
                return req;
            }
            return null;
        }
        catch (Exception e)
        {
            log.info("get stack template:{} params error:{}",stackTemplateName,  e);
            return null;
        }

    }


    @Override
    public RpcResult<List<StackInfoResp>> addStackSpec(@ApiParam(name = "stackTemplateName")String stackTemplateName,@ApiParam(name = "req") AddStackRpcReq req)
    {
        try
        {
            List<String> targetNodes = req.getTargetNodes();
            if (CollectionUtils.isEmpty(targetNodes))
            {
                // todo
                return new RpcResult(0, "target nodes is null", null);
            }

            String userId = req.getUserId();
            String bpId = req.getBpId();

            List<TblStackTemplateInfo> tblStackTemplateInfos = templateServiceFacade.selectBasicInfoByName(stackTemplateName);
            if (!CollectionUtils.isEmpty(tblStackTemplateInfos))
            {
                TblStackTemplateInfo tblStackTemplateInfo = tblStackTemplateInfos.get(0);
                AddStackReq localReq = new AddStackReq();

                List<StackInfoResp> stackInfosResp = targetNodes.stream().map(targetNode ->
                {
                    try
                    {
                        StackInfoResp stackInfoResp = doCreateStackSpec(stackTemplateName, req, userId, bpId, tblStackTemplateInfo, targetNode);
                        return stackInfoResp;
                    }
                    catch (Exception e)
                    {
                        log.error("add monitor stack:{} error:{}", stackTemplateName, e);
                    }

                   return null;
                }).filter(Objects::nonNull).collect(Collectors.toList());

                if (targetNodes.size() == stackInfosResp.size())
                {

                    return new RpcResult(SUCCESS.getCode(), "success", stackInfosResp);
                }
                else
                {
                    return new RpcResult(CREATE_STACK_ERROR.getCode(), "failed to create stack", null);
                }
            }


        }
        catch (Exception e)
        {
            log.info("create stack {} error:{}", stackTemplateName, e);
        }

        return new RpcResult(CREATE_STACK_ERROR.getCode(), "failed to create stack", null);
    }



    public enum ResultCode
    {

        SUCCESS(0),

        SYSTEM_ERROR(9999),

        CREATE_STACK_ERROR(9998),

        MISSING_PARAMETER(9997),

        SERVICE_NOT_UP(9996);

        private int code;

        ResultCode(int code)
        {
            this.code = code;
        }

        public int getCode()
        {
            return code;
        }

    }

    private StackInfoResp doCreateStackSpec(String stackTemplateName, AddStackRpcReq req, String userId, String bpId, TblStackTemplateInfo tblStackTemplateInfo, String targetNode)
    {

        TblStackSpecInfo specInfo = new TblStackSpecInfo();
        String uuid = Utils.assignUUId();
        specInfo.setSpecId(uuid);
        specInfo.setSpecName(uuid);
        specInfo.setCreateUserId(userId);
        specInfo.setBpId(bpId);
        specInfo.setUserId(userId);
        specInfo.setCreateTime(new Date());
        specInfo.setUpdateTime(specInfo.getCreateTime());
        specInfo.setDescription(stackTemplateName + " " + "created by the monitoring system");
        String stackCompose = tblStackTemplateInfo.getStackCompose();
        specInfo.setJusticeCompose(tblStackTemplateInfo.getJusticeCompose());

        if (CollectionUtils.isEmpty(req.getInputParams()))
        {
            specInfo.setStackCompose(stackCompose);
        }
        else
        {
            List<ConfigVariable> configVariables = JacksonUtils.strToObjTypeDefault(req.getInputParams().get(targetNode), new TypeReference<List<ConfigVariable>>() {});
            Map<String, String> inputParams = new HashMap<>();
            if (!CollectionUtils.isEmpty(configVariables))
            {

                configVariables.stream().forEach(configVariable -> {
                    inputParams.put(configVariable.getVariable(), configVariable.getDefaultValue());
                });
            }
            //Map<String, String> inputParams = JacksonUtils.strToObjTypeDefault(req.getInputParams().get(targetNode), new TypeReference<Map<String, String>>() {});
            stackCompose = AosTemplateUtil.format(specInfo.getSpecId(), stackCompose, inputParams);
            specInfo.setStackCompose(stackCompose);
        }

        specInfo.setTemplateId(tblStackTemplateInfo.getTemplateId());
        specInfo.setAutoRun(true);
        specInfo.setAosType("docker-compose");
        specInfo.setContentType("yaml");
        specInfo.setReplicaNum(1);
        specInfo.setDevNeedInfo(JsonUtils.toJson(addDefaultMonitorDevNeedInfo()));
        List<TargetNode> targetNodes = addTargetNode(targetNode);
        specInfo.setTargetNodes(JsonUtils.toJson(targetNodes));
        specInfo.setRegistryId("");
        specInfo.setSchedulingStrategy(JsonUtils.toJson(addDefaultSchedulingStrategy()));
        specInfo.setExtraParams(JsonUtils.toJson(addDefaultExtraParams()));
        List<String> stackComposeImages = getStackComposeImages(stackCompose);
        specInfo.setImageNames(JsonUtils.toJson(stackComposeImages));
        specInfo.setStatus(StackState.MAKED);
        // add default labels
        stackCompose = stackServiceFacade.addDefaultLabels(specInfo, stackCompose);
        stackRepo.insertStackSpecInfo(specInfo);

        List<String> stacks = stackServiceFacade.genStack(specInfo, true).stream().map(x -> x.getStackId())
                .collect(Collectors.toList());

        StackInfoResp stackInfoResp = new StackInfoResp();
        stackInfoResp.setStackId(stacks.get(0));
        TargetNode dstTargetNode = targetNodes.get(0);
        stackInfoResp.setRegionId(dstTargetNode.getDstRegionId());
        stackInfoResp.setSiteId(dstTargetNode.getDstSiteId());
        stackInfoResp.setNodeId(dstTargetNode.getDstNodeId());
        return stackInfoResp;
    }


}
