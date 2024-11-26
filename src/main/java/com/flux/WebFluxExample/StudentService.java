package com.flux.WebFluxExample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    public Mono<Student> findStudById(long id){
        return studentRepository.findById(id);
    }

    public Flux<Student> findStudByName(String name){
        if(name == null || name.isEmpty())
            return studentRepository.findAll();
        else
            return studentRepository.findByName(name);
    }

    public Mono<Student> addStud(Student student) {
        return studentRepository.save(student);
    }
    public Mono<Void> delStud(Student student) {
        return studentRepository.delete(student);
    }


    public  Mono<Student> updStud(long id, Student student) {
        return studentRepository.findById(id)
                        .flatMap(st->{
                            student.setId(st.getId());
                            return  studentRepository.save(student);
                        });

    }
}
