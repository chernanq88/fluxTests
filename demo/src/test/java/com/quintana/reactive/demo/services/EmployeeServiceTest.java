package com.quintana.reactive.demo.services;

import com.github.javafaker.Faker;
import com.quintana.reactive.demo.clients.EmployeeDataWebClient;
import com.quintana.reactive.demo.controllers.ControllerExampleReactivo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Source
 * https://github.com/lokeshgupta1981/Spring-Boot-Examples/blob/master/Testing-Libraries/src/test/java/com/howtodoinjava/demo/mockito/MockitoHelloTest.java
 */
@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeDataWebClient employeeDataWebClient;

    @Spy
    private Faker faker;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    @DisplayName("Flux test example")
    public void testEmployeeFlux(){

        Flux<String> source=
                Flux.fromIterable(List.of("JOHN","MARTIN","CLOE", "CATE"))
                                .doOnNext(name->System.out.println(name));
        source.subscribe();
        StepVerifier
                .create(source)
                .expectNext("JOHN")
                .expectNextMatches(name -> name.startsWith("MA"))
                .expectNext("CLOE", "CATE")
                .expectComplete()
                .verify();

    }

    @Test
    @DisplayName("Flux asyncronous non blocking")
    public void test2(){

        Mockito.when(employeeDataWebClient.getData()).thenReturn(
                Mono.just(EmployeeService.Respuesta.builder().fecha(LocalDate.now()).build()));

        List<ControllerExampleReactivo.Employee> lista=
                new ArrayList<ControllerExampleReactivo.Employee>();

        ControllerExampleReactivo.Employee e1=
                ControllerExampleReactivo.Employee.builder()
                        .id(1)
                        .nombre("Carlos")
                        .apellido("Quintana")
                        .build();
        ControllerExampleReactivo.Employee e2=
                ControllerExampleReactivo.Employee.builder().id(2).build();

        lista.addAll(List.of(e1,e2));

        employeeService.getEmployees(lista).subscribe();

        StepVerifier.create(employeeService.getEmployees(lista))
                .expectNextMatches(employee -> employee.getId()==1 && employee.getFechaNacimiento()!=null)
                .expectNextMatches(employee -> employee.getId()==2 && employee.getFechaNacimiento()!=null)
                .expectComplete()
                .verify();

    }
}
