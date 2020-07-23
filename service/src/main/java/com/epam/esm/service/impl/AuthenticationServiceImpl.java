package com.epam.esm.service.impl;

import com.epam.esm.entity.User;
import com.epam.esm.exception.AuthenticationDataException;
import com.epam.esm.security.jwt.JwtTokenProvider;
import com.epam.esm.service.api.AuthenticationService;
import com.epam.esm.service.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationServiceImpl(JwtTokenProvider jwtTokenProvider, UserService userService, AuthenticationManager authenticationManager) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    public String login(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            User user = userService.getByUsername(username);

            return jwtTokenProvider.createToken(username, user.getRoles());
        } catch (AuthenticationException e) {
            throw new AuthenticationDataException("Invalid username or password");
        }
    }
}
