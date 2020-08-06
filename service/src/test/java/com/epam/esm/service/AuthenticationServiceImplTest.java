package com.epam.esm.service;

import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.security.jwt.JwtTokenProvider;
import com.epam.esm.service.api.AuthenticationService;
import com.epam.esm.service.api.UserService;
import com.epam.esm.service.impl.AuthenticationServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceImplTest {
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private UserService userService;
    @Mock
    private AuthenticationManager authenticationManager;

    private AuthenticationService authenticationService;

    @Before
    public void setUp() {
        authenticationService = new AuthenticationServiceImpl(jwtTokenProvider, userService, authenticationManager);
    }

    @Test
    public void loginWhenAuthenticationSuccessShouldEndWithoutError(){
        List<Role> roles = new ArrayList<>();
        roles.add(new Role());
        User user = new User();
        user.setRoles(roles);
        String token = "token";
        String username = "Username";
        String password = "Password1";

        when(userService.getByUsername(username)).thenReturn(user);
        when(jwtTokenProvider.createToken(username, roles)).thenReturn(token);

        authenticationService.login(username, password);

        verify(authenticationManager, times(1)).authenticate(any());
        verify(userService, times(1)).getByUsername(username);
        verify(jwtTokenProvider, times(1)).createToken(username, roles);
    }
}
