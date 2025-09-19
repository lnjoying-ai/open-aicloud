package com.lnjoying.justice.ims.db.model;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceInstanceName;
import com.lnjoying.justice.ims.exception.ImsWebSystemException;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * registry project
 */
@ApiModel(value = "TblImsRegistryProject")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TblImsRegistryProject
{
    /**
     * project id
     */
    @ApiModelProperty(value = "project id")
    private String projectId;

    /**
     * project name
     */
    @ApiModelProperty(value = "project name")
    @ResourceInstanceName
    private String projectName;

    /**
     * project descripion
     */
    @ApiModelProperty(value = "project descripion")
    private String projectDesc;

    /**
     * registry id
     */
    @ApiModelProperty(value = "registry id")
    private String registryId;

    /**
     * scope(0:private;1:bp;2:public)
     */
    @ApiModelProperty(value = "scope(0:private;1:bp;2:public)")
    private Integer scope;

    /**
     * record status(1:normal;-1:deleted)
     */
    @ApiModelProperty(value = "record status(1:normal;-1:deleted)")
    private Integer status;

    /**
     * user id
     */
    @ApiModelProperty(value = "user id")
    private String userId;

    /**
     * bp id
     */
    @ApiModelProperty(value = "bp id")
    private String bpId;

    /**
     * create time
     */
    @ApiModelProperty(value = "create time")
    private LocalDateTime createTime;

    /**
     * update time
     */
    @ApiModelProperty(value = "update time")
    private LocalDateTime updateTime;

    /**
     * user name
     */
    @ApiModelProperty(value = "user name")
    private String userName;

    /**
     * bp name
     */
    @ApiModelProperty(value = "bp name")
    private String bpName;

    public enum ProjectScope
    {
        /**
         * private： Only available for creating user
         */
        PRIVATE(0),

        /**
         * bp: bp user visible
         */
        BP(1),

        /**
         * public: All users can access
         */
        PUBLIC(2);
        private final int value;

        ProjectScope(int value)
        {
            this.value = value;
        }

        public int value()
        {
            return this.value;
        }

        public static ProjectScope fromValue(int value) throws ImsWebSystemException
        {
            switch (value)
            {
                case 0:
                    return PRIVATE;
                case 1:
                    return BP;
                case 2:
                    return PUBLIC;
                default:
                    throw new ImsWebSystemException(ErrorCode.ILLEGAL_ARGUMENT, ErrorLevel.INFO);
            }
        }
    }

}