package com.lnjoying.justice.aos.helm.model;

import java.util.Arrays;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/8/25 17:19
 */

public enum ReleaseStatus
{
    /**
     * StatusUnknown indicates that a release is in an uncertain state.
     */
    StatusUnknown("unknown"),

    /**
     * StatusDeployed indicates that the release has been pushed to Kubernetes.
     */
    StatusDeployed("deployed"),

    /**
     * StatusUninstalled indicates that a release has been uninstalled from Kubernetes.
     */
    StatusUninstalled("uninstalled"),

    /**
     * StatusSuperseded indicates that this release object is outdated and a newer one exists.
     */
    StatusSuperseded("superseded"),

    /**
     * StatusFailed indicates that the release was not successfully deployed.
     */
    StatusFailed("failed"),

    /**
     *  StatusUninstalling indicates that a uninstall operation is underway.
     */
    StatusUninstalling("uninstalling"),

    /**
     * StatusPendingInstall indicates that an install operation is underway.
     */
    StatusPendingInstall("pending-install"),

    /**
     * StatusPendingUpgrade indicates that an upgrade operation is underway.
     */
    StatusPendingUpgrade("pending-upgrade"),

    /**
     * StatusPendingRollback indicates that an rollback operation is underway.
     */
    StatusPendingRollback("pending-rollback");

    
    private final String name;

    ReleaseStatus(String name)
    {
        this.name = name;
    }

    public static ReleaseStatus fromName(String name)
    {
        return  Arrays.stream(ReleaseStatus.values()).filter(releaseStatus -> releaseStatus.getName().equalsIgnoreCase(name))
                .findFirst().orElseThrow(() -> new RuntimeException());
    }

    public String getName()
    {
        return name;
    }
}
