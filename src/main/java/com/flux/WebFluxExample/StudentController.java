package com.flux.WebFluxExample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/stud")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Student>> getStudent(@PathVariable long id){
        return studentService.findStudById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<Student> listOfStud(@RequestParam(name = "name", required = false) String name){
        return studentService.findStudByName(name);
    }

    @PostMapping
    public Mono<Student> addNewStudent(@RequestBody Student student){
        return studentService.addStud(student);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteStudent(@PathVariable long id){
        return studentService.findStudById(id)
                .flatMap(stud->studentService.delStud(stud)
                        .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
                )
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Student>> updateStudent(@PathVariable long id
                                                    ,@RequestBody Student student){
        return studentService.updStud(id, student)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
