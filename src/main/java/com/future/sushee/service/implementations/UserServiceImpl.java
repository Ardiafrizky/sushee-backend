package com.future.sushee.service.implementations;

import com.future.sushee.model.EnumRole;
import com.future.sushee.model.Role;
import com.future.sushee.model.User;
import com.future.sushee.payload.request.SignupRequest;
import com.future.sushee.payload.response.JwtResponse;
import com.future.sushee.payload.response.MessageResponse;
import com.future.sushee.payload.response.UserResponse;
import com.future.sushee.repository.RoleRepository;
import com.future.sushee.repository.UserRepository;
import com.future.sushee.security.jwt.JwtUtils;
import com.future.sushee.security.services.UserDetailsImpl;
import com.future.sushee.service.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    @Override
    public List<User> getAllUser() { return userRepository.findAll(); }

    @Override
    public JwtResponse authenticateUser(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles
        );
    }

    @Override
    public ResponseEntity<MessageResponse> registerUser(SignupRequest signUpRequest) {
        if (isExistsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }
        Boolean x = isExistsByEmail(signUpRequest.getEmail());
        if (isExistsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(
                signUpRequest.getUsername(),
                signUpRequest.getFullname(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(EnumRole.ROLE_CLIENT)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "ROLE_ADMIN":
                        Role adminRole = roleRepository.findByName(EnumRole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "ROLE_STAFF":
                        Role modRole = roleRepository.findByName(EnumRole.ROLE_STAFF)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(EnumRole.ROLE_CLIENT)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User '"+ user.getUsername() + "' successfully registered"));
    }

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

        userRepository.save(user);
        return "User '"+ user.getUsername() + "' data successfully updated";
    }
    
    @Override
    public UserResponse createUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setEmail(user.getEmail());
        response.setFullname(user.getFullName());
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
    public Boolean isExistsByUsername(String username) { return userRepository.existsByUsername(username); }

    @Override
    public Boolean isExistsByEmail(String email) { return userRepository.existsByEmail(email); }

    @Override
    public User getUserByUsername(String username){ return userRepository.findByUsername(username).get(); }

    @Override
    public User delete(User user){
        userRepository.delete(user);
        return user;
    }
}
