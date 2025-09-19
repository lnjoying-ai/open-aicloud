package com.lnjoying.justice.iam.handler.actiondescription.i18n.zh_cn;

import com.lnjoying.justice.commonweb.handler.template.actiondescription.AbstractActionDescriptionTemplate;
import com.lnjoying.justice.commonweb.handler.template.constant.ActionDescriptionContextFields;
import com.lnjoying.justice.commonweb.mapper.CommonResourceCache;
import com.lnjoying.justice.commonweb.util.ApplicationUtils;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.iam.common.constant.IamResources;
import com.lnjoying.justice.iam.db.model.TblIamPolicy;
import com.lnjoying.justice.iam.db.model.TblIamRole;
import com.lnjoying.justice.iam.db.model.TblUserInfo;
import com.lnjoying.justice.schema.constant.ResourceOperationTypeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.lnjoying.justice.iam.handler.actiondescription.i18n.zh_cn.AuthzGroupActionDescriptionTemplate.Descriptions.USER_GROUP_ASSIGN_AUTHORIZATION;
import static com.lnjoying.justice.iam.handler.actiondescription.i18n.zh_cn.AuthzUserActionDescriptionTemplate.Descriptions.USER_ASSIGN_AUTHORIZATION;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2024年02月02日 14:30
 */
public class AuthzGroupActionDescriptionTemplate extends AbstractActionDescriptionTemplate
{

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthzGroupActionDescriptionTemplate.class);

    @Override
    public Optional<String> getDescriptionTemplate()
    {
//        String operationType = getOperationType().orElse("");
//        if (StringUtils.isEmpty(operationType))
//        {
//            return Optional.empty();
//        }
//
//        Optional<CommonResourceCache> commonResourceCacheOptional = getBean(CommonResourceCache.class);
//        if (!commonResourceCacheOptional.isPresent())
//        {
//            LOGGER.debug("CommonResourceCache bean definition is not found");
//            return Optional.empty();
//        }
//
//        CommonResourceCache commonResourceCache = commonResourceCacheOptional.get();
//        String userId = getDataFromContext(ActionDescriptionContextFields.RESOURCE_INSTANCE_ID);
//        Optional<Object> userOptional = commonResourceCache.getResourceInstance(IamResources.USER, "selectByPrimaryKey", userId);
//        if (!userOptional.isPresent())
//        {
//            LOGGER.debug("user instance is not found, userId: {}", userId);
//            return Optional.empty();
//        }
//
//        //填充用户组名
//        getContextData().put("_TMP_USER_NAME", ((TblUserInfo) userOptional.get()).getUserName());
//
//        Object policyIds = getContextData().get("policyIds");
//        if (policyIds != null && policyIds instanceof List)
//        {
//            List<String> policyIdList = (List<String>) policyIds;
//            if (!CollectionUtils.isEmpty(policyIdList))
//            {
//                List<CompletableFuture<String>> furetureList = policyIdList.stream().map(policyId ->
//                {
//
//                    CompletableFuture<String> future = CompletableFuture.supplyAsync(() ->
//                    {
//                        Optional<Object> policyIdOptional = commonResourceCache.getResourceInstance(IamResources.POLICY, "selectByPrimaryKey", policyId);
//                        if (policyIdOptional.isPresent())
//                        {
//                            TblIamPolicy tblIamPolicy = (TblIamPolicy) policyIdOptional.get();
//                            return tblIamPolicy.getDisplayName();
//                        }
//
//                        return "";
//                    });
//                    return future;
//                }).collect(Collectors.toList());
//
//                List<String> policyNameList = furetureList.stream().map(CompletableFuture::join)
//                        .filter(x -> StringUtils.hasText(x)).collect(Collectors.toList());
//                getContextData().put("_TMP_POLICY_LIST", String.join(",", policyNameList));
//            }
//        }
//
//        Object roleIds = getContextData().get("roleIds");
//        if (roleIds != null && roleIds instanceof List)
//        {
//            List<Long> roleIdList = (List<Long>) roleIds;
//            if (!CollectionUtils.isEmpty(roleIdList))
//            {
//                List<CompletableFuture<String>> furetureList = roleIdList.stream().map(roleId ->
//                {
//
//                    CompletableFuture<String> future = CompletableFuture.supplyAsync(() ->
//                    {
//                        Optional<Object> policyIdOptional = commonResourceCache.getResourceInstance(IamResources.ROLE, "selectByPrimaryKey", roleId);
//                        if (policyIdOptional.isPresent())
//                        {
//                            TblIamRole tblIamRole = (TblIamRole) policyIdOptional.get();
//                            return tblIamRole.getRole();
//                        }
//
//                        return "";
//                    });
//                    return future;
//                }).collect(Collectors.toList());
//
//                List<String> roleNameList = furetureList.stream().map(CompletableFuture::join)
//                        .filter(x -> StringUtils.hasText(x)).collect(Collectors.toList());
//                getContextData().put("_TMP_ROLE_LIST", String.join(",", roleNameList));
//            }
//        }
//
//
//        switch (operationType)
//        {
//            case ResourceOperationTypeConstants.RESOURCE_CREATE:
//                return Optional.of(USER_GROUP_ASSIGN_AUTHORIZATION);
//            case ResourceOperationTypeConstants.RESOURCE_UPDATE:
//                return Optional.of(Descriptions.USER_GROUP_REVOKE_AUTHORIZATION);
//        }

        return Optional.empty();
    }

    public interface Descriptions
    {
        String USER_GROUP_ASSIGN_AUTHORIZATION = "用户组 ${_TMP_USER_GROUP_NAME} 添加授权策略: [${_TMP_POLICY_LIST}],  角色: [${_TMP_ROLE_LIST}]";
        String USER_GROUP_REVOKE_AUTHORIZATION = "用户组 ${_TMP_USER_GROUP_NAME} 取消授权策略: [${_TMP_POLICY_LIST}],  角色: ${_TMP_ROLE_LIST}]";
    }
}
