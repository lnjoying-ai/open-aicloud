package com.lnjoying.justice.omc.domain.dto.response;

import com.lnjoying.justice.omc.domain.dto.model.OperationEventDto;
import lombok.Data;

import java.util.List;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2023年11月02日 14:49
 */
@Data
public class QueryOperationEventRsp
{
    private long                    total_num;
    private List<OperationEventDto> events;
}
