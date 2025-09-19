package com.lnjoying.justice.cluster.manager.config;

import brave.Tracing;
import brave.http.HttpRuleSampler;
import brave.http.HttpSampler;
import brave.http.HttpTracing;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ZipkinConfig
{
    @Bean
    @Primary
    HttpTracing httpTracing(Tracing tracing)
    {
//        HttpSampler clientSampler = HttpRuleSampler.newBuilder().addRule("POST", "/submitWorkerMessage", 0f).build();
        HttpSampler serverSampler = HttpRuleSampler.newBuilder().addRule("GET", "/health", 0f).build();
        return HttpTracing.newBuilder(tracing).serverSampler(serverSampler).build();
    }
}
