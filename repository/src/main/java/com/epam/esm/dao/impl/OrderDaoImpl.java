package com.epam.esm.dao.impl;

import com.epam.esm.dao.api.OrderDao;
import com.epam.esm.entity.Order;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class OrderDaoImpl implements OrderDao {
    @PersistenceContext
    private final EntityManager entityManager;

    public static final String ADD_CERTIFICATE_ORDERS = "INSERT INTO certificate_orders (certificate_id, order_id) " +
            "VALUES(:certificateId, :orderId)";

    @Autowired
    public OrderDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Order create(Order order) {
        entityManager.persist(order);
        return order;
    }

    @Override
    public Order findById(long id) {
        TypedQuery<Order> orderQuery = entityManager.createNamedQuery("Order.findById", Order.class);
        orderQuery.setParameter("orderId", id);

        return orderQuery.getSingleResult();
    }

    @Override
    public List<Order> findAll(int offset, int pageSize) {
        TypedQuery<Order> orderQuery = entityManager.createNamedQuery("Order.findAll", Order.class);
        orderQuery.setFirstResult(offset);
        orderQuery.setMaxResults(pageSize);

        return orderQuery.getResultList();
    }

    @Override
    public int lockById(long id) {
        Query orderQuery = entityManager.createNamedQuery("Order.lockById");
        orderQuery.setParameter("orderId", id);

        return orderQuery.executeUpdate();
    }

    @Override
    public int createCertificateOrders(long certificateId, long orderId){
        Query certificateOrdersQuery = entityManager.createNativeQuery(ADD_CERTIFICATE_ORDERS);
        certificateOrdersQuery.setParameter("certificateId", certificateId);
        certificateOrdersQuery.setParameter("orderId", orderId);

        return certificateOrdersQuery.executeUpdate();
    }

    @Override
    public List<Order> findOrdersByUserId(long userId, int offset, int pageSize) {
        TypedQuery<Order> orderQuery = entityManager.createNamedQuery("Order.findAllByUserId", Order.class);
        orderQuery.setParameter("userId", userId);
        orderQuery.setFirstResult(offset);
        orderQuery.setMaxResults(pageSize);

        return orderQuery.getResultList();
    }
}
