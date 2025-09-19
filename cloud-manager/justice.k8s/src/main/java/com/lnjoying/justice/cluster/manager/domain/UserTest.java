package com.lnjoying.justice.cluster.manager.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;

import javax.validation.constraints.Pattern;

@ToString
@ApiModel(value = "UserTest")
public class UserTest
{
    @JsonProperty("name_info")
    @SerializedName("name_info")
    @ApiModelProperty(example = "123456")
    @Expose
    private String nameInfo;
    
//    @JsonProperty("age_info")
    private transient int ageInfo;
    
    public String getNameInfo() {
        return nameInfo;
        
    }
    
    public void setNameInfo(String nameInfo) {
        this.nameInfo = nameInfo;
        
    }
    
    public int getAgeInfo() {
        return ageInfo;
        
    }
    
    public void setAgeInfo(int ageInfo) {
        this.ageInfo = ageInfo;
        
    }
}
