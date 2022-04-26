package com.ead.authuser.configs;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {//Classe de configuração

    static final int TIMEOUT = 5000;
    @LoadBalanced //Define para utilizar balanceamento de carga do microservice course.
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){//Metodo produtor que retorna um RestTemplate.
        return builder
                .setConnectTimeout(Duration.ofMillis(TIMEOUT))//Tempo limite de conexão
                .setReadTimeout(Duration.ofMillis(TIMEOUT))//Tempo limite de leitura
                .build();
    }
}
