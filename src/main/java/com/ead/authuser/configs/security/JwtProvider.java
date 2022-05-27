package com.ead.authuser.configs.security;

import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

@Log4j2
@Component
public class JwtProvider { //Classe que gera o token JWT
    @Value("${ead.auth.jwtSecret}")
    private String jwtSecret;

    @Value("${ead.auth.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwt(Authentication authentication){
        UserDetailsImp userPrincipal = (UserDetailsImp) authentication.getPrincipal();

        final String roles = userPrincipal.getAuthorities().stream()//Extraindo roles do userPrincipal
                .map(role -> {
                    return role.getAuthority();
                }).collect(Collectors.joining(","));//Junta a listagem obtida e tranforma em uma string, delimitando cada role com ,

        return Jwts.builder()
                .setSubject((userPrincipal.getUserId().toString()))
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getSubjetJwt(String token){//Recebe o token e extrai o uuid pelo token recebido.
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwt(String authToken){//Metodo que valida se o token Jwt está válido.
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e){
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e){
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e){
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e){
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e){
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }



}
