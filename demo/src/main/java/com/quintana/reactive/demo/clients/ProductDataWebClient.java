package com.quintana.reactive.demo.clients;

import com.quintana.reactive.demo.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@Service
@Log4j2
public class ProductDataWebClient implements InitializingBean, DisposableBean {

    @Autowired
    private WebClient productWebClient;
    public Mono<Product> getData(Integer productId){

        return productWebClient.get().uri("/products/getProductsById/" + productId ).exchangeToMono(response -> {
            if (response.statusCode().equals(HttpStatus.OK)) {
                return response.bodyToMono(Product.class);
            } else if (response.statusCode().is4xxClientError()) {
                return Mono.empty();
            }
            return Mono.empty();
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
