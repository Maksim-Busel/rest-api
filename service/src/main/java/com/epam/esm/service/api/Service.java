package com.epam.esm.service.api;

import com.epam.esm.entity.Identifable;

public interface Service <T extends Identifable>{

    T getById(long id);

    T getById(long id, boolean exceptionIfNotFound);

    void lock(long id);
}
