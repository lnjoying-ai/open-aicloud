package com.lnjoying.justice.omc.service.notify.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/11 19:27
 */

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class AlertInfo
{

    private AlertData alertData;

    private Map<String, String> alertParams;
}
