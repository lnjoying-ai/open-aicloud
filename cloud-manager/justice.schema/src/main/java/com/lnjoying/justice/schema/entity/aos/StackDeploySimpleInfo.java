package com.lnjoying.justice.schema.entity.aos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/12/15 15:31
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class StackDeploySimpleInfo
{
    /**
     * spec name
     */
    private String name;

    /**
     * deployment id
     */
    private String id;

    private int replicaNum;

    private int readyNum;

    private int availableNum;

    private int processingNum;

    private int failedNum;
}
