package com.future.sushee.payload.response;

import com.future.sushee.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String uuid;
    private String email;
    private String username;
    private String fullname;
    private String imageUrl;
    private Set<String> roles;
}
