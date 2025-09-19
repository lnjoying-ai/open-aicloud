package com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo;

import com.lnjoying.justice.cmp.common.constant.ImageType;
import com.lnjoying.justice.cmp.db.model.TblCmpImage;
import com.micro.core.common.Utils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class ImageDetailInfoRsp
{
    private String imageId;
    private String imageName;
    private Integer imageOsType;
    private Integer imageOsVendor;
    private String imageOsVersion;
    private Integer imageFormat;
    private Boolean isPublic;
    private String createTime;
    private String updateTime;
    private Integer phaseStatus;
    private String gpuDriverVersion;

    private String eeBp;
    private String eeUser;
    private String eeBpName;
    private String eeUserName;
    private Boolean eeVisibility;

    public void setImageDetailInfoRsp(TblCmpImage tblImage){
        this.imageId = tblImage.getImageId();
        this.imageName = tblImage.getImageName();
        this.imageOsType = tblImage.getImageOsType() == null ? null : tblImage.getImageOsType().intValue();
        this.imageOsVendor = tblImage.getImageOsVendor() == null ? null : tblImage.getImageOsVendor().intValue();
        this.imageOsVersion = tblImage.getImageOsVersion();
        this.isPublic = tblImage.getIsPublic();
        this.phaseStatus = tblImage.getPhaseStatus() == null ? null : tblImage.getPhaseStatus().intValue();
        if (StringUtils.isBlank(tblImage.getFileIdFromAgent()))
        {
            this.imageFormat = ImageType.VM_IMAGE_TYPE;
        }
        else
        {
            this.imageFormat = ImageType.BAREMETAL_IMAGE_TYPE;
        }
        this.createTime = Utils.formatDate(tblImage.getCreateTime());
        this.updateTime = Utils.formatDate(tblImage.getUpdateTime());

        if (this.imageName.contains("GPU"))
        {
            String[] imageSplit = this.imageName.split("-GPU-");
            this.gpuDriverVersion = imageSplit[1];
        }

        this.eeBp = tblImage.getEeBp();
        this.eeUser = tblImage.getEeUser();
        this.eeVisibility = tblImage.getEeVisibility() == null || tblImage.getEeVisibility();
    }
}
