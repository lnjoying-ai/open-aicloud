package com.lnjoying.justice.ims.domain.dto.rsp;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

/**
 * harbor intall info
 *
 * @author merak
 **/

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RegistryInstallInfoRsp
{
    public static final String HARBOR_INSTALL_ONLINE = "harbor-install-online.sh";
    public static final String HARBOR_INSTALL_ONLINE_PART = "harbor-install-online-part.sh";
    public static final String HARBOR_INSTALL_OFFLINE = "harbor-install-offline.sh";
    public static final String HARBOR_STOP = "harbor-stop.sh";
    public static final String HARBOR_REMOVE = "harbor-remove.sh";

    private String registryId;

    private String registryName;

    private String harborInstallOnline;

    private String harborInstallOnlinePart;

    private String harborInstallOffline;

    private String harborStop;

    private String harborRemove;
}
