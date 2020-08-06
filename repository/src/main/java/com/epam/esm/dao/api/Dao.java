package com.epam.esm.dao.api;

import com.epam.esm.entity.Identifable;

public interface Dao <T extends Identifable> {

    T create(T entity);

    T findById(long id);

    int lockById(long id);

}
