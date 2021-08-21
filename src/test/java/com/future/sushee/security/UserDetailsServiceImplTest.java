package com.future.sushee.security;

import com.future.sushee.model.User;
import com.future.sushee.repository.UserRepository;
import com.future.sushee.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.mockito.Mockito.when;

@Tag("security")
public class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.user = new User("username", "email1", "fullname1", "password1");
    }

    @Test
    public void loadUserByUserNameTest() throws Exception {
        when(userRepository.findByUsername("username")).thenReturn(java.util.Optional.ofNullable(user));
        UserDetails result = userDetailsService.loadUserByUsername("username");
        Assertions.assertEquals(result.getUsername(), "username");
        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("usernameFalse");
        });
    }
}
