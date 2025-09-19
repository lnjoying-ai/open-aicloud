package com.lnjoying.justice.schema.service.aos;

import com.lnjoying.justice.schema.entity.RpcResult;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/10/27 20:36
 */

public interface AosTemplateService
{

    int countStackByName(@ApiParam(name = "stackTemplateName") String stackTemplateName);

    StackTemplateParamsReq getStackTemplateParamsByName(@ApiParam(name = "stackTemplateName") String stackTemplateName);



    int addStackTemplate(@ApiParam(name = "req")StackTemplateReq req);

    RpcResult<List<StackInfoResp>> addStackSpec(@ApiParam(name = "stackTemplateName")String stackTemplateName,@ApiParam(name = "req") AddStackRpcReq req);

    @Data
    public static class StackTemplateReq
    {
        private String name;
        private String version;
        private String description;
        private String logo_url;
        private String vendor;
        private String aos_type;
        private String content_type;
        private String stack_compose;
        private String justice_compose;
        private List<String> labels;
        private String bpId;
        private String userId;
    }

    @Data
    public static class StackTemplateParamsReq
    {
        private String justiceCompose;

        private String showInputs;

        private String stackTemplateId;

        private String stackTemplateVersionId;
    }

    @Data
    public static class AddStackRpcReq
    {
        private String name;

        private String description;

        private String templateId;

        private Map<String, String> inputParams;

        List<String> labels;

        Boolean autoRun;

        String bpId;

        String userId;

        List<String> targetNodes;


    }


    @Data
    public static class StackInfoResp
    {
        private String stackId;

        private String regionId;

        private String siteId;

        private String nodeId;
    }
}
