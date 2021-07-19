package com.future.sushee.service;

import com.future.sushee.model.User;
import com.future.sushee.payload.response.UserResponse;

import java.util.List;

public interface UserService {
    List<User> getAllUser();
    UserResponse createUserResponse(User user);
    User getUserByUsername(String username);
    User delete(User user);
}
