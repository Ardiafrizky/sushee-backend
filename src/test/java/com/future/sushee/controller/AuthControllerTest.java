package com.future.sushee.controller;

import com.future.sushee.payload.request.LoginRequest;
import com.future.sushee.payload.request.SignupRequest;
import com.future.sushee.payload.response.JwtResponse;
import com.future.sushee.service.implementations.OrderServiceImpl;
import com.future.sushee.service.implementations.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Tag("controller")
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void authenticateUserTest() throws Exception {
        when(userService.authenticateUser(
                ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(new JwtResponse("a", "1", "u", "e", null));

        mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"username\": \"user-1\",\n" +
                        "    \"password\": \"12345678\"\n" +
                        "}"))
                .andExpect(jsonPath("$.accessToken", is("a")));
    }

    @Test
    public void registerUserTest() throws Exception {
        when(userService.registerUser(ArgumentMatchers.any())).thenReturn(null);
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"username\": \"user-s4\",\n" +
                        "    \"fullname\": \"user-4 bin user-0\",\n" +
                        "    \"email\": \"users4@g.com\",\n" +
                        "    \"password\": \"12345678\",\n" +
                        "    \"role\": [\"ROLE_CLIENT\"]\n" +
                        "}"));

        verify(userService).registerUser(ArgumentMatchers.any());
    }
}
