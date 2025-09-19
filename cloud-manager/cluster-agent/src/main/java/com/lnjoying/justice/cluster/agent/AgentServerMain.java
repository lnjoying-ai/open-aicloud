package com.lnjoying.justice.cluster.agent;

import com.lnjoying.justice.cluster.agent.common.GlobalArgsInfo;
import com.lnjoying.justice.cluster.agent.common.SpringUtil;
import com.lnjoying.justice.cluster.agent.net.AgentSvrAcceptor;
import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = "com.lnjoying.justice.cluster.agent", exclude = {SecurityAutoConfiguration.class})
public class AgentServerMain
{
    private static Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) throws Exception
    {
        Options options = new Options();
    
        options.addOption(new Option("rk","reg_token", true, "reg token for cluster"));
        options.addOption(new Option("rg","region", true, "region for cluster"));
        options.addOption(new Option("gw","gateway", true, "gateway to login"));
        options.addOption(new Option("h","help", false, "help info for use client"));
        options.addOption(new Option("t","worker_type", true, "worker agent type"));
        
        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, args);
    
        GlobalArgsInfo.cmdArgs.setGateway(commandLine.getOptionValue("gateway"));
        GlobalArgsInfo.cmdArgs.setRegion(commandLine.getOptionValue("region"));
        GlobalArgsInfo.cmdArgs.setToken(commandLine.getOptionValue("reg_token"));
        GlobalArgsInfo.cmdArgs.setWorkerType(commandLine.getOptionValue("worker_type"));
        
        ConfigurableApplicationContext context =SpringApplication.run(AgentServerMain.class, args);
        SpringUtil.set(context);
    
        AgentSvrAcceptor svrAcceptor = SpringUtil.getBean("agentSvrAcceptor");
        svrAcceptor.start();
    }
}
