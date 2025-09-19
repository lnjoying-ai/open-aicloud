package com.lnjoying.justice.ims.db.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceInstanceName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * region image registry table
 */
@ApiModel(value = "TblImsRegistry")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TblImsRegistry
{

    public static final int UNAVAILABLE = 4;
    /**
     * registry id
     */
    @ApiModelProperty(value = "registry id")
    private String registryId;

    /**
     * registry name
     */
    @ApiModelProperty(value = "registry name")
    @ResourceInstanceName
    private String registryName;

    /**
     * registry desc
     */
    @ApiModelProperty(value = "registry desc", hidden = true)
    private String registryDesc;

    /**
     * registry type(0:harbor;1:docker-hub)
     */
    @ApiModelProperty(value = "registry type(0:harbor;1:docker-hub)")
    private Integer type;

    /**
     * registry url
     */
    @ApiModelProperty(value = "registry url")
    private String url;

    /**
     * administrator account
     */
    @ApiModelProperty(value = "administrator account")
    private String adminName;

    /**
     * administrator password
     */
    @ApiModelProperty(value = "administrator password", hidden = true)
    private String adminPasswd;

    /**
     * user id
     */
    @ApiModelProperty(value = "user id")
    private String userId;

    /**
     * status(1:creating;2:failed;3:succeed;4:unhealthy;
     * 5:healthy;6:enable;7:disable;-1:deleted
     * )
     */
    @ApiModelProperty(value = "status(1:creating;2:failed;3:succeed;4:unhealthy;,5:healthy;6:enable;7:disable;-1:deleted,)")
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

    public enum RegistryStatus
    {
        /**
         * creating when init registry
         */
        CREATING(1),

        /**
         * failed when ping timeout
         */
        FAILED(2),

        /**
         * succeed when ping registry return pong
         */
        SUCCEED(3),

        /**
         * unhealthy when can not login registry
         */
        UNHEALTHY(4),

        /**
         * healthy when login registry successful
         */
        HEALTHY(5),

        /**
         * enable when registry is available
         */
        ENABLE(6),

        /**
         * disable when disabled by administrator
         */
        DISABLE(7),

        /**
         * mark registry is deleted
         */
        DELETED(-1);

        private final int value;

        RegistryStatus(int value)
        {
            this.value = value;
        }

        public int value()
        {
            return this.value;
        }

        /**
         * States other than enable deleted
         * @return
         */
        public static Collection<Integer> otherStatus()
        {
            List<Integer> statusCollection = new ArrayList<>();
            statusCollection.add(CREATING.value);
            statusCollection.add(FAILED.value);
            statusCollection.add(SUCCEED.value);
            statusCollection.add(UNHEALTHY.value);
            statusCollection.add(HEALTHY.value);
            statusCollection.add(DISABLE.value);
            return statusCollection;
        }
    }

    public enum RegistryType
    {
        /**
         * harbor
         */
        HARBOR(0),

        /**
         * docker-hub
         */
        DOCKERHUB(1);

        private final int value;

        RegistryType(int value)
        {
            this.value = value;
        }

        public int value()
        {
            return this.value;
        }
    }

}