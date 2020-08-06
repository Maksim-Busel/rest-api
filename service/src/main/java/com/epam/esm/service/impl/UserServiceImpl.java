package com.epam.esm.service.impl;

import com.epam.esm.dao.api.RoleDao;
import com.epam.esm.dao.api.UserDao;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ThereIsNoSuchUserException;
import com.epam.esm.service.api.OrderService;
import com.epam.esm.service.api.UserService;
import com.epam.esm.util.OffsetCalculator;
import com.epam.esm.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl extends AbstractService<User> implements UserService {
    private final OrderService orderService;
    private final UserValidator userValidator;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserDao userDao;
    private final RoleDao roleDao;

    private static final String ROLE_USER = "ROLE_USER";
    private static final String USERNAME_FIELD = "username";

    @Autowired
    public UserServiceImpl(UserDao userDao, RoleDao roleDao, OrderService orderService,
                           BCryptPasswordEncoder passwordEncoder, OffsetCalculator offsetCalculator,
                           UserValidator userValidator) {
        super(userValidator, userDao, offsetCalculator);
        this.orderService = orderService;
        this.passwordEncoder = passwordEncoder;
        this.roleDao = roleDao;
        this.userDao = userDao;
        this.userValidator = userValidator;
    }

    @Override
    @Transactional
    public User add(User user) {
        validator.validate(user);

        Role roleUser = roleDao.findByRoleName(ROLE_USER);
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(userRoles);

        return dao.create(user);
    }

    @Override
    public List<Order> getUserOrdersByUserId(long userId, int pageNumber, int pageSize) {
        validator.validatePageParameters(pageNumber, pageSize);
        validator.validateExistenceEntityById(userId);

        return orderService.getUserOrdersByUserId(userId, pageNumber, pageSize);
    }

    @Override
    public User getByUsername(String username, boolean exceptionIfNotFound) {
        validator.validateString(username, USERNAME_FIELD);

        try {
            return userDao.findByUsername(username);
        } catch (EmptyResultDataAccessException e) {
            if (exceptionIfNotFound) {
                throw new ThereIsNoSuchUserException("User: " + username + " not found", e);
            }

            return null;
        }
    }

    @Override
    @Transactional
    public User edit(User updatedUser) {
        long userId = updatedUser.getId();
        validator.validateExistenceEntityById(userId);
        User userFromDb = dao.findById(userId);

        String updatedUsername = updatedUser.getUsername();
        String updatedPassword = updatedUser.getPassword();

        if (userFromDb.getUsername().equals(updatedUsername)) {
            userValidator.validatePassword(updatedPassword);
        } else {
            validator.validate(updatedUser);
            userFromDb.setUsername(updatedUsername);
        }
        userFromDb.setPassword(passwordEncoder.encode(updatedPassword));

        return userFromDb;
    }

    @Override
    public User getByUsername(String username) {
        return this.getByUsername(username, true);
    }

    @Override
    public User getUserWithLargestAmountOrders() {
        return this.getUserWithLargestAmountOrders(false, false);
    }

    @Override
    public User getUserWithLargestAmountOrders(boolean userLockAllowed, boolean orderLockAllowed) {
        return userDao.findUserWithLargestAmountOrders(userLockAllowed, orderLockAllowed);
    }

    @Override
    public List<User> getAll(int pageNumber, int pageSize) {
        validator.validatePageParameters(pageNumber, pageSize);
        int offset = offsetCalculator.calculate(pageNumber, pageSize);

        return userDao.findAll(offset, pageSize);
    }
}
