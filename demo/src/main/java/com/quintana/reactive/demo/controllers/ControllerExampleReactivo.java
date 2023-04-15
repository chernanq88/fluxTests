package com.quintana.reactive.demo.controllers;

import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

import java.time.LocalDate;
import java.util.stream.Stream;

@RestController
@RequestMapping("/example")
public class ControllerExampleReactivo {

    @Data
    @Builder
    static class Persona{
        private String name;
        private String lastname;
        private LocalDate dob;

    }

    @Data
    static class Employee{

        private String nombre;
        private String apellido;
        private LocalDate fechaNacimiento;

        public Employee(String nombre, String apellido) {
            this.nombre = nombre;
            this.apellido = apellido;
            fechaNacimiento= LocalDate.now();
        }
    }

    @GetMapping()
    public Flux<Tuple2<Persona, Integer>> getEmployeeList(){

        return Flux.fromStream(
                Stream.of(new Employee("Carlos","Quintana"),
                        new Employee("Liliana","Agredo"),
                        new Employee("Ana","Leina")))
                .map(employee -> {
                        var em = employee;
                        return Persona.builder().name(em.getNombre()).lastname(em.getApellido()).build();
                    }
                ).doOnError(throwable -> {System.out.println("Error here");})
                .doOnNext(ControllerExampleReactivo::maskNames)
                .zipWith(Flux.fromStream(Stream.of(1,2,3)))
                .doOnError(throwable -> System.out.println("Error during masking"))
                .doOnComplete(() -> System.out.println("All good"));

    }

    @SneakyThrows
    private static Persona maskNames(Persona p){

        p.setLastname("xxxxxxx");
        System.out.println("Waiting for persona " + p.getName());
        Thread.sleep(1000);
        return p;
    }


}
