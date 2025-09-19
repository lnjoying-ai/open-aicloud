package com.lnjoying.justice.iam.domain.dto.response.authz;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.iam.authz.opa.pep.User;
import com.lnjoying.justice.iam.domain.dto.response.role.TblIamRoleDetail;
import com.lnjoying.justice.iam.domain.model.IamRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/14 10:16
 */

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@ApiModel(value = "AttachmentEntitiesRsp")
public class AttachmentEntitiesRsp
{

    private List<User> users;

    private List<Role> roles;

    private List<Group> groups;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static final class User
    {
        private String userId;

        private String userName;

        private String email;

        private String phone;

        private String bpId;

        private String bpName;

        /**
         * create time
         */
        @ApiModelProperty(value = "create time")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
        private Date createTime;

        /**
         * update time
         */
        @ApiModelProperty(value = "update time")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
        private Date updateTime;

    }


    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static final class Role extends IamRole
    {
        public static Role of (TblIamRoleDetail tblIamRoleDetail)
        {
            Role iamRole = new Role();
            BeanUtils.copyProperties(tblIamRoleDetail, iamRole);
            return iamRole;
        }
    }

    @Data
    public static final class Group
    {
        private String groupId;

        private String groupName;
    }
}
