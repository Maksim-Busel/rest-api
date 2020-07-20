package com.epam.esm.dao.impl;

import com.epam.esm.dao.api.OrderDao;
import com.epam.esm.entity.Order;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class OrderDaoImpl implements OrderDao {
    @PersistenceContext
    private final EntityManager entityManager;

    private static final String FIND_BY_ID = "SELECT * FROM orders WHERE id=:orderId AND lock=false";
    private static final String FIND_ALL = "SELECT * FROM orders WHERE lock=false ORDER BY id ";
    private static final String LOCK_BY_ID = "UPDATE orders SET lock=true WHERE id=:orderId";
    private static final String FIND_ALL_BY_USER_ID = "SELECT * FROM orders WHERE user_id=:userId AND lock=false ORDER BY id";
    private static final String ADD_CERTIFICATE_ORDERS = "INSERT INTO certificate_orders (certificate_id, order_id) " +
            "VALUES(:certificateId, :orderId)";

    @Autowired
    public OrderDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Order create(Order order) {
        Session currentSession = entityManager.unwrap(Session.class);
        currentSession.save(order);

        return order;
    }

    @Override
    public Order findById(long id) {
        Session currentSession = entityManager.unwrap(Session.class);

        Query<Order> orderQuery = currentSession.createNativeQuery(FIND_BY_ID, Order.class);
        orderQuery.setParameter("orderId", id);

        return orderQuery.getSingleResult();
    }

    @Override
    public List<Order> findAll(int offset, int pageSize) {
        Session currentSession = entityManager.unwrap(Session.class);

        Query<Order> orderQuery = currentSession.createNativeQuery(FIND_ALL, Order.class);
        orderQuery.setFirstResult(offset);
        orderQuery.setMaxResults(pageSize);

        return orderQuery.getResultList();
    }

    @Override
    public int lockById(long id) {
        Session currentSession = entityManager.unwrap(Session.class);

        Query orderQuery = currentSession.createNativeQuery(LOCK_BY_ID);
        orderQuery.setParameter("orderId", id);

        return orderQuery.executeUpdate();
    }

    @Override
    public int createCertificateOrders(long certificateId, long orderId){
        Session currentSession = entityManager.unwrap(Session.class);

        Query certificateOrdersQuery = currentSession.createNativeQuery(ADD_CERTIFICATE_ORDERS);
        certificateOrdersQuery.setParameter("certificateId", certificateId);
        certificateOrdersQuery.setParameter("orderId", orderId);

        return certificateOrdersQuery.executeUpdate();
    }

    @Override
    public List<Order> findOrdersByUserId(long userId, int offset, int pageSize) {
        Session currentSession = entityManager.unwrap(Session.class);

        Query<Order> orderQuery = currentSession.createNativeQuery(FIND_ALL_BY_USER_ID, Order.class);
        orderQuery.setParameter("userId", userId);
        orderQuery.setFirstResult(offset);
        orderQuery.setMaxResults(pageSize);

        return orderQuery.getResultList();
    }
}
