package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import com.micro.core.common.Utils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: Regulus
 * @Date: 12/9/21 4:59 PM
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupVersion
{
    private String   group;
    private  String version;
    public String toString()
    {
        if (StringUtils.isNotBlank(group))
        {
            Utils.buildStr(group, "/", version);
        }
        
        return version;
    }
}
