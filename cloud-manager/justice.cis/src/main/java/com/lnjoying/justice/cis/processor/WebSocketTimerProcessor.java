package com.lnjoying.justice.cis.processor;

import com.lnjoying.justice.cis.webserver.*;
import com.micro.core.process.processor.AbstractRunnableProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

@Component
public class WebSocketTimerProcessor extends AbstractRunnableProcessor
{
    private static Logger LOGGER = LogManager.getLogger();

    private Integer timeout = 1000 * 60 * 5;           //5 minutes

    public WebSocketTimerProcessor() {}

    @Override
    public void start() {
        LOGGER.info("websocket timer processor start");
    }

    @Override
    public void stop() {
        LOGGER.info("websocket timer processor stop");
    }

    @Override
    public void run()
    {
        LOGGER.info(" websocket timer processor run");

        try
        {
            long curTime = System.currentTimeMillis();

            Iterator<Map.Entry<String, WsServerEndPoint>> entries = GlobalWsInfo.WS_SESSIONS.entrySet().iterator();
            while (entries.hasNext())
            {
                Map.Entry<String, WsServerEndPoint> entry = entries.next();
                if (curTime > entry.getValue().getUpdateTime())
                {
                    long diff = curTime - entry.getValue().getUpdateTime();
                    //no msg in 10 minutes
                    if (diff > timeout)
                    {
                        LOGGER.info("release session and stop shell, token: {}", entry.getKey());
                        if(entry.getValue().getAction().equals("exec"))
                        {
                            ContainerShellService.stopShell(entry.getValue().getInstId(), entry.getValue().getToken());
                        }
                        else if (entry.getValue().getAction().equals("logs"))
                        {
                            ContainerLogService.stopLog(entry.getValue().getInstId(), entry.getValue().getToken());
                        }
//                        entry.getValue().stopShell();
                        entry.getValue().release();
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("websocket timer processor error {}", e.getMessage());
        }
    }
}
