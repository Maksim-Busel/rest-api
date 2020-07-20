package com.epam.esm.service.impl;

import com.epam.esm.dao.api.RoleDao;
import com.epam.esm.dao.api.UserDao;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.exception.FailedOperationException;
import com.epam.esm.exception.IncorrectDataException;
import com.epam.esm.exception.ThereIsNoSuchUserException;
import com.epam.esm.service.api.OrderService;
import com.epam.esm.service.api.UserService;
import com.epam.esm.util.OffsetCalculator;
import com.epam.esm.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserDao dao;
    private final Validator<User> validator;
    private final OrderService orderService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleDao roleDao;
    private final OffsetCalculator offsetCalculator;

    private static final String ROLE_USER = "ROLE_USER";
    private static final String USERNAME_FIELD = "username";

    @Autowired
    public UserServiceImpl(UserDao dao, Validator<User> validator, RoleDao roleDao,
                           OrderService orderService, BCryptPasswordEncoder passwordEncoder, OffsetCalculator offsetCalculator) {
        this.dao = dao;
        this.validator = validator;
        this.orderService = orderService;
        this.passwordEncoder = passwordEncoder;
        this.roleDao = roleDao;
        this.offsetCalculator = offsetCalculator;
    }

    @Override
    @Transactional
    public User register(User user) {
        Role roleUser = roleDao.findByRoleName(ROLE_USER);
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);

        validator.validate(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(userRoles);

        try {
            return dao.create(user);
        } catch (DataIntegrityViolationException e) {
            throw new IncorrectDataException("User with: " + user.getUsername() + " username already exists.");
        }
    }

    @Override
    public List<Order> getUserOrdersByUserId(long userId, int pageNumber, int pageSize) {
        validator.validatePageParameters(pageNumber, pageSize);
        validator.validateExcitingEntityById(userId);

        return orderService.getUserOrdersByUserId(userId, pageNumber, pageSize);
    }

    @Override
    public User getByUsername(String username) {
        validator.validateString(username, USERNAME_FIELD);

        try {
            return dao.findByUsername(username);
        } catch (EmptyResultDataAccessException e) {
            throw new ThereIsNoSuchUserException("User: " + username + " not found", e);
        }
    }

    @Override
    public User getUserWithLargestAmountOrders() {
        return dao.findUserWithLargestAmountOrders(false, false);
    }

    @Override
    public User getUserWithLargestAmountOrders(boolean userLockAllowed, boolean orderLockAllowed) {
        return dao.findUserWithLargestAmountOrders(userLockAllowed, orderLockAllowed);
    }

    @Override
    public User getById(long id, boolean exceptionIfNotFound) {
        validator.validateIdValue(id);

        try {
            return dao.findById(id);
        } catch (EmptyResultDataAccessException e) {

            if (exceptionIfNotFound) {
                throw new ThereIsNoSuchUserException("User: " + id + " doesn't exist", e);
            }
            return null;
        }
    }

    @Override
    public User getById(long id) {
        return this.getById(id, true);
    }

    @Override
    public List<User> getAll(int pageNumber, int pageSize, boolean exceptionIfNotFound) {
        validator.validatePageParameters(pageNumber, pageSize);
        int offset = offsetCalculator.calculate(pageNumber, pageSize);

        List<User> users = dao.findAll(offset, pageSize);
        if (users.size() == 0 && exceptionIfNotFound) {
            throw new ThereIsNoSuchUserException("Not found any users for your request");
        }

        return users;
    }

    @Override
    public List<User> getAll(int pageNumber, int pageSize) {
        return this.getAll(pageNumber, pageSize, true);
    }

    @Override
    @Transactional
    public void lock(long id) {
        validator.validateExcitingEntityById(id);
        int result = dao.lockById(id);

        if (result == 0) {
            throw new FailedOperationException("Failed to delete user " + id);
        }
    }
}
