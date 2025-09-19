package com.lnjoying.justice.aos.domain.model;

import lombok.Builder;
import lombok.Data;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/4/14 20:13
 */
@Builder
@Data
public class TtyStack
{
    private String userId;

    /**
     * the value is stack id
     */
    private String ttyToken;

    /**
     * tty proxy url
     */
    private String url;

    private String startDate;

    private String lastHeartbeatTime;
}
