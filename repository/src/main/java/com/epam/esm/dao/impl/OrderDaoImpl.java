package com.epam.esm.dao.impl;

import com.epam.esm.dao.api.OrderDao;
import com.epam.esm.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class OrderDaoImpl extends AbstractDao<Order> implements OrderDao {
    @PersistenceContext
    private final EntityManager entityManager;

    @Autowired
    public OrderDaoImpl(EntityManager entityManager) {
        super(entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public Order findById(long id) {
        TypedQuery<Order> orderQuery = entityManager.createNamedQuery(Order.QueryNames.FIND_BY_ID, Order.class);
        orderQuery.setParameter("orderId", id);

        return orderQuery.getSingleResult();
    }

    @Override
    public List<Order> findAll(int offset, int pageSize) {
        TypedQuery<Order> orderQuery = entityManager.createNamedQuery(Order.QueryNames.FIND_ALL, Order.class);
        orderQuery.setFirstResult(offset);
        orderQuery.setMaxResults(pageSize);

        return orderQuery.getResultList();
    }

    @Override
    public int lockById(long id) {
        Query orderQuery = entityManager.createNamedQuery(Order.QueryNames.LOCK_BY_ID);
        orderQuery.setParameter("orderId", id);

        return orderQuery.executeUpdate();
    }

    @Override
    public List<Order> findOrdersByUserId(long userId, int offset, int pageSize) {
        TypedQuery<Order> orderQuery = entityManager.createNamedQuery(Order.QueryNames.FIND_ALL_BY_USER_ID, Order.class);
        orderQuery.setParameter("userId", userId);
        orderQuery.setFirstResult(offset);
        orderQuery.setMaxResults(pageSize);

        return orderQuery.getResultList();
    }
}
