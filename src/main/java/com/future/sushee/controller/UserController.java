package com.future.sushee.controller;

import com.future.sushee.model.EnumRole;
import com.future.sushee.model.User;
import com.future.sushee.payload.request.ChangePasswordRequest;
import com.future.sushee.payload.request.LoginRequest;
import com.future.sushee.payload.request.SignupRequest;
import com.future.sushee.payload.response.MessageResponse;
import com.future.sushee.payload.response.UserResponse;
import com.future.sushee.service.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

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

    @GetMapping("/client")
    public List<UserResponse> getUserClient() {
        List<User> users = userService.getAllUser();
        List<UserResponse> response = new ArrayList<>();
        for(User user: users) {
            if (user.getRoles().stream().anyMatch(o -> o.getName().equals(EnumRole.ROLE_CLIENT)))
                response.add(userService.createUserResponse(user));
        }
        return response;
    }

    @GetMapping("/staff")
    public List<UserResponse> getUserStaff() {
        List<User> users = userService.getAllUser();
        List<UserResponse> response = new ArrayList<>();
        for(User user: users) {
            if (user.getRoles().stream().anyMatch(o -> o.getName().equals(EnumRole.ROLE_STAFF)))
                response.add(userService.createUserResponse(user));
        }
        return response;
    }

    @GetMapping("/admin")
    public List<UserResponse> getUserAdmin() {
        List<User> users = userService.getAllUser();
        List<UserResponse> response = new ArrayList<>();
        for(User user: users) {
            if (user.getRoles().stream().anyMatch(o -> o.getName().equals(EnumRole.ROLE_ADMIN)))
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

    @DeleteMapping("/{username}")
    public UserResponse deleteUser(@PathVariable String username) {
        User user = userService.delete(userService.getUserByUsername(username));
        return userService.createUserResponse(user);
    }

    @PostMapping("/change-password")
    public UserResponse changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        return userService.changePassword(changePasswordRequest);
    }
}