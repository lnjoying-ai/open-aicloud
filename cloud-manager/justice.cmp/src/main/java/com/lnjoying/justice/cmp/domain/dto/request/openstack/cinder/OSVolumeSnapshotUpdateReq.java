package com.lnjoying.justice.cmp.domain.dto.request.openstack.cinder;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OSVolumeSnapshotUpdateReq
{
    private static final long serialVersionUID = 1L;
    private OSVolumeSnapshotUpdate snapshot;

    @Data
    public static class OSVolumeSnapshotUpdate
    {
        @JsonProperty("name")
        private String name;
        @JsonProperty("description")
        private String description;
    }
}
