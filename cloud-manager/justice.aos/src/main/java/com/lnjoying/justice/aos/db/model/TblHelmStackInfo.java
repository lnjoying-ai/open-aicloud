package com.lnjoying.justice.aos.db.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value="tbl_helm_stack_info")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TblHelmStackInfo implements Serializable
{

    private static final long serialVersionUID = -9042024454960792539L;

    public static final String PREFIX = "HM-";


    /**
    * clsid+deploy name
    */
    @ApiModelProperty(value="clsid+deploy name")
    private String stackId;

    /**
    * release name
    */
    @ApiModelProperty(value="release name")
    private String releaseName;

    /**
    * app id
    */
    @ApiModelProperty(value="app id")
    private String appId;

    /**
    * chart name
    */
    @ApiModelProperty(value="chart name")
    private String chartName;

    /**
    * chart version
    */
    @ApiModelProperty(value="chart version")
    private String chartVersion;

    /**
    * chart package name(xxx.tgz)
    */
    @ApiModelProperty(value="chart package name(xxx.tgz)")
    private String chartPackageName;

    /**
    * description
    */
    @ApiModelProperty(value="description")
    private String description;

    /**
    * status(1:creating;2:runnning;3:stop;4:deploy failed;5:app cleared;6:partial run)
    */
    @ApiModelProperty(value="status(1:creating;2:runnning;3:stop;4:deploy failed;5:app cleared;6:partial run)")
    private Integer status;

    /**
    * chart config
    */
    @ApiModelProperty(value="chart config")
    private String stackCompose;

    /**
    * cluster id
    */
    @ApiModelProperty(value="cluster id")
    private String clusterId;

    /**
    * cluster name
    */
    @ApiModelProperty(value="cluster name")
    private String clusterName;

    /**
    * namespace
    */
    @ApiModelProperty(value="namespace")
    private String namespace;

    /**
     * labels
     */
    @ApiModelProperty(value="labels")
    private Object labels;


    /**
    * owner id
    */
    @ApiModelProperty(value="owner id")
    private String ownerId;

    /**
    * owner name
    */
    @ApiModelProperty(value="owner name")
    private String ownerName;

    /**
     * owner id
     */
    @ApiModelProperty(value="owner id")
    private String ownerBpId;

    /**
     * owner name
     */
    @ApiModelProperty(value="owner name")
    private String ownerBpName;

    /**
    * creator id
    */
    @ApiModelProperty(value="creator id")
    private String creatorId;

    /**
    * creator name
    */
    @ApiModelProperty(value="creator name")
    private String creatorName;

    @ApiModelProperty(value="")
    private Date createTime;

    @ApiModelProperty(value="")
    private Date updateTime;

    public enum StackStatus
    {
        // status(1:creating;2:runnning;3:stop;4:deploy failed;5:app cleared;6:partial run;7:unknown)

        CREATING(1),

        RUNNING(2),

        STOP(3),

        DEPLOY_FAILED(4),

        APP_CLEARED(5),

        PARTIAL_RUN(6),

        UNKNOWN(7);

        private final int value;

        StackStatus(int value)
        {
            this.value = value;
        }

        public int value()
        {
            return value;
        }
    }

    public enum ActionType
    {
        /**
         * upgrade a release
         */
        UPGRADE(0),

        /**
         * roll back a release to a previous revision
         */
        ROLLBACK(1);

        private final int value;

        ActionType(int value)
        {
            this.value= value;
        }

        public int value()
        {
            return this.value;
        }

        public static ActionType fromValue(int value)
        {
            return Arrays.stream(ActionType.values()).filter(x -> x.value() == value).findFirst().get();
        }
    }
}