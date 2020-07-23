package com.epam.esm.validator.impl;

import com.epam.esm.entity.Identifable;
import com.epam.esm.exception.ParameterException;
import com.epam.esm.service.api.Service;
import com.epam.esm.validator.Validator;

import static org.apache.commons.lang3.StringUtils.isBlank;

public abstract class AbstractValidatorImpl<T extends Identifable> implements Validator<T> {
    protected final Service<T> entityService;

    private static final int PAGE_NUMBER_MIN = 1;
    private static final int PAGE_NUMBER_MAX = 10_000_000;
    private static final int PAGE_SIZE_MIN = 1;
    private static final int PAGE_SIZE_MAX = 100;
    private static final String PAGE_SIZE_NAME = "Page size";
    private static final String PAGE_NUMBER_NAME = "Page number";

    protected AbstractValidatorImpl(Service<T> entityService) {
        this.entityService = entityService;
    }

    @Override
    public void validateExistenceEntityById(long id) {
        validateIdValue(id);
        checkExistenceEntityById(id);
    }

    private void checkExistenceEntityById(long entityId) {
        entityService.getById(entityId, true);
    }

    @Override
    public void validateString(String parameter, String parameterName) {
        validateString(parameter, parameterName, false);
    }

    @Override
    public void validateString(String parameter, String parameterName, boolean emptyValueAllowed) {
        if (isBlank(parameter) && !emptyValueAllowed) {
            throw new ParameterException("The " + parameterName + " you entered is incorrect.");
        }
    }

    @Override
    public void validatePageParameters(int pageNumber, int pageSize) {
        validatePageParameter(pageNumber, PAGE_NUMBER_NAME, PAGE_NUMBER_MIN, PAGE_NUMBER_MAX);
        validatePageParameter(pageSize, PAGE_SIZE_NAME, PAGE_SIZE_MIN, PAGE_SIZE_MAX);
    }

    private void validatePageParameter(int pageNumber, String parameterName, int minValue, int maxValue) {
        if (pageNumber < minValue || pageNumber > maxValue) {
            throw new ParameterException(parameterName + " must be in the range " + minValue + " and " + maxValue);
        }
    }

    @Override
    public void validateIdValue(long id) {
        if (id < 1) {
            throw new ParameterException("id cannot be 0 or a negative number");
        }
    }
}
