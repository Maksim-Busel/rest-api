package com.epam.esm.security;

import com.epam.esm.entity.User;
import com.epam.esm.exception.ThereIsNoSuchUserException;
import com.epam.esm.security.jwt.JwtUser;
import com.epam.esm.security.jwt.JwtUserFactory;
import com.epam.esm.service.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    private final UserService userService;
    private final JwtUserFactory jwtUserFactory;

    @Autowired
    public JwtUserDetailsService(@Lazy UserService userService, JwtUserFactory jwtUserFactory) {
        this.userService = userService;
        this.jwtUserFactory = jwtUserFactory;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getByUsername(username);

        if(user == null){
            throw new ThereIsNoSuchUserException("User: " + username + " doesn't exist");
        }

        JwtUser jwtUser = jwtUserFactory.create(user);

        return jwtUser;
    }
}
