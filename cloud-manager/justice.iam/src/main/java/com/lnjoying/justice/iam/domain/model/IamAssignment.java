package com.lnjoying.justice.iam.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/17 9:46
 */

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IamAssignment
{

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static final class IamUserAssignment
    {
        /**
         * projectId
         */
        @ApiModelProperty(value="projectId")
        private String projectId;

        /**
         * userId
         */
        @ApiModelProperty(value="userId")
        private String userId;

        /**
         * userName
         */
        @ApiModelProperty(value="userName")
        private String userName;


        private String email;

        /**
         * create time
         */
        @ApiModelProperty(value="create time")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
        private Date createTime;

    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static final class IamGroupAssignment
    {
        /**
         * projectId
         */
        @ApiModelProperty(value="projectId")
        private String projectId;

        /**
         * groupId
         */
        @ApiModelProperty(value="groupId")
        private String groupId;

        /**
         * groupName
         */
        @ApiModelProperty(value="groupName")
        private String groupName;
    }

}
