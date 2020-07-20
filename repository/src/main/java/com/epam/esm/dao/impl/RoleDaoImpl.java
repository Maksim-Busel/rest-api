package com.epam.esm.dao.impl;

import com.epam.esm.dao.api.RoleDao;
import com.epam.esm.entity.Role;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class RoleDaoImpl implements RoleDao {
    @PersistenceContext
    private final EntityManager entityManager;

    private static final String FIND_BY_USERNAME = "SELECT roles.id, roles.name FROM roles WHERE name=:roleName";

    @Autowired
    public RoleDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Role findByRoleName(String roleName) {
        Session currentSession = entityManager.unwrap(Session.class);

        Query<Role> userQuery = currentSession.createNativeQuery(FIND_BY_USERNAME, Role.class);
        userQuery.setParameter("roleName", roleName);

        return userQuery.getSingleResult();
    }
}
