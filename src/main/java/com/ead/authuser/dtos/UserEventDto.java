package com.ead.authuser.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class UserEventDto {//Dto dos atributos que serão enviados na mensagem para o RabbitMq.
    private UUID userId;
    private String username;
    private String email;
    private String fullname;
    private String userStatus;
    private String userType;
    private String phoneNumber;
    private String cpf;
    private String imageUrl;
    private String actionType;//tipo de ação: criação, atualização, deleção.
}
