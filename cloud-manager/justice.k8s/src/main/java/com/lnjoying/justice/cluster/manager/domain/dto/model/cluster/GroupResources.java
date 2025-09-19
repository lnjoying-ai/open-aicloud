package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Regulus
 * @Date: 12/9/21 4:54 PM
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupResources implements Serializable
{
    private String               group;
    private List<String>     resources;
    private  List<String> resourceNames;
}
