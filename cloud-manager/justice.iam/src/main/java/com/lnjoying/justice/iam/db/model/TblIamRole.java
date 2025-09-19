package com.lnjoying.justice.iam.db.model;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceInstanceName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "tbl_iam_role")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TblIamRole
{
    @ApiModelProperty(value = "")
    private Long roleId;

    @ApiModelProperty(value = "")
    private String platform;

    @ApiModelProperty(value = "")
    @ResourceInstanceName
    private String role;

    @ApiModelProperty(value = "")
    private String projectId;

    @ApiModelProperty(value = "")
    private String description;

    /**
     * role type(1:system;2:custom;)
     */
    @ApiModelProperty(value = "role type(1:system;2:custom;)")
    private Integer roleType;

    @ApiModelProperty(value = "")
    private String bpId;

    @ApiModelProperty(value = "")
    private String userId;

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

    public enum RoleType
    {
        SYSTEM(1),

        CUSTOM(2);

        private final int value;

        RoleType(int value)
        {
            this.value = value;
        }

        public int value()
        {
            return this.value;
        }
    }
}