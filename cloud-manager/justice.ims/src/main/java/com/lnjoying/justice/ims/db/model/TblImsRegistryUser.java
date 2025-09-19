package com.lnjoying.justice.ims.db.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@ApiModel(value = "TblImsRegistryUser")
@Data
@Builder
public class TblImsRegistryUser
{
    /**
     * registry user id
     */
    @ApiModelProperty(value = "registry user id")
    private String registryUserId;

    /**
     * registry user name
     */
    @ApiModelProperty(value = "registry user name")
    private String registryUserName;

    /**
     * registry user password
     */
    @ApiModelProperty(value = "registry user password")
    private String registryUserPassword;

    /**
     * registry user old password ,
     * Used to maintain password consistency when synchronizing data to multiple registry
     */
    @ApiModelProperty(value = "registry user password")
    private String registryUserOldPassword;

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
     * record status(0:creating;1:partially completed;2:completed:-1:deleted)
     */
    @ApiModelProperty(value = "record status(0:creating;1:partially completed;2:completed:-1:deleted)")
    private Integer status;

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

    public enum RegistryUserStatus
    {
        /**
         * The record was created, but it was not synchronized to the registry
         */
        CREATING(0),
        /**
         * The user has been synchronized to some registry
         */
        PARTIALLY_COMPLETED(1),
        /**
         * The user has been synchronized to all registry
         */
        COMPLETED(2),
        /**
         * mark user is deleted
         */
        DELETED(-1);

        private final int value;

        RegistryUserStatus(int value)
        {
            this.value = value;
        }

        public int value()
        {
            return this.value;
        }
    }
}