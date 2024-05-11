package com.student.crudapp.repository;

import com.student.crudapp.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    List<Student> findAll();
    Student findById(int id);
    int deleteById(int id);


}