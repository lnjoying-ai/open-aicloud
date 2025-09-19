package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author: Regulus
 * @Date: 12/9/21 4:27 PM
 * @Description:
 */
@Data
public class ObjectMeta implements Serializable
{
    private String                            name;
    private String                    generateName;
    private String                       namespace;
    private String                        selfLink;
    private String                             uid;
    private String                 resourceVersion;
    private String                      generation;
    private String               creationTimestamp;
    private String               deletionTimestamp;
    private String      deletionGracePeriodSeconds;
    private Map<String, String>             labels;
    private Map<String, String>        annotations;
    private List<OwnerReference>   ownerReferences;
    private List<String>                finalizers;
    private String                     clusterName;
    private List<ManagedFieldsEntry> managedFields;
}
