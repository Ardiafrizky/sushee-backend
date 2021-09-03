package com.future.sushee.security;

import com.future.sushee.model.User;
import com.future.sushee.security.services.UserDetailsImpl;
import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("security")
public class UserDetailsImplTest {

    private User user;
    private UserDetailsImpl userDetails1;
    private UserDetailsImpl userDetails2;

    @Test
    public void userDetailsImplConstructTest() throws Exception {
        this.userDetails1 = new UserDetailsImpl("id", "username", "fullname", "email", "password", null);
        assertEquals(userDetails1.getUsername(), "username");
        assertEquals(userDetails1.getEmail(), "email");
        assertEquals(userDetails1.getPassword(), "password");
        assertEquals(userDetails1.getId(), "id");
        assertNull(userDetails1.getAuthorities());

        this.user = new User("username", "fname", "email", "password");
        this.userDetails2 = UserDetailsImpl.build(user);
        assertTrue(userDetails2.isAccountNonExpired());
        assertTrue(userDetails2.isAccountNonLocked());
        assertTrue(userDetails2.isCredentialsNonExpired());
        assertTrue(userDetails2.isEnabled());

        assertTrue(userDetails1.equals(userDetails1));
        assertFalse(userDetails1.equals(new Object()));
        assertFalse(userDetails1.equals(userDetails2));
    }
}
