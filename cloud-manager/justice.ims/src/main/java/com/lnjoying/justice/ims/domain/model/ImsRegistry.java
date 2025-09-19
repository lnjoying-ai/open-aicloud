package com.lnjoying.justice.ims.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.lnjoying.justice.ims.db.model.TblImsRegistry;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * registry info
 *
 * @author merak
 **/

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ImsRegistry
{
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
     * registry desc
     */
    @ApiModelProperty(value = "registry desc")
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
    @JsonIgnore
    @ApiModelProperty(value = "administrator password")
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

    @ApiModelProperty(value = "regions")
    private List<Region> regions;
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


    public static ImsRegistry of(TblImsRegistry registry)
    {
        ImsRegistry imsRegistry = new ImsRegistry();
        BeanUtils.copyProperties(registry, imsRegistry);
        return imsRegistry;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static final class Region
    {

        private final String regionId;

        private final String regionName;
    }
}
