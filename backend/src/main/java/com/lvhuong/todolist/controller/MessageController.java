package com.lvhuong.todolist.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

// For frontend testing
@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class MessageController {

    @GetMapping("/messages")
    public ResponseEntity<List<String>> message(){
        return ResponseEntity.ok(Arrays.asList("first", "second"));
    }
}
