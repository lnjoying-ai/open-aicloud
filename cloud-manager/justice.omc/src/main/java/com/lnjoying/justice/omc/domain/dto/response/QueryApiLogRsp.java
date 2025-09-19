package com.lnjoying.justice.omc.domain.dto.response;

import com.lnjoying.justice.omc.domain.dto.model.ApiLogDto;
import lombok.Data;

import java.util.List;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2023年11月01日 15:21
 */
@Data
public class QueryApiLogRsp
{
    private long            total_num;
    private List<ApiLogDto> logs;
}
