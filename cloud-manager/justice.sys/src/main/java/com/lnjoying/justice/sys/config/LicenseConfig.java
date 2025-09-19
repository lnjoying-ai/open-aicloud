package com.lnjoying.justice.sys.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 3/4/23 5:10 PM
 */
@Data
@ApiModel(value = "license config")
public class LicenseConfig
{
    @JsonProperty(value = "node_num")
    private String nodeNum;

    @JsonProperty(value = "core_num")
    private String coreNum;
    
    @JsonProperty(value = "start_time")
    @NotBlank(message = "content can not be empty")
    @Pattern(regexp = ServiceConfig.TIME_PATTERN, message = "time pattern yyyy-MM-dd HH:mm:ss")
    private String StartTime;
    
    @JsonProperty(value = "end_time")
    @NotBlank(message = "content can not be empty")
    @Pattern(regexp = ServiceConfig.TIME_PATTERN, message = "time pattern yyyy-MM-dd HH:mm:ss")
    private String endTime;
    
    @JsonProperty(value = "sign")
    @NotBlank(message = "content can not be empty")
    private String sign;
    
    @JsonProperty(value = "verification_code")
    @NotBlank(message = "content can not be empty")
    private String verificationCode;
    
    @JsonProperty(value = "reserve")
    private String reserve;
}