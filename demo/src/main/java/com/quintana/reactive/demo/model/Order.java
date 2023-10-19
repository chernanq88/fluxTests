package com.quintana.reactive.demo.model;

import lombok.Builder;
import lombok.Data;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class Order{
    private Integer orderId;
    private String customer;
    private LocalDate dob;
    private List<Product> products;
    private List<Integer> productsIds;

}
