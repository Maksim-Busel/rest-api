package com.epam.esm.validator.impl;

import com.epam.esm.entity.User;
import com.epam.esm.exception.ParameterException;
import com.epam.esm.exception.RegistrationException;
import com.epam.esm.service.api.UserService;
import com.epam.esm.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class UserValidatorImpl extends AbstractValidatorImpl<User> implements UserValidator {
    private final UserService userService;

    private static final String PASSWORD_PATTERN = "\\A(?=\\w*[0-9])(?=\\w*[a-z])(?=\\w*[A-Z])\\S{6,25}\\z";
    private static final String LOGIN_PATTERN = "^[A-Za-z]([A-Za-z0-9-]{3,18})$";

    @Autowired
    public UserValidatorImpl(@Lazy UserService userService) {
        super(userService);
        this.userService = userService;
    }

    @Override
    public void validate(User user) {
        String username = user.getUsername();
        String password = user.getPassword();

        validateUsername(username);
        validateExistenceUserByUsername(username);
        validatePassword(password);
    }

    private void validateUsername(String username) {
        if (!username.matches(LOGIN_PATTERN)) {
            throw new RegistrationException("Invalid login. Login can contain only letters and numbers." +
                    "There must be at least one letter. Login must be between 4-25 characters.");
        }
    }

    private void validateExistenceUserByUsername(String username) {
        User user = userService.getByUsername(username, false);

        if (user != null) {
            throw new ParameterException("User with: " + username + " name already exists");
        }
    }

    @Override
    public void validatePassword(String password) {

        if (!password.matches(PASSWORD_PATTERN)) {
            throw new RegistrationException("Invalid password. Password can contain only letters and numbers. " +
                    "There must be at least one uppercase letter and 1 digit. " +
                    "Password must be between 6-25 characters.");
        }
    }
}
