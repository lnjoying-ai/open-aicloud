package com.lnjoying.justice.iam.db.model;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceInstanceName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "tbl_iam_project")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TblIamProject
{
    @ApiModelProperty(value = "")
    private String id;

    /**
     * project name
     */
    @ApiModelProperty(value = "project name")
    private String name;

    @ApiModelProperty(value = "")
    @ResourceInstanceName
    private String displayName;

    @ApiModelProperty(value = "")
    private String description;

    /**
     * status(1:normal;-1:delete)
     */
    @ApiModelProperty(value = "status(1:normal;-1:delete)")
    private Integer status;

    /**
     * enable(1:enable;-1:disable)
     */
    @ApiModelProperty(value = "enable(1:enable;-1:disable)")
    private Integer enable;

    @ApiModelProperty(value = "")
    private String parentId;

    /**
     * create time
     */
    @ApiModelProperty(value = "create time")
    private Date createTime;

    /**
     * update time
     */
    @ApiModelProperty(value = "update time")
    private Date updateTime;

    @ApiModelProperty(value = "")
    private String bpId;

    @ApiModelProperty(value = "")
    private String userId;

    /**
     * type(system:0;admin:1; bp:2;personal:3;bp_user:4;default:10;other:20;)
     */
    @ApiModelProperty(value = "type(system:0;admin:1; bp:2;personal:3;bp_user:4;default:10;other:20;)")
    private Integer type;

    public enum ProjectEnableStatus
    {
        ENABLE(1),

        DISABLE(-1);

        private final int value;

        ProjectEnableStatus(int value)
        {
            this.value = value;
        }

        public int value()
        {
            return this.value;
        }
    }

    public enum ProjectType
    {
        // temporarily not used
        SYSTEM(0),

        ADMIN(1),

        BP(2),

        PERSONAL(3),

        BP_USER(4),

        BP_DEFAULT(10),

        ADMIN_DEFAULT(11),

        OTHER(20);

        private final int value;

        ProjectType(int value)
        {
            this.value = value;
        }

        public int value()
        {
            return this.value;
        }
    }
}