package com.lnjoying.justice.cis.domain.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/11/2 20:42
 */

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ContainerConfig
{
    private String userId;

    private String group;

    private String dataId;

    private String containerDest;

    private boolean readOnly;
}
