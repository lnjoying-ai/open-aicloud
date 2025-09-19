package com.lnjoying.justice.cmp.domain.dto.response.openstack.cinder;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class OSVolumeSnapshotMetadata
{
    private Map<String, String> metadata;
}
