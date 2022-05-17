package com.ead.authuser.configs.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)//Define que essa é a classe de configuração da instancia global de autentication manager
@EnableWebSecurity//Desliga configurações DEFAULT do spring security
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] AUTH_WHITELIST = {//Lista de URIs que poderão ser acessadas sem autenticação
        "/auth/**"
    };


    @Override
    protected void configure(HttpSecurity http) throws Exception{//Esse metodo
        http
                .httpBasic()//define Http Basic
                .and()
                .authorizeRequests()//Qualquer uma das requisições, precisarão estar autorizadas
                .antMatchers(AUTH_WHITELIST).permitAll()//Define que todas URIs presentes dentro da lista passada, terão todas permissões liberadas, fica sem autenticação
                .anyRequest().authenticated()//Qualquer uma das requisições, precisarão estar autenticadas
                .and()
                .csrf().disable()//Desabilita CSRF
                .formLogin();//Para aparecer o formulario no eureka client, é necessário essa linha
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.inMemoryAuthentication()//Define tipo de autenticação.
                .withUser("admin")//Passamos usuário padrão
                .password(passwordEncoder().encode("123456"))//passamos senha padrão
                .roles("ADMIN");
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
