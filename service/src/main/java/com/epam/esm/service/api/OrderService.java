package com.epam.esm.service.api;

import com.epam.esm.entity.Order;

import java.util.List;

public interface OrderService extends Service<Order>{

    List<Order> getUserOrdersByUserId(long userId, int pageNumber, int pageSize);

    Order add(Order entity, List<Integer> certificatesId);

    List<Order> getAll(int pageNumber, int pageSize);

    List<Order> getAll(int pageNumber, int pageSize, boolean exceptionIfNotFound);
}
