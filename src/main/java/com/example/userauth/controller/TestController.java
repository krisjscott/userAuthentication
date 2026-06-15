package com.example.userauth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/student-only")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<String> studentOnly() {
        return ResponseEntity.ok("You are a STUDENT");
    }

    @GetMapping("/teacher-only")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<String> teacherOnly() {
        return ResponseEntity.ok("You are a TEACHER");
    }

    @GetMapping("/any-user")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    public ResponseEntity<String> anyUser() {
        return ResponseEntity.ok("You are authenticated");
    }
}
