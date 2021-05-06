package com.future.sushee.service;

import com.future.sushee.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUser();
    User getUserByUsername(String username);
    User delete(User user);
}
