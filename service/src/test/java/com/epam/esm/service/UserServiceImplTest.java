package com.epam.esm.service;

import com.epam.esm.dao.api.RoleDao;
import com.epam.esm.dao.api.UserDao;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ThereIsNoSuchUserException;
import com.epam.esm.service.api.OrderService;
import com.epam.esm.service.api.UserService;
import com.epam.esm.service.impl.UserServiceImpl;
import com.epam.esm.util.OffsetCalculator;
import com.epam.esm.validator.UserValidator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {
    private static final String ROLE_USER = "ROLE_USER";
    private static final String USERNAME_FIELD = "username";
    int pageNumber = 1;
    int pageSize = 10;

    @Mock
    private OrderService orderService;
    @Mock
    private UserValidator userValidator;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private UserDao userDao;
    @Mock
    private RoleDao roleDao;
    @Mock
    private OffsetCalculator calculator;

    private User user = mock(User.class);

    private UserService userService;

    @Before
    public void setUp() {
        userService = new UserServiceImpl(userDao, roleDao, orderService, passwordEncoder, calculator, userValidator);
    }

    @Test
    public void addWhenUserCorrectShouldExecuteValidateFindByRoleNameCreateOneTime() {
        userService.add(user);

        verify(userValidator, times(1)).validate(user);
        verify(roleDao, times(1)).findByRoleName(ROLE_USER);
        verify(userDao, times(1)).create(user);
    }

    @Test
    public void getUserOrdersByUserIdWhenUserCorrectShouldExecuteThreeMethodsOneTime() {
        long userId = 1;

        userService.getUserOrdersByUserId(userId, pageNumber, pageSize);

        verify(userValidator, times(1)).validatePageParameters(pageNumber, pageSize);
        verify(userValidator, times(1)).validateExistenceEntityById(userId);
        verify(orderService, times(1)).getUserOrdersByUserId(userId, pageNumber, pageSize);
    }

    @Test
    public void getByUsernameWhenExistUserWithThisNameShouldExecuteValidateStringAndFindByUsernameOneTime() {
        String username = "Mikola";
        boolean exceptionIfNotFound = true;

        userService.getByUsername(username, exceptionIfNotFound);

        verify(userValidator, times(1)).validateString(username, USERNAME_FIELD);
        verify(userDao, times(1)).findByUsername(username);
    }

    @Test(expected = ThereIsNoSuchUserException.class)
    public void getByUsernameWhenNoExistUserWithThisNameAndExceptionIfNotFoundTrueShouldThrowThereIsNoSuchUserException() {
        String username = "Mikola";
        boolean exceptionIfNotFound = true;
        doThrow(EmptyResultDataAccessException.class).when(userDao).findByUsername(username);

        userService.getByUsername(username, exceptionIfNotFound);

        verify(userValidator, times(1)).validateString(username, USERNAME_FIELD);
        verify(userDao, times(1)).findByUsername(username);
    }

    @Test
    public void getByUsernameWhenNoExistUserWithThisNameAndExceptionIfNotFoundFalseShouldReturnNull() {
        String username = "Mikola";
        boolean exceptionIfNotFound = false;
        doThrow(EmptyResultDataAccessException.class).when(userDao).findByUsername(username);

        User nullUser = userService.getByUsername(username, exceptionIfNotFound);

        Assert.assertNull(nullUser);
        verify(userValidator, times(1)).validateString(username, USERNAME_FIELD);
        verify(userDao, times(1)).findByUsername(username);
    }

    @Test
    public void getUserWithLargestAmountOrdersWhenNoExistUserWithThisNameAndExceptionIfNotFoundFalseShouldReturnNull() {
        boolean userLockAllowed = false;
        boolean orderLockAllowed = false;

        userService.getUserWithLargestAmountOrders(userLockAllowed, orderLockAllowed);

        verify(userDao, times(1)).findUserWithLargestAmountOrders(userLockAllowed, orderLockAllowed);
    }

    @Test
    public void getAllShouldExecuteFindAllAndValidatePageParametersOneTime() {
        int pageNumber = 1;
        int pageSize = 10;
        when(calculator.calculate(pageNumber, pageSize)).thenReturn(0);
        int expectedOffset = 0;

        userService.getAll(pageNumber, pageSize);

        verify(userValidator, times(1)).validatePageParameters(pageNumber, pageSize);
        verify(calculator, times(1)).calculate(pageNumber, pageSize);
        verify(userDao, times(1)).findAll(expectedOffset, pageSize);
    }
}
