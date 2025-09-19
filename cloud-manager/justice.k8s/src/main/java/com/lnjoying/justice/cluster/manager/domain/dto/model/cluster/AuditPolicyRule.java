package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Regulus
 * @Date: 12/9/21 4:25 PM
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditPolicyRule implements Serializable
{
    private String                   level;
    private  List<String>             users;
    private  List<String>        userGroups;
    private List<String>             verbs;
    private List<GroupResources> resources;
    private  List<String>        namespaces;
    private List<String>   nonResourceURLs;
    private List<String>        omitStages;
}
