package com.epam.esm.validator;

import com.epam.esm.entity.User;

public interface UserValidator extends Validator<User>{
    void validatePassword(String password);
}
