package com.lnjoying.justice.cmp.domain.dto.response.openstack.cinder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OSVolumeAttachmentInfo
{
    @JsonProperty("server_id")
    @SerializedName("server_id")
    private String serverId;
    @JsonProperty("attachment_id")
    @SerializedName("attachment_id")
    private String attachmentId;
    @JsonProperty("attached_at")
    @SerializedName("attached_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date attachedAt;
    @JsonProperty("host_name")
    @SerializedName("host_name")
    private String hostName;
    @JsonProperty("volume_id")
    @SerializedName("volume_id")
    private String volumeId;
    private String device;
    private String id;
}
