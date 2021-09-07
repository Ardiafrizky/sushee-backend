package com.future.sushee.service.interfaces;

import com.future.sushee.model.User;
import com.future.sushee.payload.request.ChangePasswordRequest;
import com.future.sushee.payload.request.SignupRequest;
import com.future.sushee.payload.response.JwtResponse;
import com.future.sushee.payload.response.UserResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    List<User> getAllUser();
    JwtResponse authenticateUser(String username, String password);
    ResponseEntity<?> registerUser(SignupRequest signupRequest);
    UserResponse changePassword(ChangePasswordRequest changePasswordRequest);
    String updateUser(SignupRequest user);
    Boolean isExistsByUsername(String username);
    Boolean isExistsByEmail(String email);
    UserResponse createUserResponse(User user);
    User getUserByUsername(String username);
    User delete(User user);
}
