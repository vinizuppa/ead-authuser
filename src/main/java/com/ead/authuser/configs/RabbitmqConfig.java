package com.ead.authuser.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {//Classe de configuração do rabbitMq
    @Autowired
    CachingConnectionFactory cachingConnectionFactory;//Responsável por conectar microservice com o RabbitMq. Ele leva em consideração o endereço que inserimos no application-dev.yaml

    @Value(value = "${ead.broker.exchange.userEvent}")//Pega o valor definido no application-dev.yaml
    private String exchangeUserEvent;

    @Bean
    public RabbitTemplate rabbitTemplate(){//Envia mensagens(eventos)
        RabbitTemplate template = new RabbitTemplate(cachingConnectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter(){//Converte as mensagens para serializar e desserializar mensagens para enviar e receber do broker.
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public FanoutExchange fanoutUserEvent(){
        return new FanoutExchange(exchangeUserEvent);//Define a exchange como do tipo fanout, para enviar mensagens para diversas filas.
    }
}
