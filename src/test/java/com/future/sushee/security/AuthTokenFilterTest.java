package com.future.sushee.security;

import com.future.sushee.security.jwt.AuthTokenFilter;
import com.future.sushee.security.jwt.JwtUtils;
import com.future.sushee.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;

@Tag("security")
public class AuthTokenFilterTest {
    @Spy
    @InjectMocks
    AuthTokenFilter authTokenFilter;

    @Mock
    JwtUtils jwtUtils;

    @Mock
    UserDetailsServiceImpl userDetailsService;

    @Mock
    HttpServletResponse httpServletResponse;

    @Mock
    HttpServletRequest httpServletRequest;

    @Mock
    FilterChain filterChain;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void doFilterInternalTests() throws Exception {
        String token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwb3N0bWFuIiwiaWF0IjoxNjE5NDM2MjUyLCJleHAiOjE2MTk1MjI2NTJ9.5VGhn8rN1BoS5VK8nNqt7Zoey2up0nAZqxMLamTfLWyNEcCu9kjSvkZKpF2Bh5bqo3JoSEU9a3Bifyr0X8MFTw";
        doReturn(token).when(authTokenFilter).parseJwt(httpServletRequest);
        when(jwtUtils.validateJwtToken(ArgumentMatchers.anyString())).thenReturn(Boolean.TRUE);

        authTokenFilter.doFilterInternal(httpServletRequest,httpServletResponse,filterChain);
        verify(filterChain).doFilter(httpServletRequest, httpServletResponse);
    }
}
