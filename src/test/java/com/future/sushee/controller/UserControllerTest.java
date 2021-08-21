package com.future.sushee.controller;

import com.future.sushee.model.User;
import com.future.sushee.payload.request.SignupRequest;
import com.future.sushee.service.implementations.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static javax.swing.UIManager.get;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("controller")
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;

    private User user1;
    private List<User> users;
    private SignupRequest signupRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        this.signupRequest = new SignupRequest("name1", "fullname1", "email1", "password", null);
        this.user1 = new User("name1", "fullname1", "email1", "password");
        this.users = Collections.singletonList(user1);
    }

    @Test
    public void getUserAll() throws Exception {
        when(userService.getAllUser()).thenReturn(users);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/user")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getUserByUsernameTest() throws Exception {
        when(userService.getUserByUsername("name1")).thenReturn(user1);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/user/name1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void upsertUser() throws Exception {
        when(userService.updateUser(ArgumentMatchers.any())).thenReturn("ye");

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/user/upsert")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"username\": \"bobi\",\n" +
                        "    \"fullname\": \"bobia bin bibo\",\n" +
                        "    \"email\": \"bobi@gm.com\",\n" +
                        "    \"password\": \"12345678\",\n" +
                        "    \"role\": [\"ROLE_CLIENT\"]\n" +
                        "}"))
                .andExpect(status().isOk());
    }
}
