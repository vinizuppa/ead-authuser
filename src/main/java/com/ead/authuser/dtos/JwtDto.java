package com.ead.authuser.dtos;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor //Anotação para ser criado um construtor apenas com os campos obrigatórios.
public class JwtDto {
    @NonNull
    private String token;
    private String type = "Bearer";
}
