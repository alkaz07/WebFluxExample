package com.flux.WebFluxExample;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
public class StudentHandler {
    private StudentService studentService;

    public StudentHandler(StudentService studentService) {
        this.studentService = studentService;
    }

    public Mono<ServerResponse> getStudent(ServerRequest serverRequest){
        long id = Long.parseLong(serverRequest.pathVariable("id"));
        Mono<Student> studentMono =  studentService.findStudById(id);
        return studentMono.flatMap(st->ServerResponse.ok()
                  .body(fromValue(st))
                  .switchIfEmpty(ServerResponse.notFound().build())
        );

    }

    public Mono<ServerResponse> getStudentsByName(ServerRequest serverRequest){
       // long id = Long.parseLong(serverRequest.pathVariable("id"));
        String name = serverRequest.queryParam("name").orElse(null);
        Flux<Student> studentFlux =  studentService.findStudByName(name);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(studentFlux, Student.class);


    }
}
