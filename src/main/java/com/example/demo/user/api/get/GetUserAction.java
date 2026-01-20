package com.example.demo.user.api.get;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetUserAction {

    @GetMapping("/users")
    public ResponseEntity<Integer> getUsers() {
        return ResponseEntity.ok(1);
    }

}
