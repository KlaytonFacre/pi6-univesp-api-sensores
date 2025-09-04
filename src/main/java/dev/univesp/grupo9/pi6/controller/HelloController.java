package dev.univesp.grupo9.pi6.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/hello")
public class HelloController {

@GetMapping
public ResponseEntity<Map<String, String>> sayHello() {

    Map<String, String> response = new HashMap<>();
    response.put("message", "Hello World!");
    return ResponseEntity.status(HttpStatus.OK).body(response);
}
}
