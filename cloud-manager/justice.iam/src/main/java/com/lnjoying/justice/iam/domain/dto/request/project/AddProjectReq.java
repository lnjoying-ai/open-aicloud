package com.lnjoying.justice.iam.domain.dto.request.project;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.iam.config.ServiceConfig;
import com.lnjoying.justice.iam.db.model.TblIamProject;
import com.lnjoying.justice.iam.domain.dto.request.BaseReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;
import com.lnjoying.justice.iam.validation.Enum;

import static com.lnjoying.justice.iam.common.constant.ConstraintConstants.*;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/9 15:35
 */

@Data
@ApiModel(value = "AddProjectReq")
@JsonIgnoreProperties({"id", "bp_id", "user_id", "bp_name", "user_name", "create_time", "update_time"})
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AddProjectReq extends BaseReq
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
    @Enum(clazz = TblIamProject.ProjectEnableStatus.class, message = "enable(1:enable;-1:disable)")
    @ApiModelProperty(value="enable(1:enable;-1:disable)")
    private Integer enable;

    @ApiModelProperty(value="")
    private String parentId;

    /**
     * create time
     */
    @ApiModelProperty(value="create time")
    private Date createTime;

    /**
     * update time
     */
    @ApiModelProperty(value="update time")
    private Date updateTime;

}
