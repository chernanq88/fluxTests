package com.quintana.reactive.demo.clients;

import com.quintana.reactive.demo.services.EmployeeService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Service
public class EmployeeDataWebClient {
    public Mono<EmployeeService.Respuesta> getData(){
        WebClient client = WebClient.builder()
                .baseUrl("http://localhost:8081")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8080"))
                .build();
        return client.get().uri("/employee").exchangeToMono(response -> {
            if (response.statusCode().equals(HttpStatus.OK)) {
                return response.bodyToMono(EmployeeService.Respuesta.class);
            } else if (response.statusCode().is4xxClientError()) {
                return Mono.just(EmployeeService.Respuesta.builder().build());
            } else {
                return response.createException()
                        .flatMap(Mono::error);
            }
        });
    }
}
