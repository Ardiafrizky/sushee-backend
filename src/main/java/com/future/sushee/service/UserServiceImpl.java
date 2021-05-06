package com.future.sushee.service;

import com.future.sushee.model.User;
import com.future.sushee.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public List<User> getAllUser() { return userRepository.findAll(); }

    @Override
    public User getUserByUsername(String username){ return userRepository.findByUsername(username).get(); }

    @Override
    public User delete(User user){
        userRepository.delete(user);
        return user;
    }
}
