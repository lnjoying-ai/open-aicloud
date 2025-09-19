package com.lnjoying.justice.iam.rpcserviceimpl;

import com.lnjoying.justice.iam.authz.opa.pep.*;
import com.lnjoying.justice.iam.authz.opa.pep.pathMatcher.MockHttpServletRequest;
import com.lnjoying.justice.iam.authz.opa.pep.pathMatcher.SwaggerRequestMapping;
import com.lnjoying.justice.iam.db.model.TblIamProject;
import com.lnjoying.justice.iam.db.repo.ProjectRepository;
import com.lnjoying.justice.iam.domain.model.ActionsCache;
import com.lnjoying.justice.iam.utils.ServiceCombRequestUtils;
import com.lnjoying.justice.schema.service.iam.AuthzService;
import com.lnjoying.justice.schema.service.iam.CommonResourceService;
import com.micro.core.common.Pair;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.servicecomb.provider.pojo.Invoker;
import org.apache.servicecomb.provider.pojo.RpcSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.util.*;

import static com.lnjoying.justice.iam.common.constant.IamConstants.IAM_SERVICE_CODE;
import static com.lnjoying.justice.schema.constant.UserHeadInfo.PROJECT;
import static com.lnjoying.justice.schema.constant.UserHeadInfo.PROJECT_CHAIN;
import static com.lnjoying.justice.schema.service.iam.CommonResourceService.DEFAULT_SCHEMA_ID;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/22 20:40
 */

@RpcSchema(schemaId = "authzService")
@Slf4j
public class AuthzServiceImpl implements AuthzService
{

    @Autowired
    private Enforcer opaEnforcer;

    @Autowired
    private SwaggerRequestMapping swaggerRequestMapping;

    @Autowired
    private ActionsCache actionsCache;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private CommonResourceService commonResourceService;

    @Override
    public AuthzResponse allow(@ApiParam(name = "requestInfo")RequestInfo requestInfo, @ApiParam(name = "jwtInfo")JwtInfo jwtInfo)
    {
        AuthzResponse authzResponse = new AuthzResponse();
        AuthzResponse checkParamsResponse = checkParams(requestInfo);
        if (Objects.nonNull(checkParamsResponse))
        {
            return checkParamsResponse;
        }

        // directly use the kind authentication
        if(checkAdmin(jwtInfo))
        {
            authzResponse.setAllow(true);
            authzResponse.setDetail("");
            assembleAuthzResponse(null, requestInfo, authzResponse, true);
            return authzResponse;
        }

        User user = buildUser(jwtInfo);

        // left:actionName right:uriVariables
        Pair<String, Map<String, String>> actionInfo = getAction(requestInfo);
        String actionName = actionInfo.getLeft();
        if (StringUtils.isBlank(actionName))
        {
            authzResponse.setAllow(false);
            authzResponse.setDetail("the action not exist");
            return authzResponse;
        }

        ActionsCache.ServiceAction serviceAction = actionsCache.get(actionName);
        if (Objects.isNull(serviceAction))
        {
            authzResponse.setAllow(false);
            authzResponse.setDetail("the action not exist");
            return authzResponse;
        }

        Resource resource = new Resource();
        String bpId = jwtInfo.getBpId();
        if (StringUtils.isNotBlank(bpId))
        {
            resource = buildResource(actionInfo, serviceAction, bpId);
        }

        AccessDecision check = opaEnforcer.check(user, actionName, resource);
        if (Objects.nonNull(check))
        {
            authzResponse.setAllow(check.isAllow());
            authzResponse.setDetail(check.getReason());
            assembleAuthzResponse(resource, requestInfo, authzResponse, false);
        }
        else
        {
            authzResponse.setAllow(false);
            authzResponse.setDetail("unknown reason");
        }
        return authzResponse;
    }


    private boolean checkAdmin(JwtInfo jwtInfo)
    {
        try
        {
            String userKind = jwtInfo.getUserKind();
            if (StringUtils.isNotBlank(userKind))
            {
                return ServiceCombRequestUtils.isAdmin(userKind);
            }
        }
        catch (Exception e)
        {
            log.error("check admin failed, params:{}, error:{}", jwtInfo, e);
        }

        return false;
    }

    private AuthzResponse checkParams(RequestInfo requestInfo)
    {
        AuthzResponse authzResponse = new AuthzResponse();
        if (Objects.isNull(requestInfo))
        {
            authzResponse.setAllow(false);
            authzResponse.setDetail("request info is null");
            return authzResponse;
        }
        else
        {
            if (StringUtils.isBlank(requestInfo.getPath()))
            {
                authzResponse.setAllow(false);
                authzResponse.setDetail("request path is null");
                return authzResponse;
            }
        }

        return null;
    }


    private Pair<String, Map<String, String>> getAction(RequestInfo requestInfo)
    {
        try
        {
            String actionName = "";
            Map<String, String> uriVariables = new HashMap<>();

            MockHttpServletRequest request = new MockHttpServletRequest(StringUtils.upperCase(requestInfo.getMethod()), requestInfo.getPath());
            RequestMappingInfo requestMappingInfo = swaggerRequestMapping.lookUp(request);
            String name = HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE;
            uriVariables = (Map<String, String>) request.getAttribute(name);

            if (Objects.isNull(requestMappingInfo))
            {
                return new Pair(actionName, uriVariables);
            }
            else
            {
                actionName = requestMappingInfo.getName();
                String[] iamCodeAndAction = StringUtils.split(actionName, ":");
                if (iamCodeAndAction.length != 2)
                {
                    actionName = "";
                    log.error("illegal action name:{}", actionName);
                }
                return new Pair(actionName, uriVariables);
            }
        }
        catch (Exception e)
        {
            log.error("get action failed, params:{}, error:{}", requestInfo, e);
            return new Pair("", new HashMap<>());
        }

    }

    private Resource buildResource(Pair<String, Map<String, String>> actionInfo, ActionsCache.ServiceAction serviceAction, String bpId)
    {
        String actionName = actionInfo.getLeft();
        Map<String, String> uriVariables = actionInfo.getRight();
        Resource resource = new Resource();

        Map<String, String> resourceAttributes = new HashMap<>();
        if (!CollectionUtils.isEmpty(uriVariables))
        {
            resourceAttributes.putAll(uriVariables);
        }

        String[] iamCodeAndAction = StringUtils.split(actionName, ":");
        Map<String, Object> resourceInfo = new HashMap<>();
        if (iamCodeAndAction.length == 2)
        {
            Map<String, Object> resourceMap = getResourceAttributes(iamCodeAndAction[0], serviceAction.getResourceName(), resourceAttributes);
            resourceInfo.putAll(resourceMap);
        }
        else
        {
            log.error("illegal action name:{}", actionName);
        }

        resource.setType(serviceAction.getResourceType());
        String project = (String)Optional.ofNullable(resourceInfo).map(map -> map.get("projectId")).orElseGet(() ->
        {
            List<TblIamProject> tblIamProjects = projectRepository.selectByNameAndBpId(null, bpId, TblIamProject.ProjectType.BP_DEFAULT.value());
            if (!CollectionUtils.isEmpty(tblIamProjects))
            {
                String projectId = tblIamProjects.get(0).getId();
                return projectId;
            }
            return "";
        });

        resourceInfo.put("project", project);
        resource.setProject(project);
        resource.setAttributes(resourceInfo);
        return resource;
    }

    private static User buildUser(JwtInfo jwtInfo)
    {
        User user = new User();
        user.setKey(jwtInfo.getUserId());
        user.setBp(jwtInfo.getBpId());
        user.setKind(jwtInfo.getUserKind());
        Map<String, String> userAttributes = new HashMap<>();
        // userAttributes.put("bpId", jwtInfo.getBpId());
        userAttributes.put("bpName", jwtInfo.getBpName());
        // userAttributes.put("kind", jwtInfo.getUserKind());
        userAttributes.put("userName", jwtInfo.getUserName());
        user.setAttributes(userAttributes);
        return user;
    }


    private Map<String, Object> getResourceAttributes(String iamCode, String resourceName, Map<String, String> resourceInfo)
    {
        try
        {
            if (IAM_SERVICE_CODE.equals(iamCode))
            {
                Map<String, Object> resourceInfos = commonResourceService.commonResource(resourceName, resourceInfo);
                if (!CollectionUtils.isEmpty(resourceInfos))
                {
                    return resourceInfos;
                }
            }
            else
            {
                CommonResourceService proxy = Invoker.createProxy(iamCode, DEFAULT_SCHEMA_ID, CommonResourceService.class);
                Map<String, Object> resourceInfos = proxy.commonResource(resourceName, resourceInfo);
                if (!CollectionUtils.isEmpty(resourceInfos))
                {
                    return resourceInfos;
                }
            }

        }
        catch (Exception e)
        {
            log.error("invoke service:{}, resourceName:{} failed", iamCode, resourceName);
        }

        return Collections.emptyMap();
    }



    private void assembleAuthzResponse(Resource resource, RequestInfo requestInfo, AuthzResponse authzResponse, boolean isAdmin)
    {
        String projectId = Optional.ofNullable(requestInfo.getProjectId()).orElseGet(() ->
        {
            if (Objects.nonNull(resource))
            {
                return resource.getProject();
            }
            else
            {
                if (isAdmin)
                {
                   return projectRepository.selectAdminDefaultProjectId();
                }

            }

            return "";
        });
        Map<String, String> attributes = authzResponse.getAttributes();
        if (CollectionUtils.isEmpty(attributes))
        {
            attributes = new HashMap<>();
            authzResponse.setAttributes(attributes);
        }

        attributes.put(PROJECT, projectId);
        // todo Parent-child relationships and trees need to be considered
        attributes.put(PROJECT_CHAIN, "");

    }
}
