package com.epam.esm.service.api;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;

import java.util.List;

public interface UserService extends Service<User> {

    User add(User user);

    List<Order> getUserOrdersByUserId(long userId, int pageNumber, int pageSize);

    User getByUsername(String username);

    User getByUsername(String username, boolean exceptionIfNotFound);

    User getUserWithLargestAmountOrders();

    User getUserWithLargestAmountOrders(boolean userLockAllowed, boolean orderLockAllowed);

    List<User> getAll(int pageNumber, int pageSize);

    User edit(User user);
}
