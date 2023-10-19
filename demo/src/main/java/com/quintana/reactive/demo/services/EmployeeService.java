package com.quintana.reactive.demo.services;


import com.github.javafaker.Faker;
import com.quintana.reactive.demo.clients.EmployeeDataWebClient;
import com.quintana.reactive.demo.controllers.ControllerExampleReactivo;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeService {

    @Qualifier(value = "employeeWebClient")
    private EmployeeDataWebClient webClient;

    @Autowired
    private EmailService emailService;


    @Autowired
    private Faker faker;
    public Mono<LocalDate> getDoB(int employeeId){
        return Mono.just(LocalDate.now());
    }

    public LocalDate getDoBSimple(int employee){

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return LocalDate.now();
    }

    public Flux<ControllerExampleReactivo.Employee> getEmployees(int quantity) {
        return getEmployees(getEmployeeList(5));
    }
    public Flux<ControllerExampleReactivo.Employee> getEmployees(List<ControllerExampleReactivo.Employee> employeeList){
        return Flux.fromIterable( employeeList )
                .flatMap(employee -> {
                    Mono<Respuesta> localDateMono = webClient.getData();
                    return Mono.zip(Mono.just(employee), localDateMono, EmployeeWithItem::new );
                }).map(employeeWithItem -> ControllerExampleReactivo.Employee.builder()
                        .id(employeeWithItem.getEmployee().getId())
                        .nombre(employeeWithItem.getEmployee().getNombre())
                        .apellido(employeeWithItem.getEmployee().getApellido())
                        .fechaNacimiento(employeeWithItem.getResponseServiceDate().getFecha())
                        .build()
                ).doOnNext(employee -> {
                    EmailService.Email e=
                            EmailService.Email.builder()
                                    .from(employee.getNombre())
                                    .to("chernanq88@gmail.com")
                                    .build();
                    emailService.sendEmail(e);

                })
                .sort(new Comparator<ControllerExampleReactivo.Employee>() {
                        @Override
                        public int compare(ControllerExampleReactivo.Employee o1, ControllerExampleReactivo.Employee o2) {
                            if (o1.getId()>o2.getId())
                                return 1;
                            return -1;
                        }
                    }
                );

    }
    public Flux<Persona> getPersonas(int quantity){
        return  getEmployees(quantity)
                .map(employee -> {
                            var em = employee;
                            return Persona.builder()
                                    .name(em.getNombre())
                                    .lastname(em.getApellido())
                                    .id(em.getId())
                                    .build();
                        }
                )
                .doOnError(throwable -> {System.out.println("Error here");})
                .doOnNext(EmployeeService::maskNames)
                .map(persona -> {
                            persona.setDob(getDoBSimple(persona.getId()));
                            return persona;
                        }
                )
                .doOnComplete(() -> System.out.println("All good"));
    }
    public List<ControllerExampleReactivo.Employee> getEmployeeList(int quantity){
        return IntStream.range(1,quantity).mapToObj(value-> {
                return ControllerExampleReactivo.Employee.builder()
                        .nombre(faker.name().firstName())
                        .apellido(faker.name().lastName())
                        .id(value)
                        .build();
        }).collect(Collectors.toList());
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmployeeWithItem{
        private ControllerExampleReactivo.Employee employee;
        private Respuesta responseServiceDate;

    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Respuesta{
        private LocalDate fecha;
    }
    @Data
    @Builder
    public static class Persona{
        private String name;
        private Integer id;
        private String lastname;
        private LocalDate dob;

    }
    @SneakyThrows
    private static Persona maskNames(Persona p){
        p.setLastname("xxxxxxx");
        System.out.println("Waiting for persona " + p.getName());
        Thread.sleep(1000);
        return p;
    }
}
