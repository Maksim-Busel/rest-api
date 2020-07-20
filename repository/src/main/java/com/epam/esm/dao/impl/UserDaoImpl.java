package com.epam.esm.dao.impl;

import com.epam.esm.dao.api.UserDao;
import com.epam.esm.entity.User;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {
    @PersistenceContext
    private final EntityManager entityManager;

    private static final String FIND_BY_ID = "SELECT * FROM users WHERE id=:userId AND lock=false";
    private static final String FIND_ALL = "SELECT * FROM users WHERE lock=false ORDER BY id";
    private static final String LOCK_BY_ID = "UPDATE users SET lock=true WHERE id=:userId";
    private static final String FIND_BY_USERNAME = "SELECT * FROM users WHERE username=:username AND lock=false";
    private static final String FIND_USER_WITH_LARGEST_AMOUNT_ORDERS = "SELECT DISTINCT users.username, users.password, " +
            "users.id, SUM(price_total) FROM users JOIN orders ON users.id=user_id " +
            "GROUP BY users.username, users.id, users.username HAVING users.id IN " +
                "(SELECT user_id FROM " +
                    "(SELECT user_id, SUM(price_total) FROM orders JOIN users ON users.id=user_id" +
                    " GROUP BY user_id, users.lock, orders.lock  HAVING users.lock=:userLock AND orders.lock=:orderLock" +
                    " ORDER BY sum DESC LIMIT 1) AS user_id_largest_sum_orders);";

    public UserDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public User create(User user) {
        Session currentSession = entityManager.unwrap(Session.class);
        currentSession.save(user);
        entityManager.flush();

        return user;
    }

    @Override
    public User findById(long id) {
        Session currentSession = entityManager.unwrap(Session.class);

        Query<User> userQuery = currentSession.createNativeQuery(FIND_BY_ID, User.class);
        userQuery.setParameter("userId", id);

        return userQuery.getSingleResult();
    }

    @Override
    public List<User> findAll(int offset, int pageSize) {
        Session currentSession = entityManager.unwrap(Session.class);

        Query<User> userQuery = currentSession.createNativeQuery(FIND_ALL, User.class);
        userQuery.setFirstResult(offset);
        userQuery.setMaxResults(pageSize);

        return userQuery.getResultList();
    }

    @Override
    public int lockById(long id) {
        Session currentSession = entityManager.unwrap(Session.class);

        Query userQuery = currentSession.createNativeQuery(LOCK_BY_ID);
        userQuery.setParameter("userId", id);

        return userQuery.executeUpdate();
    }

    @Override
    public User findByUsername(String username) {
        Session currentSession = entityManager.unwrap(Session.class);

        Query<User> userQuery = currentSession.createNativeQuery(FIND_BY_USERNAME, User.class);
        userQuery.setParameter("username", username);

        return userQuery.getSingleResult();
    }

    @Override
    public User findUserWithLargestAmountOrders(boolean userLockAllowed, boolean orderLockAllowed) {
        Session currentSession = entityManager.unwrap(Session.class);
        Query<User> userQuery = currentSession.createNativeQuery(FIND_USER_WITH_LARGEST_AMOUNT_ORDERS, User.class);
        userQuery.setParameter("userLock", userLockAllowed);
        userQuery.setParameter("orderLock", orderLockAllowed);

        return userQuery.getSingleResult();
    }
}
