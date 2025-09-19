package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Regulus
 * @Date: 12/9/21 4:30 PM
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OwnerReference
{
    private String          apiVersion;
    private String                kind;
    private String                name;
    private String                 uid;
    private Boolean         controller;
    private Boolean blockOwnerDeletion;
}
