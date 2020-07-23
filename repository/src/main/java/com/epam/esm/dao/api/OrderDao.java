package com.epam.esm.dao.api;

import com.epam.esm.entity.Order;

import java.util.List;

public interface OrderDao extends Dao<Order>{

    List<Order> findOrdersByUserId(long userId, int offset, int pageSize);

    List<Order> findAll(int offset, int pageSize);

}
