package com.lnjoying.justice.schema.entity.cluster;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 3/1/22 3:14 PM
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClusterSecretInfo
{
    private String rootPem;
    private String keyPem;
    private String crtPem;
}
