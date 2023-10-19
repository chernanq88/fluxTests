package com.quintana.reactive.demo.clients;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@Service
@Log4j2
public class OrderDataWebClient implements InitializingBean, DisposableBean {


    @Autowired
    private WebClient orderWebClient;

    @Autowired
    private RestTemplate restTemplate;
    public Mono<Respuesta> getDataReactive(Integer orderId){

        return orderWebClient.get().uri("/order/getOrder/"+ orderId).exchangeToMono(response -> {
            if (response.statusCode().equals(HttpStatus.OK)) {
                return response.bodyToMono(Respuesta.class);
            } else if (response.statusCode().is4xxClientError()) {
                return Mono.just(Respuesta.builder().build());
            } else {
                return response.createException()
                        .flatMap(Mono::error);
            }
        });
    }

    public Respuesta getData(Integer orderId){
        return restTemplate.getForEntity("http://localhost:8081/employee/order/getOrder/" + orderId , Respuesta.class).getBody();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("Initializing EmployeData web client service");
    }

    @Override
    public void destroy() throws Exception {

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Respuesta{
        private String customer;
        private Integer orderId;
        private LocalDate dob;
        private List<Integer> productIds;
    }
}
