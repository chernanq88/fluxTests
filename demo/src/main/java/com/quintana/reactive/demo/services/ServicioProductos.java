package com.quintana.reactive.demo.services;

import com.quintana.reactive.demo.clients.ProductDataWebClient;
import com.quintana.reactive.demo.model.Product;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;


@Service
public class ServicioProductos {

    @Autowired
    private ProductDataWebClient webClient;
    public Mono<Product> getOrderProducts(int productId){
        return webClient.getData(productId);
    }

}
