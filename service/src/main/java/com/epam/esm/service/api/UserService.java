package com.epam.esm.service.api;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;

import java.util.List;

public interface UserService extends Service<User> {

    User register(User user);

    List<Order> getUserOrdersByUserId(long userId, int pageNumber, int pageSize);

    User getByUsername(String username);

    User getUserWithLargestAmountOrders();

    User getUserWithLargestAmountOrders(boolean userLockAllowed, boolean orderLockAllowed);

    List<User> getAll(int pageNumber, int pageSize);

    List<User> getAll(int pageNumber, int pageSize, boolean exceptionIfNotFound);
}
