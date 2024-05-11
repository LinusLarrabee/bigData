package com.student.crudapp.hadoop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HadoopController {

    @Autowired
    private HadoopFileService hadoopFileService;

    @GetMapping("/createFile")
    public ResponseEntity<String> createFile(@RequestParam String path) {
        hadoopFileService.createFile(path);
        return ResponseEntity.ok("File created successfully");
    }

    @GetMapping("/readFile")
    public ResponseEntity<String> readFile(@RequestParam String path) {
        String content = hadoopFileService.readFile(path);
        return ResponseEntity.ok(content);
    }
}
