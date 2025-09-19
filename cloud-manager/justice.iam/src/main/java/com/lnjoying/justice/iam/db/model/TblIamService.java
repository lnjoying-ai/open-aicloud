package com.lnjoying.justice.iam.db.model;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceInstanceName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "tbl_iam_service")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TblIamService
{
    @ApiModelProperty(value = "")
    private String id;

    @ApiModelProperty(value = "")
    private String name;

    @ApiModelProperty(value = "")
    @ResourceInstanceName
    private String displayName;

    @ApiModelProperty(value = "")
    private String iamCode;

    @ApiModelProperty(value = "")
    private String parentId;

    @ApiModelProperty(value = "")
    private String lrnFormat;

    @ApiModelProperty(value = "")
    private String lrnRegex;

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
    private String description;

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

    public enum ServiceEnableStatus
    {
        ENABLE(1),

        DISABLE(-1);

        private final int value;

        ServiceEnableStatus(int value)
        {
            this.value = value;
        }

        public int value()
        {
            return this.value;
        }
    }


}