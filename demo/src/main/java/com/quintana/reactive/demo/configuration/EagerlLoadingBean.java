package com.quintana.reactive.demo.configuration;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class EagerlLoadingBean {

    public EagerlLoadingBean(){
        System.out.println("Eagerly loading bean");

    }

}
