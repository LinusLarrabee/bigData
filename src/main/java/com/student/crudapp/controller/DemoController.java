package com.student.crudapp.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/test")
public class DemoController {

    @GetMapping(path = "/{branchId}")
    public String getBranch(@PathVariable("branchId") int branchId) {
        if (branchId == 0) {
            return "master";
        } else if (branchId == 1) {
            return "dev";
        }
        return "release";
    }
}
