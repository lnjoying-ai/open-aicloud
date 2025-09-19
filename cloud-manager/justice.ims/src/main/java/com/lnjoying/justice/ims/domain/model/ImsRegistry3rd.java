package com.lnjoying.justice.ims.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.lnjoying.justice.ims.db.model.TblImsRegistry3rd;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

/**
 * third party registry info
 *
 * @author merak
 **/

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties({"accessSecret"})
public class ImsRegistry3rd
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
     * Third-party access key
     */
    @ApiModelProperty(value = "Third-party access key")
    private String accessKey;

    /**
     * Third-party access password
     */
    @ApiModelProperty(value = "Third-party access password")
    private String accessSecret;

    /**
     * Verify remote certificate
     */
    @ApiModelProperty(value = "Verify remote certificate")
    private Boolean insecure;

    /**
     * bp id
     */
    @ApiModelProperty(value = "bp id")
    private String bpId;

    /**
     * user id
     */
    @ApiModelProperty(value = "user id")
    private String userId;


    /**
     * create time
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "create time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * update time
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "update time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
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

    public static ImsRegistry3rd of(TblImsRegistry3rd registry3rd)
    {
        ImsRegistry3rd imsRegistry3rd = new ImsRegistry3rd();
        BeanUtils.copyProperties(registry3rd, imsRegistry3rd);
        return imsRegistry3rd;
    }
}
