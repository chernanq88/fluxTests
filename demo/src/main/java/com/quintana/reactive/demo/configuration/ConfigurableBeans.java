package com.quintana.reactive.demo.configuration;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Locale;

@Configuration
@EnableAsync
public class ConfigurableBeans {
    @Bean
    public FakeValuesService getFakeValuesService(){
        return  new FakeValuesService(
                new Locale("en-GB"), new RandomService());
    }

    @Bean
    public Faker getFaker(){
        return new Faker();
    }

}
