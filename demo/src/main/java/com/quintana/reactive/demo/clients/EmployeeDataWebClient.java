package com.quintana.reactive.demo.clients;

import com.quintana.reactive.demo.services.EmployeeService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Log4j2
public class EmployeeDataWebClient implements InitializingBean, DisposableBean {

    @Lazy
    @Autowired
    private WebClient webClientEmployee;
    public Mono<EmployeeService.Respuesta> getData(){

        return webClientEmployee.get().uri("/employee/extraData").exchangeToMono(response -> {
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

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("Initializing EmployeData web client service");
    }

    @Override
    public void destroy() throws Exception {

    }
}
