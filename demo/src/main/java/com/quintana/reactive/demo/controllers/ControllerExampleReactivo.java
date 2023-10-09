package com.quintana.reactive.demo.controllers;


import com.quintana.reactive.demo.services.EmployeeService;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@RestController
@RequestMapping("/reactive")
@PropertySource("classpath:application-messages.yml")
public class ControllerExampleReactivo {

    @Value("${oracle.tuxedo.connectionString}")
    private String connectionString;

    @Autowired
    private EmployeeService servicioEmpleados;

    /**
     *
     * Mala implementacion de un flux por una operacion bloqueante
     * @return Flux<Persona>
     */
    @GetMapping(value = "asyncronousBlocking")
    public Flux<EmployeeService.Persona> asyncronousBlocking(){
        return servicioEmpleados.getPersonas(10);
    }

    /**
     *
     * Correcta implementacion de un flux sin operaciones bloqueantes
     * @return Flux<Employee>
     */
    @GetMapping(value = "/asyncronousNonBlocking")
    public Flux<Employee> asyncronousNonBlocking(){
        return servicioEmpleados.getEmployees(
                servicioEmpleados.getEmployeeList(40));
    }







    @Data
    @Builder
    public static class Employee{
        private Integer id;
        private String nombre;
        private String apellido;
        private LocalDate fechaNacimiento;
        private boolean validated;
    }

}
