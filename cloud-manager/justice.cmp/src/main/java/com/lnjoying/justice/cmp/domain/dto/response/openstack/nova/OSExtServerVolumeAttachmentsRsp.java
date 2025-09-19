package com.lnjoying.justice.cmp.domain.dto.response.openstack.nova;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cmp.db.model.TblCmpOsVolumeAttachment;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.cinder.OSExtVolumeInfo;
import lombok.Data;

import java.util.List;

@Data
public class OSExtServerVolumeAttachmentsRsp
{
    private List<OSServerVolumeAttachment> volumeAttachments;

    @SerializedName("total_num")
    @JsonProperty("total_num")
    private long totalNum;

    @Data
    public static class OSServerVolumeAttachment
    {
        private String id;
        private String serverId;
        private String volumeId;
        private String device;
        private String tag;
        @JsonProperty("delete_on_termination")
        @SerializedName("delete_on_termination")
        private Boolean deleteOnTermination;
        @JsonProperty("attachment_id")
        @SerializedName("attachment_id")
        private String attachmentId;
        @JsonProperty("bdm_uuid")
        @SerializedName("bdm_uuid")
        private String bdmUuid;

        @JsonProperty("volume")
        @SerializedName("volume")
        private OSExtVolumeInfo volume;

        public void setVolumeAttachment(TblCmpOsVolumeAttachment tblCmpOsVolumeAttachment)
        {
            this.id = tblCmpOsVolumeAttachment.getId();
            this.serverId = tblCmpOsVolumeAttachment.getInstanceUuid();
            this.volumeId = tblCmpOsVolumeAttachment.getVolumeId();
            this.device = tblCmpOsVolumeAttachment.getMountpoint();
            this.attachmentId = tblCmpOsVolumeAttachment.getId();
        }
    }
}
