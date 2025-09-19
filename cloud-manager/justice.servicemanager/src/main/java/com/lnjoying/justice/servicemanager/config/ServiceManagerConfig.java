package com.lnjoying.justice.servicemanager.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@PropertySource(value = "classpath:microservice.yaml", encoding="utf-8")
public class ServiceManagerConfig
{
    @Value(" ${servicecomb.service.registry.address}")
    private String servicecombUrl;
}
