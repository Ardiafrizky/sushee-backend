package com.future.sushee.security;

import com.future.sushee.security.jwt.JwtUtils;
import com.future.sushee.security.services.UserDetailsImpl;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.Authentication;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Tag("security")
public class JwtUtilsTest {

    @InjectMocks
    JwtUtils jwtUtils;

    @Mock
    Authentication authentication;

    @Mock
    UserDetailsImpl userDetails;

    @Mock
    JwtBuilder jwtBuilder;

    private String token;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        this.token = Jwts.builder()
                .setSubject("username")
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + 8640000))
                .signWith(SignatureAlgorithm.HS512, "susheeSecretKey")
                .compact();
    }

    @Test
    public void generateJwtTokenTest() throws Exception {
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtBuilder.signWith(ArgumentMatchers.any(), ArgumentMatchers.anyString())).thenReturn(jwtBuilder);
        when(userDetails.getUsername()).thenReturn("username");

        String result = jwtUtils.generateJwtToken(authentication);
        assertEquals(result.getClass(), String.class);
    }

    @Test
    public void getUserNameFromJwtTokenTest() throws Exception {
        String result = jwtUtils.getUserNameFromJwtToken(token);
        assertEquals(result, "username");
    }

    @Test
    public void validateJwtTokenSuccessTest() throws Exception {
        boolean result = jwtUtils.validateJwtToken(token);
        assertTrue(result);
    }

    @Test
    public void validateJwtTokenInvalidSignatureTest() throws Exception {
        this.token = Jwts.builder()
                .setSubject("username")
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + 8640000))
                .signWith(SignatureAlgorithm.HS512, "susheeSecretKey2")
                .compact();
        boolean result = jwtUtils.validateJwtToken(token);
        assertFalse(result);
    }

    @Test
    public void validateJwtTokenInvalidTokenTest() throws Exception {
        this.token = Jwts.builder()
                .setSubject("username")
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + 8640000))
                .signWith(SignatureAlgorithm.HS512, "susheeSecretKey")
                .compact();
        boolean result = jwtUtils.validateJwtToken(token.substring(1));
        assertFalse(result);
    }

    @Test
    public void validateJwtTokenExpiredTest() throws Exception {
        this.token = Jwts.builder()
                .setSubject("username")
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + -8640000))
                .signWith(SignatureAlgorithm.HS512, "susheeSecretKey")
                .compact();
        boolean result = jwtUtils.validateJwtToken(token);
        assertFalse(result);
    }

    @Test
    public void validateJwtTokenEmptyTest() throws Exception {
        boolean result = jwtUtils.validateJwtToken("");
        assertFalse(result);
    }
}
