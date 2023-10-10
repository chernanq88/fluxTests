package com.quintana.reactive.demo.configuration;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;

@Configuration
@Log4j2
public class EmployeeWebClientConfiguration {

    @Bean
    @Lazy
    public WebClient employeeWebClient(){
        log.info("Lazy load bean");
        return WebClient.builder()
                .baseUrl("http://localhost:8081/employee")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8080"))
                .build();
    }
}
