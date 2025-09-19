package com.lnjoying.justice.schema.service.iam;

import io.swagger.annotations.ApiParam;

import java.io.Serializable;
import java.util.List;

/**
 * ums rpc service
 * @deprecated use ums UmsService
 * @author merak
 **/
public interface IamService
{

    /**
     * Get all roles owned by the current user
     * @param userId
     * @return
     */
    List<String> getRolesByUserId(@ApiParam(name = "userId")String userId);

    boolean isBpUser(@ApiParam(name = "bpId")String bpId, @ApiParam(name = "userId")String userId);

    /**
     * get user name by user id
     * @param userId
     * @return
     */
    String  getUserNameByUserId(@ApiParam(name = "userId")String userId);

    /**
     * get bp name by bp id
     * @param bpId
     * @return
     */
    String  getBpNameByBpId(@ApiParam(name = "bpId")String bpId);

    UserInfo getUserInfoByUseId(@ApiParam(name = "userId")String userId);

    UserInfo getMasterUserInfoByBpId(@ApiParam(name = "bpId")String bpId);

    final class UserInfo implements Serializable
    {
        private String userId;

        private String bpId;

        private String userName;

        private Integer kind;

        private String email;

        public UserInfo(){};

        public UserInfo(String userId, String bpId, String userName, Integer kind, String email)
        {
            this.userId = userId;
            this.bpId = bpId;
            this.userName = userName;
            this.kind = kind;
            this.email = email;
        }

        public String getUserId()
        {
            return userId;
        }

        public void setUserId(String userId)
        {
            this.userId = userId;
        }

        public String getBpId()
        {
            return bpId;
        }

        public void setBpId(String bpId)
        {
            this.bpId = bpId;
        }

        public String getUserName()
        {
            return userName;
        }

        public void setUserName(String userName)
        {
            this.userName = userName;
        }

        public Integer getKind()
        {
            return kind;
        }

        public void setKind(Integer kind)
        {
            this.kind = kind;
        }

        public String getEmail()
        {
            return email;
        }

        public void setEmail(String email)
        {
            this.email = email;
        }
    }

    int getBpNum(@ApiParam(name = "userId")String userId, @ApiParam(name = "bpId")String bpId);

    List<String> getBpIds();

    List<String> getUserIds();
}
