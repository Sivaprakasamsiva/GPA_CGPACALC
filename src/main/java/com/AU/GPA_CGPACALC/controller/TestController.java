package com.AU.GPA_CGPACALC.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "GPA Calculator API is running!";
    }

    @GetMapping("/auth/test")
    public String authTest() {
        return "Authentication endpoint is accessible!";
    }
}