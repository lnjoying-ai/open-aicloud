package com.lnjoying.justice.api;

import com.lnjoying.justice.api.config.SecurityModeConfig;
import com.lnjoying.justice.api.handler.BusinessHandler;
import com.lnjoying.justice.api.handler.SessionManageHandler;
import com.lnjoying.justice.schema.msg.EeNetMessageApi;
import com.micro.core.nework.entity.NetEntity;
import com.micro.core.nework.tcp.TcpAcceptor;
import com.micro.core.nework.tcp.method.ITcpAddPipeHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.foundation.common.utils.BeanUtils;
import java.util.concurrent.TimeUnit;

import static com.lnjoying.justice.commonweb.util.NacosConfigLoaderUtils.loadAndSyncNacosConfigToSpring;

public class ApiServerMain
{
    private static Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) throws Exception
    {
        LOGGER.info("start server");
        SecurityModeConfig.loadSecurityModeConfig();
        loadAndSyncNacosConfigToSpring();
        BeanUtils.init();

        int serverPort = Integer.parseInt(args.length > 0 && args[0] != null ? args[0] : "9188");
        TcpAcceptor acceptor = new TcpAcceptor(socketChannel ->
        {
            ChannelPipeline pipeline = socketChannel.pipeline();
            pipeline.addLast(new ReadTimeoutHandler(600, TimeUnit.SECONDS));
            pipeline.addLast(new WriteTimeoutHandler(600, TimeUnit.SECONDS));
            pipeline.addLast("serverLog",    new LoggingHandler(LogLevel.DEBUG));
            pipeline.addLast("FrameDecoder", new LengthFieldBasedFrameDecoder(10 * 10240000 , 0, 4, 0, 4, true));
            pipeline.addLast("FrameEncoder", new LengthFieldPrepender(4));
            pipeline.addLast("decoder",      new ProtobufDecoder(EeNetMessageApi.ee_net_message.getDefaultInstance()));
            pipeline.addLast("encoder",      new ProtobufEncoder());
            pipeline.addLast("sessionLogin", new SessionManageHandler());
            pipeline.addLast("business",     new BusinessHandler());
        });

        acceptor.bind(new NetEntity("0.0.0.0", serverPort, NetEntity.ChannelType.SERVER));
        LOGGER.info("start api server success.");
        acceptor.loop();
    }
}
