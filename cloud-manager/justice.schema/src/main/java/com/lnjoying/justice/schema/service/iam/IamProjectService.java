package com.lnjoying.justice.schema.service.iam;

import com.lnjoying.justice.schema.entity.RpcResult;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;

import java.util.Date;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/5/8 10:05
 */

public interface IamProjectService
{
    public RpcResult addProject(@ApiParam(name = "projectReq")ProjectReq projectReq);

    public class ProjectReq
    {
        @ApiModelProperty(value="")
        private String id;

        /**
         * project name
         */
        //@NotBlank(message = "project name must not blank")
        //@Pattern(regexp = PATTERN_NAME, message = MESSAGE_NAME)
        @ApiModelProperty(value="project name")
        private String name;

        @ApiModelProperty(value="")
        private String displayName;

        @ApiModelProperty(value="")
        private String description;

        /**
         * enable(1:enable;-1:disable)
         */
        @ApiModelProperty(value="enable(1:enable;-1:disable)")
        private Integer enable = 1;

        @ApiModelProperty(value="")
        private String parentId;

    }


    public enum ResultCode
    {

        SUCCESS(0),

        SYSTEM_ERROR(9999);

        private int code;

        ResultCode(int code)
        {
            this.code = code;
        }

        public int getCode()
        {
            return code;
        }
    }
    }
