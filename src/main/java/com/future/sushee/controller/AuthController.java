package com.future.sushee.controller;

import com.future.sushee.payload.request.LoginRequest;
import com.future.sushee.payload.request.SignupRequest;
import com.future.sushee.service.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private final UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(
                userService.authenticateUser(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        ResponseEntity<?> response = userService.registerUser(signUpRequest);
        if (!response.getStatusCode().equals(HttpStatus.OK)) { return response;}
        return ResponseEntity.ok(
                userService.authenticateUser(
                        signUpRequest.getUsername(),
                        signUpRequest.getPassword()
                )
        );
    }
}
