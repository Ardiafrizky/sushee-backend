package com.future.sushee.service;

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
import com.future.sushee.service.implementations.UserServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("service")
public class UserServiceTest {

    @Spy
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetailsImpl userDetails;

//    private Role roleStaff;
//    private Role roleAdmin;
    private User user1;
    private User user2;
    private List<User> users;
    private SignupRequest signupRequest;
    private Set<String> roles;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

//        this.roleStaff = new Role(1, EnumRole.ROLE_STAFF);
        this.user1 = new User("username1", "email1", "fullname1", "password1");
        this.user2 = new User("username2", "email2", "fullname2", "password2");
        this.signupRequest = new SignupRequest("username", "email", "password", "fullname", null);
        this.roles = new HashSet<>(Collections.singletonList("ROLE_STAFF"));
    }

    @AfterEach()
    public void afterEach() {
        reset(userService, userRepository, roleRepository);
    }


    @Test
    public void getAllUserTest() {
        when(userRepository.findAll()).thenReturn(users);
        List<User> result = userService.getAllUser();

        verify(userRepository).findAll();
        assertEquals(result, users);
    }

    // TODO: authenticateUserTest
    @Test
    public void authenticateUserTest() {
        when(authenticationManager.authenticate(ArgumentMatchers.any())).thenReturn(authentication);
        when(jwtUtils.generateJwtToken(ArgumentMatchers.any())).thenReturn("jwt");
        when(authentication.getPrincipal()).thenReturn(userDetails);

        JwtResponse result = userService.authenticateUser("name", "password");
        assertEquals(result.getClass(), JwtResponse.class);
    }

    @Test
    public void registerUserExistUsernameTest() {
        doReturn(Boolean.TRUE).when(userService).isExistsByUsername(ArgumentMatchers.anyString());
        ResponseEntity<MessageResponse> result = userService.registerUser(signupRequest);
        verify(userService).isExistsByUsername(ArgumentMatchers.anyString());
        assertEquals(Objects.requireNonNull(result.getBody()).getMessage(), "Error: Username is already taken!" );
    }

    @Test
    public void registerUserExistEmailTest() {
        doReturn(Boolean.FALSE).when(userService).isExistsByUsername(ArgumentMatchers.anyString());
        doReturn(Boolean.TRUE).when(userService).isExistsByEmail(ArgumentMatchers.anyString());

        ResponseEntity<MessageResponse> result = userService.registerUser(signupRequest);
        verify(userService).isExistsByUsername(ArgumentMatchers.anyString());
        assertEquals(Objects.requireNonNull(result.getBody()).getMessage(), "Error: Email is already in use!" );
    }

    @Test
    public void registerUserEmptyRoleTest() {
        signupRequest.setRole(new HashSet<>(Collections.singletonList("ROLE_STAFF")));
        doReturn(Boolean.FALSE).when(userService).isExistsByUsername(ArgumentMatchers.anyString());
        doReturn(Boolean.FALSE).when(userService).isExistsByEmail(ArgumentMatchers.anyString());
        when(roleRepository.findByName(ArgumentMatchers.any())).thenReturn(Optional.of(new Role()));

        ResponseEntity<MessageResponse> result = userService.registerUser(signupRequest);
        verify(userService).isExistsByUsername(ArgumentMatchers.anyString());
    }

    @Test
    public void registerUserStaffTest() {
        SignupRequest signupRequest = new SignupRequest("username", "email", "password", "fullname", roles);
        doReturn(Boolean.FALSE).when(userService).isExistsByUsername(ArgumentMatchers.anyString());
        doReturn(Boolean.FALSE).when(userService).isExistsByEmail(ArgumentMatchers.anyString());
        when(roleRepository.findByName(EnumRole.ROLE_STAFF)).thenReturn(Optional.of(new Role()));

        ResponseEntity<MessageResponse> result = userService.registerUser(signupRequest);
        verify(userService).isExistsByUsername(ArgumentMatchers.anyString());
        assertEquals(Objects.requireNonNull(result.getBody()).getMessage(), "User 'username' successfully registered" );
    }

    @Test
    public void registerUserAdminTest() {
        this.roles = new HashSet<>(Collections.singletonList("ROLE_ADMIN"));
        SignupRequest signupRequest = new SignupRequest("username", "email", "password", "fullname", roles);

        doReturn(Boolean.FALSE).when(userService).isExistsByUsername(ArgumentMatchers.anyString());
        doReturn(Boolean.FALSE).when(userService).isExistsByEmail(ArgumentMatchers.anyString());
        when(roleRepository.findByName(EnumRole.ROLE_ADMIN)).thenReturn(Optional.of(new Role()));

        ResponseEntity<MessageResponse> result = userService.registerUser(signupRequest);
        verify(userService).isExistsByUsername(ArgumentMatchers.anyString());
        assertEquals(Objects.requireNonNull(result.getBody()).getMessage(), "User 'username' successfully registered" );
    }

    @Test
    public void registerUserWithoutRoleTest() {
        SignupRequest signupRequest = new SignupRequest("username", "email", "password", "fullname", null);

        doReturn(Boolean.FALSE).when(userService).isExistsByUsername(ArgumentMatchers.anyString());
        doReturn(Boolean.FALSE).when(userService).isExistsByEmail(ArgumentMatchers.anyString());

        assertThrows(RuntimeException.class, () -> {
            userService.registerUser(signupRequest);
        });
    }

    @Test
    public void updateUserTest() {
        SignupRequest signupRequest = new SignupRequest("username", "email", "password", "fullname", roles);
        when(userRepository.existsByUsername(ArgumentMatchers.anyString())).thenReturn(Boolean.TRUE);
        when(userRepository.existsByEmail(ArgumentMatchers.anyString())).thenReturn(Boolean.FALSE);
        when(authenticationManager.authenticate(ArgumentMatchers.any())).thenReturn(null);
        doReturn(user1).when(userService).getUserByUsername(ArgumentMatchers.anyString());

        String result = userService.updateUser(signupRequest);
        verify(userRepository).existsByEmail(signupRequest.getEmail());
        verify(userRepository).existsByUsername(signupRequest.getUsername());
        verify(authenticationManager).authenticate(ArgumentMatchers.any());
        assertEquals(result, "User '"+user1.getUsername()+"' data successfully updated");
    }

    @Test
    public void createUserResponseTest() {
        user1.setUuid("uuid");
        Role role = new Role();
        role.setName(EnumRole.ROLE_STAFF);
        user1.setRoles(new HashSet<>(Collections.singletonList(role)));

        UserResponse result = userService.createUserResponse(user1);
        assertEquals(result.getEmail(), user1.getEmail());
        assertEquals(result.getFullname(), user1.getFullName());
        assertEquals(result.getUsername(), user1.getUsername());
        assertEquals(result.getUuid(), user1.getUuid());
        assertEquals(
                result.getRoles().iterator().next(),
                user1.getRoles().iterator().next().getName().name());
    }

    @Test
    public void isExistByUsernameTest() {
        when(userRepository.existsByUsername(ArgumentMatchers.anyString())).thenReturn(Boolean.TRUE);

        Boolean result = userService.isExistsByUsername("test");
        assertTrue(result);
    }

    @Test
    public void isExistByEmailTest() {
        when(userRepository.existsByEmail(ArgumentMatchers.anyString())).thenReturn(Boolean.TRUE);

        Boolean result = userService.isExistsByEmail("test");
        assertTrue(result);
    }

    @Test
    public void getUserByUsernameTest() {
        when(userRepository.findByUsername(ArgumentMatchers.anyString())).thenReturn(Optional.ofNullable(user1));

        User result = userService.getUserByUsername("test");
        assertEquals(result, user1);
    }

    @Test
    public void deleteTest() {
        User result = userService.delete(user1);
        assertEquals(result, user1);
    }
}
