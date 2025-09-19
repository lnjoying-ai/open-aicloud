package com.lnjoying.justice.ecrm.db.model;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;

@ApiModel(value = "TblLabelOptionInfo")
public class TblLabelOptionInfo implements Serializable {
    private String key;

    private String labelType;

    private String type;

    private String labelOption;

    private Integer orderNum;

    private static final long serialVersionUID = 1L;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key == null ? null : key.trim();
    }

    public String getLabelType() {
        return labelType;
    }

    public void setLabelType(String labelType) {
        this.labelType = labelType == null ? null : labelType.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getLabelOption() {
        return labelOption;
    }

    public void setLabelOption(String labelOption) {
        this.labelOption = labelOption == null ? null : labelOption.trim();
    }

    public Integer getOrderNum()
    {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum)
    {
        this.orderNum = orderNum;
    }
}