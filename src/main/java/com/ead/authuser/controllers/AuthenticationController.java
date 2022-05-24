package com.ead.authuser.controllers;

import com.ead.authuser.configs.security.JwtProvider;
import com.ead.authuser.dtos.JwtDto;
import com.ead.authuser.dtos.LoginDto;
import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.enums.RoleType;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.RoleModel;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.RoleService;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Log4j2 //Anotação do lombok para criar instanciação automatica do log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
public class AuthenticationController {

    //Logger logger = LogManager.getLogger(AuthenticationController.class);//Instanciando a classe de logs, passando como parametro essa classe=AuthenticationController.

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@RequestBody @Validated(UserDto.UserView.RegistrationPost.class)
                                                   @JsonView(UserDto.UserView.RegistrationPost.class) UserDto userDto){
        log.debug("POST registerUser userDto received {} ", userDto.toString());//Definindo para aparecer o log do valor contido em userDto.
        if (userService.existsByUsername(userDto.getUsername())){ //Verifica se o usuário existe no Banco de dados pelo Username.
            log.warn("Username {} is Already Taken! ", userDto.getUsername());//Definindo para aparecer o log de WARN.
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Username is Already Taken!");
        }
        if (userService.existsByEmail(userDto.getEmail())){ //Verifica se o usuário existe no Banco de dados pelo Email.
            log.warn("Email {} is Already Taken! ", userDto.getEmail());//Definindo para aparecer o log de WARN.
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Email is Already Taken!");
        }

        RoleModel roleModel = roleService.findByRoleName(RoleType.ROLE_STUDENT)
                .orElseThrow(() -> new RuntimeException("Error: Role is Not Found."));//Verifica se a Role existe, caso não, lança uma exceção.

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));//Criptografa senha que vem no DTO, antes de salva-la no banco.
        var userModel = new UserModel(); // Instancia um userModel
        BeanUtils.copyProperties(userDto, userModel);
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setUserType(UserType.STUDENT);
        userModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.getRoles().add(roleModel);
        userService.saveUser(userModel);
        log.debug("POST registerUser userId saved {} ", userModel.getUserId());//Definindo para aparecer o log do valor contido em userModel.
        log.info("User saved successfully userId {} ", userModel.getUserId());

        return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDto> authenticateUser(@Valid @RequestBody LoginDto loginDto){//retorna o JWT token
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwt(authentication);
        return ResponseEntity.ok(new JwtDto(jwt));
    }

    @GetMapping("/")
    public String index(){
        log.trace("TRACE");//É utilizado quando queremos vizualizar o log de uma forma mais detalhada.
        log.debug("DEBUG");//Utilizamos principalmente em ambiente de desenvolvimento. Utilizado para por exemplo, vizualizar valores de uma váriavel.
        log.info("INFO ");//Informações uteis de relevantes, mas de operações que ocorreram com sucesso. Por padrão do spring, o log que vem ativo para ser exibido é esse.
        log.warn("WARN");//Não é um log de erro, mas de alerta, como por exemplo: Durante a execução, pode ocorrer perda de dados secundarios, perda de processo que ocorreu mais de uma vez, conflitos, etc, mas não chega a ser considerado erro.
        log.error("ERROR");//Exibe o erro para entender o que está ocorrendo. Geralmente implementamos esse error com Try Catch.
        return "Loggin Spring Boot...";
    }
}
