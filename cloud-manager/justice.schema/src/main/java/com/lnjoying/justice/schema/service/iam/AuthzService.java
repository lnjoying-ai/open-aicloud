package com.lnjoying.justice.schema.service.iam;

import io.swagger.annotations.ApiParam;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/22 20:41
 */

public interface AuthzService
{
    AuthzResponse allow(@ApiParam(name = "requestInfo")RequestInfo requestInfo, @ApiParam(name = "jwtInfo")JwtInfo jwtInfo);

    final class RequestInfo implements Serializable
    {
        public RequestInfo() {}

        private String path;

        private String method;

        private String projectId;

        private Map<String, String> parameterMap;

        private Map<String, String> attributeMap;

        public String getPath()
        {
            return path;
        }

        public void setPath(String path)
        {
            this.path = path;
        }

        public String getMethod()
        {
            return method;
        }

        public void setMethod(String method)
        {
            this.method = method;
        }

        public String getProjectId()
        {
            return projectId;
        }

        public void setProjectId(String projectId)
        {
            this.projectId = projectId;
        }

        public Map<String, String> getParameterMap()
        {
            return parameterMap;
        }

        public void setParameterMap(Map<String, String> parameterMap)
        {
            this.parameterMap = parameterMap;
        }

        public Map<String, String> getAttributeMap()
        {
            return attributeMap;
        }

        public void setAttributeMap(Map<String, String> attributeMap)
        {
            this.attributeMap = attributeMap;
        }
    }

    final class JwtInfo implements Serializable
    {
        public JwtInfo(){}

        private String userId;

        private String userName;

        private String bpId;

        private String bpName;

        private String userKind;

        public String getUserId()
        {
            return userId;
        }

        public void setUserId(String userId)
        {
            this.userId = userId;
        }

        public String getUserName()
        {
            return userName;
        }

        public void setUserName(String userName)
        {
            this.userName = userName;
        }

        public String getBpId()
        {
            return bpId;
        }

        public void setBpId(String bpId)
        {
            this.bpId = bpId;
        }

        public String getBpName()
        {
            return bpName;
        }

        public void setBpName(String bpName)
        {
            this.bpName = bpName;
        }

        public String getUserKind()
        {
            return userKind;
        }

        public void setUserKind(String userKind)
        {
            this.userKind = userKind;
        }
    }


    final class AuthzResponse implements Serializable
    {

        public AuthzResponse(){}

        private boolean allow;

        private String detail;

        private Map<String, String> attributes;

        public boolean isAllow()
        {
            return allow;
        }

        public void setAllow(boolean allow)
        {
            this.allow = allow;
        }

        public String getDetail()
        {
            return detail;
        }

        public void setDetail(String detail)
        {
            this.detail = detail;
        }

        public Map<String, String> getAttributes()
        {
            return attributes;
        }

        public void setAttributes(Map<String, String> attributes)
        {
            this.attributes = attributes;
        }
    }
}
