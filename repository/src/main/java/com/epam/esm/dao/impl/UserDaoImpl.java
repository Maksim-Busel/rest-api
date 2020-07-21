package com.epam.esm.dao.impl;

import com.epam.esm.dao.api.UserDao;
import com.epam.esm.entity.User;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {
    @PersistenceContext
    private final EntityManager entityManager;

    public static final String FIND_USER_WITH_LARGEST_AMOUNT_ORDERS = "SELECT DISTINCT u.username, u.password, " +
            "u.id, SUM(price_total) FROM users u JOIN orders o ON u.id=user_id " +
            "GROUP BY u.username, u.id, u.username HAVING u.id IN " +
                "(SELECT user_id FROM " +
                    "(SELECT user_id, SUM(price_total) FROM orders JOIN users ON users.id=user_id" +
                     " GROUP BY user_id, users.lock, orders.lock  HAVING users.lock=:userLock AND orders.lock=:orderLock" +
                     " ORDER BY sum DESC LIMIT 1) AS user_id_largest_sum_orders)";

    @Autowired
    public UserDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public User create(User user) {
        entityManager.persist(user);

        return user;
    }

    @Override
    public User findById(long id) {
        TypedQuery<User> userQuery = entityManager.createNamedQuery("User.findById", User.class);
        userQuery.setParameter("userId", id);

        return userQuery.getSingleResult();
    }

    @Override
    public List<User> findAll(int offset, int pageSize) {
        TypedQuery<User> userQuery = entityManager.createNamedQuery("User.findAll", User.class);
        userQuery.setFirstResult(offset);
        userQuery.setMaxResults(pageSize);

        return userQuery.getResultList();
    }

    @Override
    public int lockById(long id) {
        Query userQuery = entityManager.createNamedQuery("User.lockById");
        userQuery.setParameter("userId", id);

        return userQuery.executeUpdate();
    }

    @Override
    public User findByUsername(String username) {
        TypedQuery<User> userQuery = entityManager.createNamedQuery("User.findByUsername", User.class);
        userQuery.setParameter("username", username);

        return userQuery.getSingleResult();
    }

    @Override
    public User findUserWithLargestAmountOrders(boolean userLockAllowed, boolean orderLockAllowed) {
        Query userQuery = entityManager.createNativeQuery(FIND_USER_WITH_LARGEST_AMOUNT_ORDERS, User.class);
        userQuery.setParameter("userLock", userLockAllowed);
        userQuery.setParameter("orderLock", orderLockAllowed);

        return (User) userQuery.getSingleResult();
    }
}
