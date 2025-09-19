package com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lnjoying.justice.cmp.db.model.TblCmpVolumeSnap;
import lombok.Data;

import java.util.Date;

@Data
public class VolumeSnapDetailInfoRsp
{
    private String volumeSnapId;

    private String volumeId;

    private String volumeName;

    private String name;

    private String description;

    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date createTime;

    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date updateTime;

    private Integer phaseStatus;

    private String storagePoolId;

    private Boolean isCurrent;

    private String userId;

    private String parentId;

    private String eeBp;
    private String eeUser;
    private String eeBpName;
    private String eeUserName;

    public void setVolumeSnapDetailInfoRsp(TblCmpVolumeSnap tblVolumeSnap)
    {
        this.volumeId = tblVolumeSnap.getVolumeId();

        this.volumeSnapId = tblVolumeSnap.getVolumeSnapId();

        this.name = tblVolumeSnap.getName();

        this.description = tblVolumeSnap.getDescription();

        this.isCurrent = tblVolumeSnap.getIsCurrent();

        this.phaseStatus = tblVolumeSnap.getPhaseStatus();

        this.createTime = tblVolumeSnap.getCreateTime();

        this.updateTime = tblVolumeSnap.getUpdateTime();

        this.parentId = tblVolumeSnap.getParentId();

        this.eeBp = tblVolumeSnap.getEeBp();
        this.eeUser = tblVolumeSnap.getEeUser();
    }
}
