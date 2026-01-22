package com.example.demo.user.api.test;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/auth/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<@NotNull String> admin() {
        return ResponseEntity.ok("admin");
    }

    @GetMapping("/auth/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<@NotNull String> user() {
        return ResponseEntity.ok("user");
    }
}
