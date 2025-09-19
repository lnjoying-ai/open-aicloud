package com.lnjoying.justice.iam.handler.actiondescription.i18n.zh_cn;

import com.lnjoying.justice.commonweb.handler.template.actiondescription.AbstractActionDescriptionTemplate;
import com.lnjoying.justice.commonweb.handler.template.constant.ActionDescriptionContextFields;
import com.lnjoying.justice.commonweb.mapper.CommonResourceCache;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.iam.common.constant.IamResources;
import com.lnjoying.justice.iam.db.model.TblIamPolicy;
import com.lnjoying.justice.iam.db.model.TblIamRole;
import com.lnjoying.justice.schema.constant.ResourceOperationTypeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.lnjoying.justice.iam.handler.actiondescription.i18n.zh_cn.AuthzRoleActionDescriptionTemplate.Descriptions.ROLE_ASSIGN_AUTHORIZATION;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2024年02月02日 14:30
 */
public class AuthzRoleActionDescriptionTemplate extends AbstractActionDescriptionTemplate
{

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthzRoleActionDescriptionTemplate.class);

    @Override
    public Optional<String> getDescriptionTemplate()
    {
        String operationType = getOperationType().orElse("");
        if (StringUtils.isEmpty(operationType))
        {
            return Optional.empty();
        }

        Optional<CommonResourceCache> commonResourceCacheOptional = getBean(CommonResourceCache.class);
        if (!commonResourceCacheOptional.isPresent())
        {
            LOGGER.debug("CommonResourceCache bean definition is not found");
            return Optional.empty();
        }

        CommonResourceCache commonResourceCache = commonResourceCacheOptional.get();
        TblIamRole tblIamRole = Optional.ofNullable(getContextData().get(ActionDescriptionContextFields.RESOURCE_INSTANCE))
                .map(x -> (TblIamRole) x)
                .orElse(null);
        if (tblIamRole == null)
        {
            return Optional.empty();
        }

        //填充角色名
        getContextData().put("_TMP_ROLE_NAME", tblIamRole.getRole());

        Object policyIds = getContextData().get("policyIds");
        if (policyIds != null && policyIds instanceof List)
        {
            List<String> policyIdList = (List<String>) policyIds;
            if (!CollectionUtils.isEmpty(policyIdList))
            {
                List<CompletableFuture<String>> furetureList = policyIdList.stream().map(policyId ->
                {

                    CompletableFuture<String> future = CompletableFuture.supplyAsync(() ->
                    {
                        Optional<Object> policyIdOptional = commonResourceCache.getResourceInstance(IamResources.POLICY, "selectByPrimaryKey", policyId);
                        if (policyIdOptional.isPresent())
                        {
                            TblIamPolicy tblIamPolicy = (TblIamPolicy) policyIdOptional.get();
                            return tblIamPolicy.getDisplayName();
                        }

                        return "";
                    });
                    return future;
                }).collect(Collectors.toList());

                List<String> policyNameList = furetureList.stream().map(CompletableFuture::join)
                        .filter(x -> StringUtils.hasText(x)).collect(Collectors.toList());
                getContextData().put("_TMP_POLICY_LIST", String.join(",", policyNameList));
            }
        }

        switch (operationType)
        {
            case ResourceOperationTypeConstants.RESOURCE_CREATE:
                return Optional.of(ROLE_ASSIGN_AUTHORIZATION);
            case ResourceOperationTypeConstants.RESOURCE_UPDATE:
                return Optional.of(Descriptions.ROLE_REVOKE_AUTHORIZATION);
        }

        return Optional.empty();
    }

    public interface Descriptions
    {
        String ROLE_ASSIGN_AUTHORIZATION = "角色 ${_TMP_ROLE_NAME} 添加授权策略: [${_TMP_POLICY_LIST}]";
        String ROLE_REVOKE_AUTHORIZATION = "角色 ${_TMP_ROLE_NAME} 取消授权策略: [${_TMP_POLICY_LIST}]";
    }
}
