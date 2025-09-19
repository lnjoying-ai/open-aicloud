package com.lnjoying.justice.cluster.manager.config;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/3/18 14:26
 */
@Data
public class ClusterServerRootCA
{
    public transient static final String FILE_PATH = "cluster_server_root_ca.yaml";

    private String svrRootKey;

    private String svrRootCrt;

}
