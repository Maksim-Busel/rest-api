package com.epam.esm.validator;

import com.epam.esm.entity.Identifable;

public interface Validator <T extends Identifable>{

    void validate(T entity);

    void validateIdValue(long id);

    void validateString(String parameter, String parameterName, boolean emptyValueAllowed);

    void validateString(String parameter, String parameterName);

    void validatePageParameters(int pageNumber, int pageSize);

    void validateExistenceEntityById(long entityId);

    boolean isAdmin();
}
