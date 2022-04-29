package com.ead.authuser.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope //Atualiza as propriedades que estão sendo geradas pelo config server.
public class RefreshScopeController {//Classe para atualizar propriedades em tempo de execução no config-server
    @Value("${authuser.refreshscope.name}")
    private String name;

    @RequestMapping("/refreshscope")
    public String refreshscope(){
        return this.name;
    }
}
