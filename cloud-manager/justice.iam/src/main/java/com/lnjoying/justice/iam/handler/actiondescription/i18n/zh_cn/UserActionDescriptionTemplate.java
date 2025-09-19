package com.lnjoying.justice.iam.handler.actiondescription.i18n.zh_cn;

import com.lnjoying.justice.commonweb.handler.template.actiondescription.AbstractActionDescriptionTemplate;

import java.util.Optional;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2024年01月30日 16:55
 */
public class UserActionDescriptionTemplate extends AbstractActionDescriptionTemplate
{
    @Override
    public Optional<String> getDescriptionTemplate()
    {
        return Optional.empty();
    }

    public interface Descriptions
    {
        String ADD_USER = "添加用户 ${RESOURCE_INSTANCE_NAME},  归属组织id:${bp_id}";
        String UPDATE_USER = "更新用户 ${RESOURCE_INSTANCE_NAME},  归属组织id:${bp_id}";
        String DELETE_USER = "删除用户 ${RESOURCE_INSTANCE_NAME},  归属组织id:${RESOURCE_INSTANCE.bpId}";

        String REGISTER = "新增注册用户 ${RESOURCE_INSTANCE_NAME}";
        String RETRIEVE_PASSWORD = "用户 ${USER_NAME} 重置密码";
        String UPDATE_PASSWORD = "用户 ${USER_NAME} 更新密码";
        String UPDATE_CURRENT_USER = "用户 ${USER_NAME} 更新个人信息";
        String UPDATE_PHONE = "用户 ${USER_NAME} 更新手机号";
        String UPDATE_EMAIL = "用户 ${USER_NAME} 更新邮箱";
        String UPDATE_PHONE_VIA_WECHAT = "用户 ${USER_NAME} 通过微信端修改手机号";
        String UPDATE_INFO_VIA_WECHAT = "用户 ${USER_NAME} 通过微信端修改个人信息";
    }
}
