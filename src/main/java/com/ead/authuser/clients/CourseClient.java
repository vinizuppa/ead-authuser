package com.ead.authuser.clients;

import com.ead.authuser.dtos.CourseDto;
import com.ead.authuser.dtos.ResponsePageDto;
import com.ead.authuser.services.UtilsService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Log4j2
@Component
public class CourseClient {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    UtilsService utilsService;

    @Value("${ead.api.url.course}")//Capta a URL definida no application.yaml
    String REQUEST_URL_COURSE;

    //@Retry(name = "retryInstance")
//    @CircuitBreaker(name = "circuitbreakerInstance", fallbackMethod = "circuitbreakerfallback")//Anotação que utilizar nesse metodo o CircuitBreaker definido no application.yaml  //fallbackMethod define algo padrão a ser executado quando a janela estiver Aberta, como por exemplo uma mensagem padrão.
    @CircuitBreaker(name = "circuitbreakerInstance")
    public Page<CourseDto> getAllCoursesByUser(UUID userId, Pageable pageable){
        List<CourseDto> searchResult = null;
        String url =  REQUEST_URL_COURSE + utilsService.createUrlGetAllCoursesByUser(userId, pageable);//Chama serviço que cria a URL.
        log.debug("Request URL: {} ", url);
        log.info("Request URL: {} ", url);

        try {
            ParameterizedTypeReference<ResponsePageDto<CourseDto>> responseType = new ParameterizedTypeReference<ResponsePageDto<CourseDto>>() {};//Classe abastrata do spring core, para definir parametrização.
            ResponseEntity<ResponsePageDto<CourseDto>> result = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
            searchResult = result.getBody().getContent();//Extraimos o getbody e getContent de result.
            log.debug("Response Number of Elements: {} ", searchResult.size());//Exibe no log o tamanho do retorno da busca
        }catch (HttpStatusCodeException e){
            log.error("Error request /courses {} ", e);
        }
        log.info("Ending request /courses userId {} ", userId);
        return new PageImpl<>(searchResult);
    }

    public Page<CourseDto> circuitbreakerfallback(UUID userId, Pageable pageable, Throwable t){//Metodo que será chamado, quando o circuitBreaker chamar o fallback.
        log.error("Inside circuit breaker fallback, cause - {}", t.toString());
        List<CourseDto> searchResult = new ArrayList<>();//Cria paginação vazia.
        return new PageImpl<>(searchResult);
    }

}
