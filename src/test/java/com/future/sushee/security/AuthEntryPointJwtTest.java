package com.future.sushee.security;

import com.future.sushee.security.jwt.AuthEntryPointJwt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;

@Tag("security")
public class AuthEntryPointJwtTest {

    @InjectMocks
    AuthEntryPointJwt authEntryPointJwt;

    @Mock
    HttpServletRequest httpServletRequest;

    @Mock
    HttpServletResponse httpServletResponse;

    @Mock
    AuthenticationException authenticationException;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void commenceTest() throws Exception {
        doNothing().when(httpServletResponse).sendError(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString());
        authEntryPointJwt.commence(httpServletRequest, httpServletResponse, authenticationException);
        verify(httpServletResponse).sendError(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString());
    }
}
