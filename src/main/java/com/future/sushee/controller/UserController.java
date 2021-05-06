//package com.future.sushee.controller;
//
//import com.future.sushee.payload.request.LoginRequest;
//import com.future.sushee.repository.RoleRepository;
//import com.future.sushee.repository.UserRepository;
//import com.future.sushee.security.jwt.JwtUtils;
//import lombok.AllArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//
//@AllArgsConstructor
//@RestController
//@RequestMapping("/api/user")
//@CrossOrigin(origins = "*", maxAge = 3600)
//public class UserController {
//
//    private final UserRepository userRepository;
//    private final RoleRepository roleRepository;
//    private final PasswordEncoder encoder;
//    private final AuthenticationManager authenticationManager;
//    private final JwtUtils jwtUtils;
//
//    @PostMapping("/signin")
//    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
//
//    }
//}