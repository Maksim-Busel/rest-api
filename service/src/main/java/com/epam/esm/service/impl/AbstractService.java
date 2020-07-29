package com.epam.esm.service.impl;

import com.epam.esm.dao.api.Dao;
import com.epam.esm.entity.Identifable;
import com.epam.esm.exception.FailedOperationException;
import com.epam.esm.exception.ThereIsNoSuchEntityException;
import com.epam.esm.service.api.Service;
import com.epam.esm.util.OffsetCalculator;
import com.epam.esm.validator.Validator;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;

public class AbstractService<T extends Identifable> implements Service<T> {
    protected final Validator<T> validator;
    protected final Dao<T> dao;
    protected final OffsetCalculator offsetCalculator;

    public AbstractService(Validator<T> validator, Dao<T> dao, OffsetCalculator offsetCalculator) {
        this.validator = validator;
        this.dao = dao;
        this.offsetCalculator = offsetCalculator;
    }

    @Transactional
    public T add(T entity) {
        validator.validate(entity);

        return dao.create(entity);
    }

    @Override
    public T getById(long id, boolean exceptionIfNotFound) {
        validator.validateIdValue(id);

        try {
            return dao.findById(id);
        } catch (EmptyResultDataAccessException e) {
            if (exceptionIfNotFound) {
                throw new ThereIsNoSuchEntityException("Entity: " + id + " doesn't exist", e);
            }

            return null;
        }
    }

    @Override
    public T getById(long id) {
        return this.getById(id, true);
    }

    @Override
    @Transactional
    public void lock(long id) {
        validator.validateExistenceEntityById(id);
        int result = dao.lockById(id);

        if (result == 0) {
            throw new FailedOperationException("Failed to delete entity " + id);
        }
    }
}
