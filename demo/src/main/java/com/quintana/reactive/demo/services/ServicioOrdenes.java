package com.quintana.reactive.demo.services;

import com.quintana.reactive.demo.clients.OrderDataWebClient;
import com.quintana.reactive.demo.model.Order;
import com.quintana.reactive.demo.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
public class ServicioOrdenes {

    @Autowired
    private OrderDataWebClient webClient;
    @Autowired
    private ServicioProductos servicioProductos;
    public Mono<Order> collectOrderData(Integer orderId) {

        return webClient.getDataReactive(orderId)
                .doOnNext(respuesta -> System.out.println("Respuesta HTTP de orden " + respuesta.getOrderId()))
                //.map Syncrhonous non blocking transformation input 1 - output 1
                //.flatmap asyncronous  transforms an input item into a Publisher
                .flatMap(order -> {
                    return Mono.just(order).zipWith(getProducts(order.getProductIds()))
                            .map(objects ->
                                Order.builder()
                                    .customer(objects.getT1().getCustomer())
                                    .dob(objects.getT1().getDob())
                                    .orderId(objects.getT1().getOrderId())
                                    .products(objects.getT2())
                                .build());
                }).doOnNext(order -> System.out.println("Orden encontrada y mapeada " + order.getOrderId()));
    }

    private Mono<List<Product>> getProducts(List<Integer> productIds){
        System.out.println(productIds);
        return Flux.fromIterable(productIds)
                .flatMap(productResponse -> servicioProductos.getOrderProducts(productResponse)
                                        .map(responseProduct -> Product.builder()
                                                                    .value(responseProduct.getValue())
                                                                    .name(responseProduct.getName())
                                                                    .quantity(responseProduct.getQuantity())
                                                                .build())).collectList();
    }

    /**
     * Bloquea la primer llamada hacia el servicio de ordenes, con un restTemplate
     * @param orderId
     * @return
     */
    public Mono<Order> collectOrderDataNonBlocking(Integer orderId) {
        Order order = Optional.ofNullable(webClient.getData(orderId)).map(body -> Order.builder()
                .dob(body.getDob())
                .orderId(body.getOrderId())
                .productsIds(body.getProductIds())
                .customer(body.getCustomer())
                .build()
        ).orElseThrow(NoSuchElementException::new);
        Mono<List<Product>> listProduct =
                Flux.fromIterable(order.getProductsIds())
                        .flatMap(response ->  servicioProductos.getOrderProducts(response)
                                .map(responseProduct -> Product.builder()
                                        .value(responseProduct.getValue())
                                        .name(responseProduct.getName())
                                        .quantity(responseProduct.getQuantity())
                                        .build())
                        ).collectList();
        return Mono.just(order).zipWith(listProduct).map(mapper->{
            var orderData = mapper.getT1();
            return Order.builder()
                    .customer(orderData.getCustomer())
                    .dob(orderData.getDob())
                    .orderId(orderData.getOrderId())
                    .products(mapper.getT2())
                    .build();
        });
    }

}
