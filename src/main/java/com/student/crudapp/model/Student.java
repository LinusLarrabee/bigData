package com.student.crudapp.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "Student")
public class Student {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    private String name;

    private String email;

    private String grade;
    public Student(String a){
        name = a;
        email = a;
        grade = a;
    }

    public Student() {

    }
}
