package com.example.userauth.controller;

import com.example.userauth.model.User;
import com.example.userauth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@RequestMapping("/users")
//public class UserController {
//    private final UserService userService;
//    @Autowired
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }
//
//    @GetMapping("/me")
//    public ResponseEntity<User> authenticateUser(){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User currentUser = (User) authentication.getPrincipal();
//        return ResponseEntity.ok(currentUser);
//    }
//
//    @GetMapping("/")
//    public ResponseEntity<List<User>> getAllUsers(){
//        List<User> users = userService.allUsers();
//        return ResponseEntity.ok(users);
//    }
//}
@RestController
@RequestMapping("/test")
public class UserController {

    // Only STUDENT can access
    @GetMapping("/student-only")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<String> studentOnly() {
        return ResponseEntity.ok("You are a STUDENT ✓");
    }

    // Only TEACHER can access
    @GetMapping("/teacher-only")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<String> teacherOnly() {
        return ResponseEntity.ok("You are a TEACHER ✓");
    }

    // Both STUDENT and TEACHER can access
    @GetMapping("/any-user")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    public ResponseEntity<String> anyUser() {
        return ResponseEntity.ok("You are authenticated ✓");
    }
}