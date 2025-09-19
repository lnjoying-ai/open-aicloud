package com.lnjoying.justice.schema.service.ums;

import com.lnjoying.justice.schema.common.UserMetaDTO;
import com.lnjoying.justice.schema.entity.iam.UserMetasRsp;
import io.swagger.annotations.ApiParam;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * ums rpc service
 *
 * @author merak
 **/
public interface UmsService {

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

    BpInfo getAllBps();

    List<String> getRolesByUserIdAndServiceCode(@ApiParam(name = "userId")String userId, @ApiParam(name = "serviceCode")String serviceCode);

    List<UserInfo> getUserInfosByRole(@ApiParam(name = "role")String role, @ApiParam(name = "serviceCode")String serviceCode);

    UserMetasRsp getUserMetaGroupByBpId(@ApiParam(name = "bpIds")List<String> bpIds, @ApiParam(name = "startDate")Date startDate,
                                        @ApiParam(name = "endDate")Date endDate,
                                        @ApiParam(name = "pageSize")Integer pageSize, @ApiParam(name = "pageNum")Integer pageNum,
                                        @ApiParam(name = "sortField")String sortField, @ApiParam(name = "sortDirection")String sortDirection,
                                        @ApiParam(name = "userId") String userId, @ApiParam(name = "userName")String userName);

    UserMetaDTO getUserMetaByBpId(@ApiParam(name = "bpId")String bpId, @ApiParam(name = "userId")String userId);

    String getRegisterMode();

    final class UserInfo implements Serializable
    {
        private String userId;

        private String bpId;

        private String userName;

        private String kind;

        private String email;

        public UserInfo(){};

        public UserInfo(String userId, String bpId, String userName, String kind, String email)
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

        public String getKind()
        {
            return kind;
        }

        public void setKind(String kind)
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

    long countBpIds();

    List<String> getAcBpIds();

    List<String> getUserIds();

    List<BpInfo> selectAllBps(@ApiParam(name = "createTime")Date createTime,@ApiParam(name = "pageNum") Integer pageNum,@ApiParam(name = "pageSize") Integer pageSize);

    final class BpInfo implements Serializable
    {

        private String bpId;

        private String bpName;

        private String masterUser;

        private Date createTime;

        public String getMasterUser()
        {
            return masterUser;
        }

        public void setMasterUser(String masterUser)
        {
            this.masterUser = masterUser;
        }

        public Date getCreateTime()
        {
            return createTime;
        }

        public void setCreateTime(Date createTime)
        {
            this.createTime = createTime;
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


    }
}
