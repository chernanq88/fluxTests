package com.quintana.reactive.demo.controllers;


import com.quintana.reactive.demo.model.Order;
import com.quintana.reactive.demo.model.Product;
import com.quintana.reactive.demo.services.EmployeeService;
import com.quintana.reactive.demo.services.ServicioOrdenes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reactive")
public class ControllerOrderReaactive {

    @Autowired
    private ServicioOrdenes servicioOrdenes;

    @GetMapping(value = "/asyncronousNonBlockingOrder/{orderId}")
    public Mono<Order> getOrder(@PathVariable(name = "orderId") Integer orderId){
        return servicioOrdenes.collectOrderData(orderId);
    }


}
