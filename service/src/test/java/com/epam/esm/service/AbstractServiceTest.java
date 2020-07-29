package com.epam.esm.service;

import com.epam.esm.dao.api.BikeGoodsDao;
import com.epam.esm.entity.BikeGoods;
import com.epam.esm.exception.FailedOperationException;
import com.epam.esm.exception.ThereIsNoSuchBikeGoodsException;
import com.epam.esm.exception.ThereIsNoSuchEntityException;
import com.epam.esm.service.impl.AbstractService;
import com.epam.esm.service.impl.BikeGoodsServiceImpl;
import com.epam.esm.util.OffsetCalculator;
import com.epam.esm.validator.BikeGoodsValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.EmptyResultDataAccessException;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AbstractServiceTest {
    @Mock
    private BikeGoodsValidator validator;
    @Mock
    private BikeGoodsDao dao;
    @Mock
    private OffsetCalculator calculator;
    @Mock
    private BikeGoods entity;

    private AbstractService abstractService;

    @Before
    public void setUp() {
        abstractService = new BikeGoodsServiceImpl(dao, calculator, validator);
    }

    @Test
    public void addWhenEntityCorrectShouldExecuteCreateAndValidateOneTime() {
        doNothing().when(validator).validate(entity);

        abstractService.add(entity);

        verify(validator, times(1)).validate(entity);
        verify(dao, times(1)).create(entity);
    }

    @Test
    public void getByIdWhenExistEntityWithThisIdShouldExecuteValidateIdAndFindByIdOneTime() {
        long id = 1;
        boolean exceptionIfNotFound = true;
        doNothing().when(validator).validateIdValue(id);

        abstractService.getById(id, exceptionIfNotFound);

        verify(validator, times(1)).validateIdValue(id);
        verify(dao, times(1)).findById(id);
    }

    @Test(expected = ThereIsNoSuchEntityException.class)
    public void getByIdWhenNoExistEntityWithThisIdShouldThrowThereIsNoSuchEntityException() {
        long id = 1;
        boolean exceptionIfNotFound = true;
        doNothing().when(validator).validateIdValue(id);
        doThrow(EmptyResultDataAccessException.class).when(dao).findById(id);

        abstractService.getById(id, exceptionIfNotFound);

        verify(validator, times(1)).validateIdValue(id);
        verify(dao, times(1)).findById(id);
    }

    @Test
    public void lockWhenExistBikeGoodsWithThisIdShouldExecuteValidateIdAndLockByIdOneTime() {
        long id = 1;
        doNothing().when(validator).validateExistenceEntityById(id);
        when(dao.lockById(id)).thenReturn(1);

        abstractService.lock(id);

        verify(validator, times(1)).validateExistenceEntityById(id);
        verify(dao, times(1)).lockById(id);
    }

    @Test(expected = FailedOperationException.class)
    public void lockWhenDaoReturnZeroShouldThrowFailedOperationException() {
        long id = 1;
        doNothing().when(validator).validateExistenceEntityById(id);
        when(dao.lockById(id)).thenReturn(0);

        abstractService.lock(id);

        verify(validator, times(1)).validateExistenceEntityById(id);
        verify(dao, times(1)).lockById(id);
    }
}
