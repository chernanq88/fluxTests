package com.quintana.reactive.demo.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class LazyLoadingBean {

    public LazyLoadingBean(){
        System.out.println("Lazy loading bean");
    }

}
