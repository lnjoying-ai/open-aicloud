package com.lnjoying.justice.cmp.domain.dto.response.nextstack.network;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class SecurityGroupRspVo
{
    private String sgId;

    private String name;

    private int ruleCount;

    private int computeInstanceCount;

    private int phaseStatus;

    private String description;

    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date createTime;

    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date updateTime;

    private String eeBp;
    private String eeUser;
    private String eeBpName;
    private String eeUserName;
    private Integer eeStatus;
    private Boolean isDefault;
}
