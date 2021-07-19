package com.future.sushee.controller;

import com.future.sushee.model.EnumRole;
import com.future.sushee.model.Role;
import com.future.sushee.model.User;
import com.future.sushee.payload.request.LoginRequest;
import com.future.sushee.payload.request.SignupRequest;
import com.future.sushee.payload.response.MessageResponse;
import com.future.sushee.payload.response.UserResponse;
import com.future.sushee.repository.RoleRepository;
import com.future.sushee.repository.UserRepository;
import com.future.sushee.security.jwt.JwtUtils;
import com.future.sushee.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    private final UserService userService;

    @GetMapping("")
    public List<UserResponse> getUserAll() {
        List<User> users = userService.getAllUser();
        List<UserResponse> response = new ArrayList<>();
        for(User user: users) {
            response.add(userService.createUserResponse(user));
        }
        return response;
    }

    @GetMapping("/{username}")
    public UserResponse getUserByUsername(@PathVariable String username) {
        return userService.createUserResponse(userService.getUserByUsername(username));
    }

    @PostMapping("/upsert")
    public ResponseEntity<?> upsertUser(@Valid @RequestBody SignupRequest signUpRequest) {
        String message = userService.updateUser(signUpRequest);
        return ResponseEntity.ok().body(new MessageResponse(message));
    }
}