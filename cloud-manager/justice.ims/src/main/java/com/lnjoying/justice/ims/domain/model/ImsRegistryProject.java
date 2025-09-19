package com.lnjoying.justice.ims.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.lnjoying.justice.ims.db.model.TblImsRegistryProject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * registry project info
 *
 * @author merak
 **/

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ImsRegistryProject
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
     * registry name
     */
    @ApiModelProperty(value = "registry name")
    private String registryName;

    /**
     * scope(0:private;1:bp;2:public)
     */
    @ApiModelProperty(value = "scope(0:private;1:bp;2:public)")
    private Integer scope;

    /**
     * record status(1:normal;-1:deleted)
     */
    @JsonIgnore
    @ApiModelProperty(value = "record status(1:normal;-1:deleted)")
    private Integer status;

    /**
     * user id
     */
    @ApiModelProperty(value = "user id")
    private String userId;

    /**
     * user name
     */
    @ApiModelProperty(value = "user name")
    private String userName;

    /**
     * bp id
     */
    @ApiModelProperty(value = "bp id")
    private String bpId;

    /**
     * bp name
     */
    @ApiModelProperty(value = "bp name")
    private String bpName;

    /**
     * create time
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "create time")
    private LocalDateTime createTime;

    /**
     * update time
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "update time")
    private LocalDateTime updateTime;

    public static ImsRegistryProject of(TblImsRegistryProject project)
    {
        ImsRegistryProject imsRegistryProject = new ImsRegistryProject();
        BeanUtils.copyProperties(project, imsRegistryProject);
        return imsRegistryProject;
    }
}
