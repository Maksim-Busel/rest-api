package com.epam.esm.dao.impl;

import com.epam.esm.dao.api.Dao;
import com.epam.esm.entity.Identifable;
import com.epam.esm.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

public abstract class AbstractDao<T extends Identifable> implements Dao<T>{
    private final EntityManager entityManager;

    public AbstractDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public T create(T entity) {
        entityManager.persist(entity);

        return entity;
    }
}
