package com.lnjoying.justice.iam.domain.dto.request.project;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.iam.db.model.TblIamProject;
import com.lnjoying.justice.iam.domain.dto.request.BaseReq;
import com.lnjoying.justice.iam.validation.Enum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;

import static com.lnjoying.justice.iam.common.constant.ConstraintConstants.*;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/9 17:19
 */

@Data
@ApiModel(value = "UpdateProjectReq")
@JsonIgnoreProperties({"id", "bpId", "userId", "createTime", "updateTime"})
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateProjectReq extends BaseReq
{
    @ApiModelProperty(value="")
    private String id;

    /**
     * project name
     */
    @NotBlank(message = "project name must not blank")
    @Pattern(regexp = PATTERN_NAME, message = MESSAGE_NAME)
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
