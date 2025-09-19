package com.lnjoying.justice.ims.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/5/27 10:35
 */

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ImageDownloadInfo
{
    @JsonProperty("oper_id")
    private String id;

    /**
     * registry id
     */
    @ApiModelProperty(value = "registry id")
    private String registryId;

    @ApiModelProperty(value = "registry name")
    private String registryName;

    @JsonProperty("image_name")
    private String repos;

    @JsonIgnore
    private Integer status;

    @JsonProperty("status")
    private StatusCode statusCode;

    @JsonIgnore
    private String nodeId;

    private NodeInfo nodeInfo;

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

    /**
     * user id
     */
    @ApiModelProperty(value = "user id")
    private String userId;

    /**
     * bp id
     */
    @ApiModelProperty(value = "bp id")
    private String bpId;


    @Data
    @ApiModel(value = "ImageDownloadInfoStatusCode")
    public static final class StatusCode
    {
        private int code;
        private Map<String, String> desc;
    }

    @Data
    @ApiModel(value = "ImageDownloadInfoNodeInfo")
    public static final class NodeInfo
    {

        private String nodeId;

        private String nodeName;

        private String siteId;

        private String siteName;

        private String regionId;

        private String regionName;
    }
}
