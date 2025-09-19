package com.lnjoying.justice.cmp.domain.dto.response.openstack.nova;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.OSLink;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class OSKeyPairsRsp
{
    private List<OSKeyPairInfo> keypairs;

    @JsonProperty("keypairs_links")
    @SerializedName("keypairs_links")
    private List<OSLink> keypairsLinks;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class OSKeyPairInfo
    {
        private OSKeyPairBasicInfo keypair;
    }

    public void setKeyPairBasicInfo(List<OSKeyPairBasicInfo> keyPairBasicInfos)
    {
        if (! CollectionUtils.isEmpty(keyPairBasicInfos))
        {
            this.keypairs = new ArrayList<>();
            keyPairBasicInfos.forEach(osKeyPairBasicInfo -> this.keypairs.add(new OSKeyPairInfo(osKeyPairBasicInfo)));
        }
    }
}
