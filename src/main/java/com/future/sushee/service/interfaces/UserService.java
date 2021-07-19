package com.future.sushee.service.interfaces;

import com.future.sushee.model.User;
import com.future.sushee.payload.request.SignupRequest;
import com.future.sushee.payload.response.UserResponse;

import java.util.List;

public interface UserService {
    List<User> getAllUser();
    String updateUser(SignupRequest user);
    UserResponse createUserResponse(User user);
    User getUserByUsername(String username);
    User delete(User user);
}