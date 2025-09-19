package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Regulus
 * @Date: 12/9/21 4:31 PM
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagedFieldsEntry
{
    private String     manager;
    private String   operation;
    private String  apiVersion;
    private String        time;
    private String  fieldsType;
    private String    fieldsV1;
    private String subresource;
}
