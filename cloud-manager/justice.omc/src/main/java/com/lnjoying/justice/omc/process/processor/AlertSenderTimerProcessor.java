package com.lnjoying.justice.omc.process.processor;

import com.lnjoying.justice.omc.common.OmcMsgType;
import com.lnjoying.justice.omc.db.repo.AlarmRepository;
import com.lnjoying.justice.omc.domain.model.AlertStatusEnum;
import com.lnjoying.justice.omc.process.service.OmcMsgProcessStrategy;
import com.lnjoying.justice.schema.msg.MessagePack;
import com.micro.core.common.Pair;
import com.micro.core.process.processor.AbstractRunnableProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/10 16:04
 */

@Slf4j
@Component
public class AlertSenderTimerProcessor extends AbstractRunnableProcessor
{
    @Autowired
    private AlarmRepository alarmRepository;

    @Autowired
    private OmcMsgProcessStrategy omcMsgProcessStrategy;

    @Override
    public void start()
    {
        log.info("alert sender process started");
    }

    @Override
    public void stop()
    {
        log.info("alert sender process stopped");
    }

    @Override
    public void run()
    {
        try
        {
            loadFiringAlerts();
            loadResolvedAlerts();
        }
        catch (Exception e)
        {
            log.error("load pending alerts failed:{}", e);
        }
    }

    private void loadFiringAlerts()
    {
        List<String> firingAlertLogIds = alarmRepository.selectIdByAlertStatus(AlertStatusEnum.FIRING.value());
        if (CollectionUtils.isEmpty(firingAlertLogIds))
        {
            return;
        }
        await(futures -> {

            for (String alertLogId : firingAlertLogIds)
            {
                CompletableFuture<Void> promise = new CompletableFuture<>();
                MessagePack messagePack = new MessagePack();
                messagePack.setMsgType(OmcMsgType.SEND_ALERT);
                messagePack.setMessageObj(new Pair(alertLogId, promise));
                omcMsgProcessStrategy.sendMessage(messagePack);
                futures.add(promise);
            }
        });
    }

    private void loadResolvedAlerts()
    {
        List<String> resolvedAlertLogIds = alarmRepository.selectIdByAlertStatus(AlertStatusEnum.RESOLVED.value());
        if (CollectionUtils.isEmpty(resolvedAlertLogIds))
        {
            return;
        }
        await(futures -> {


            for (String alertLogId : resolvedAlertLogIds)
            {
                CompletableFuture<Void> promise = new CompletableFuture<>();
                MessagePack messagePack = new MessagePack();
                messagePack.setMsgType(OmcMsgType.SEND_ALERT);
                messagePack.setMessageObj(new Pair(alertLogId, promise));
                omcMsgProcessStrategy.sendMessage(messagePack);
            }
        });

    }

    public void await(Consumer<List<CompletableFuture<Void>>> consumer)
    {
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        consumer.accept(futures);
        CompletableFuture<Void> future = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        future.whenComplete((r, ex) -> {
            stopWatch.stop();
            if (Objects.nonNull(ex))
            {
                log.error("load firing alerts failed:{}", ex);
            }
            else
            {
                log.info("load  alerts finished  totalTime:{} ms", stopWatch.getTotalTimeMillis());
            }
        });

        future.join();
    }

}
