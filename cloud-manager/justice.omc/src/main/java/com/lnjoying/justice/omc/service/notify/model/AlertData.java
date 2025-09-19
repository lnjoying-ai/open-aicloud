package com.lnjoying.justice.omc.service.notify.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/11 19:16
 */

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class AlertData
{
    /**
     * alarm id
     */
    private String id;

    private String title;

    private String content;

    private String level;

}
