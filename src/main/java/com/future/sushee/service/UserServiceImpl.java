package com.future.sushee.service;

import com.future.sushee.model.EnumRole;
import com.future.sushee.model.Role;
import com.future.sushee.model.User;
import com.future.sushee.payload.request.SignupRequest;
import com.future.sushee.payload.response.MessageResponse;
import com.future.sushee.payload.response.UserResponse;
import com.future.sushee.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    @Override
    public List<User> getAllUser() { return userRepository.findAll(); }

    @Override
    public String updateUser(SignupRequest userDatas) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDatas.getUsername(), userDatas.getPassword()));

        if (!userRepository.existsByUsername(userDatas.getUsername())) {
            return "User with username '" + userDatas.getUsername() +"' doesn't exists.";
        }
        User user = getUserByUsername(userDatas.getUsername());

        if (userRepository.existsByEmail(userDatas.getEmail())) {
            if (!user.getEmail().equals(userDatas.getEmail()))
                return "Email '" + userDatas.getEmail() + "' already taken.";
        }

        user.setFullName(userDatas.getFullname());
        user.setEmail(userDatas.getEmail());
        user.setImageUrl(userDatas.getImageUrl());

        userRepository.save(user);
        return "Successfully updated";
    }
    
    @Override
    public UserResponse createUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setEmail(user.getEmail());
        response.setFullname(user.getFullName());
        response.setImageUrl(user.getImageUrl());
        response.setUsername(user.getUsername());
        response.setUuid(user.getUuid());

        Set<Role> roles = user.getRoles();
        Set<String> roleNames = new HashSet<>();
        for(Role role: roles) {
            roleNames.add(role.getName().name());
        }
        response.setRoles(roleNames);
        return response;
    }

    @Override
    public User getUserByUsername(String username){ return userRepository.findByUsername(username).get(); }

    @Override
    public User delete(User user){
        userRepository.delete(user);
        return user;
    }
}
