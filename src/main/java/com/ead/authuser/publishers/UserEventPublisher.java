package com.ead.authuser.publishers;

import com.ead.authuser.dtos.UserEventDto;
import com.ead.authuser.enums.ActionType;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserEventPublisher {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Value(value = "${ead.broker.exchange.userEvent}")//Pega o valor definido no application-dev.yaml
    private String exchangeUserEvent;

    public void publishUserEvent(UserEventDto userEventDto, ActionType actionType){//Publica eventos no RabbitMq.
        userEventDto.setActionType(actionType.toString());//Define qual o tipo do evento: criação, atualização ou deleção do usuário.
        rabbitTemplate.convertAndSend(exchangeUserEvent, "", userEventDto);
    }
}
