package com.lnjoying.justice.cmp.domain.dto.response.openstack.glance;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.lnjoying.justice.cmp.db.model.TblCmpOsImages;
import com.lnjoying.justice.cmp.utils.BoolUtil;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OSImageInfo
{

    private String checksum;
    @JsonProperty("container_format")
    @SerializedName("container_format")
    private String containerFormat;
    @JsonProperty("created_at")
    @SerializedName("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date createdAt;
    @JsonProperty("disk_format")
    @SerializedName("disk_format")
    private String diskFormat;
    private String file;
    private String id;
    @JsonProperty("min_disk")
    @SerializedName("min_disk")
    private Integer minDisk;
    @JsonProperty("min_ram")
    @SerializedName("min_ram")
    private Integer minRam;
    private String name;
    @JsonProperty("os_hash_algo")
    @SerializedName("os_hash_algo")
    private String osHashAlgo;
    @JsonProperty("os_hash_value")
    @SerializedName("os_hash_value")
    private String osHashValue;
    @JsonProperty("os_hidden")
    @SerializedName("os_hidden")
    private Boolean osHidden;
    private String owner;
    @JsonProperty("protected")
    @SerializedName("protected")
    private Boolean protect;
    private String schema;
    private String self;
    private Long size;
    private String status;
    private List<String> tags;
    @JsonProperty("updated_at")
    @SerializedName("updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date updatedAt;
    @JsonProperty("virtual_size")
    @SerializedName("virtual_size")
    private Integer virtualSize;
    private String visibility;
    @JsonProperty("direct_url")
    @SerializedName("direct_url")
    private String directUrl;
    private List<Object> locations;

    @JsonProperty("instance_id")
    @SerializedName("instance_id")
    private String instanceId;
    @JsonProperty("boot_roles")
    @SerializedName("boot_roles")
    private String bootRoles;
    @JsonProperty("owner_user_name")
    @SerializedName("owner_user_name")
    private String ownerUserName;
    @JsonProperty("user_id")
    @SerializedName("user_id")
    private String userId;
    @JsonProperty("owner_project_name")
    @SerializedName("owner_project_name")
    private String ownerProjectName;
    @JsonProperty("image_type")
    @SerializedName("image_type")
    private String imageType;
    @JsonProperty("base_image_ref")
    @SerializedName("base_image_ref")
    private String baseImageRef;
    @JsonProperty("image_location")
    @SerializedName("image_location")
    private String imageLocation;
    @JsonProperty("image_state")
    @SerializedName("image_state")
    private String imageState;
    @JsonProperty("owner_id")
    @SerializedName("owner_id")
    private String ownerId;
    @JsonProperty("block_device_mapping")
    @SerializedName("block_device_mapping")
    private String blockDeviceMapping;
    @JsonProperty("bdm_v2")
    @SerializedName("bdm_v2")
    private String bdmV2;
    @JsonProperty("hw_cdrom_bus")
    @SerializedName("hw_cdrom_bus")
    private String hwCdromBus;
    @JsonProperty("hw_disk_bus")
    @SerializedName("hw_disk_bus")
    private String hwDiskBus;
    @JsonProperty("hw_machine_type")
    @SerializedName("hw_machine_type")
    private String hwMachineType;
    @JsonProperty("hw_video_model")
    @SerializedName("hw_video_model")
    private String hwVideoModel;
    @JsonProperty("hw_vif_model")
    @SerializedName("hw_vif_model")
    private String hwVifModel;
    @JsonProperty("root_device_name")
    @SerializedName("root_device_name")
    private String rootDeviceName;
    @JsonProperty("usage_type")
    @SerializedName("usage_type")
    private String usageType;

    public void setImageInfo(TblCmpOsImages tblCmpOsImage)
    {
        this.checksum = tblCmpOsImage.getChecksum();
        this.containerFormat = tblCmpOsImage.getContainerFormat();
        this.createdAt = tblCmpOsImage.getCreatedAt();
        this.diskFormat = tblCmpOsImage.getDiskFormat();
        this.id = tblCmpOsImage.getId();
        this.minDisk = tblCmpOsImage.getMinDisk();
        this.minRam = tblCmpOsImage.getMinRam();
        this.name = tblCmpOsImage.getName();
        this.osHashAlgo = tblCmpOsImage.getOsHashAlgo();
        this.osHashValue = tblCmpOsImage.getOsHashValue();
        this.osHidden = BoolUtil.short2Bool(tblCmpOsImage.getOsHidden());
        this.owner = tblCmpOsImage.getOwner();
        this.protect = BoolUtil.short2Bool(tblCmpOsImage.getProtected());
        this.size = tblCmpOsImage.getSize();
        this.status = tblCmpOsImage.getStatus();
        this.updatedAt = tblCmpOsImage.getUpdatedAt();
        this.virtualSize = tblCmpOsImage.getVirualSize() == null ? null : tblCmpOsImage.getVirualSize().intValue();
        this.visibility = tblCmpOsImage.getVisibility();

        if (StringUtils.isNotEmpty(tblCmpOsImage.getProperties()))
        {
            Map<String, String> properties = JsonUtils.fromJson(tblCmpOsImage.getProperties(), new TypeToken<Map<String, String>>(){}.getType());
            if (properties.containsKey("instance_id")) this.instanceId = properties.get("instance_id");
            if (properties.containsKey("boot_roles")) this.bootRoles = properties.get("boot_roles");
            if (properties.containsKey("owner_user_name")) this.ownerUserName = properties.get("owner_user_name");
            if (properties.containsKey("user_id")) this.userId = properties.get("user_id");
            if (properties.containsKey("owner_project_name")) this.ownerProjectName = properties.get("owner_project_name");
            if (properties.containsKey("image_type")) this.imageType = properties.get("image_type");
            if (properties.containsKey("base_image_ref")) this.baseImageRef = properties.get("base_image_ref");
            if (properties.containsKey("image_location")) this.imageLocation = properties.get("image_location");
            if (properties.containsKey("owner_id")) this.ownerId = properties.get("owner_id");
            if (properties.containsKey("block_device_mapping")) this.blockDeviceMapping = properties.get("block_device_mapping");
            if (properties.containsKey("bdm_v2")) this.bdmV2 = properties.get("bdm_v2");
            if (properties.containsKey("hw_cdrom_bus")) this.hwCdromBus = properties.get("hw_cdrom_bus");
            if (properties.containsKey("hw_disk_bus")) this.hwDiskBus = properties.get("hw_disk_bus");
            if (properties.containsKey("hw_machine_type")) this.hwMachineType = properties.get("hw_machine_type");
            if (properties.containsKey("hw_video_model")) this.hwVideoModel = properties.get("hw_video_model");
            if (properties.containsKey("hw_vif_model")) this.hwVifModel = properties.get("hw_vif_model");
            if (properties.containsKey("root_device_name")) this.rootDeviceName = properties.get("root_device_name");
            if (properties.containsKey("usage_type")) this.usageType = properties.get("usage_type");
            if (properties.containsKey("self")) this.self = properties.get("self");
            if (properties.containsKey("file")) this.file = properties.get("file");
            if (properties.containsKey("schema")) this.schema = properties.get("schema");
        }
    }
}
