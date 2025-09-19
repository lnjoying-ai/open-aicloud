package com.lnjoying.justice.omc.service;

import com.micro.core.common.Pair;

import java.util.concurrent.CompletableFuture;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/10 16:18
 */

public interface AlertSenderService
{
    void processAlertSend(Pair<String, CompletableFuture<Void>> alertLogPair);
}
