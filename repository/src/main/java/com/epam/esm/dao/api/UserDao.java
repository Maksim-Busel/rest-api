package com.epam.esm.dao.api;

import com.epam.esm.entity.User;

import java.util.List;

public interface UserDao extends Dao<User>{

    User findByUsername(String login);

    User findUserWithLargestAmountOrders(boolean userLockAllowed, boolean orderLockAllowed);

    List<User> findAll(int offset, int pageSize);
}
