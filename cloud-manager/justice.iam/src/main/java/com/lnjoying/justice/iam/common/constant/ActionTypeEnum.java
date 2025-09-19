package com.lnjoying.justice.iam.common.constant;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2024/8/23 10:37
 */

public enum ActionTypeEnum
{
    LOGIN("login", "login", "登录"),

    LOGOUT("logout", "logout", "登出");

    private String actionEn;

    private String actionCn;

    private String resource;

    ActionTypeEnum(String resource, String actionEn, String actionCn) {
        this.resource = resource;
        this.actionEn = actionEn;
        this.actionCn = actionCn;
    }

    public String getActionEn()
    {
        return actionEn;
    }

    public String getActionCn()
    {
        return actionCn;
    }


    public String getResource()
    {
        return resource;
    }

}
