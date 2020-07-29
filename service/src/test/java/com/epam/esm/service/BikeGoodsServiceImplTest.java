package com.epam.esm.service;

import com.epam.esm.dao.api.BikeGoodsDao;
import com.epam.esm.entity.BikeGoods;
import com.epam.esm.entity.BikeGoodsType;
import com.epam.esm.exception.ThereIsNoSuchBikeGoodsException;
import com.epam.esm.service.impl.BikeGoodsServiceImpl;
import com.epam.esm.util.OffsetCalculator;
import com.epam.esm.validator.BikeGoodsValidator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.EmptyResultDataAccessException;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BikeGoodsServiceImplTest {
    private static final String GOODS_NAME_FIELD = "goods name";

    @Mock
    private BikeGoodsDao goodsDao;
    @Mock
    private BikeGoodsValidator validator;
    @Mock
    private OffsetCalculator calculator;

    @InjectMocks
    private BikeGoodsServiceImpl bikeGoodsService;

    private BikeGoods bikeGoods;

    @Before
    public void setUp() {
        bikeGoods = new BikeGoods();
        bikeGoods.setId(2);
        bikeGoods.setPrice(new BigDecimal("213.431"));
        bikeGoods.setName("Aist");
        bikeGoods.setGoodsType(BikeGoodsType.BIKE);
    }

    @Test
    public void getAllShouldExecuteFindAllAndValidatePageParametersOneTime() {
        int pageNumber = 1;
        int pageSize = 10;
        when(calculator.calculate(pageNumber, pageSize)).thenReturn(0);
        int expectedOffset = 0;

        bikeGoodsService.getAll(pageNumber, pageSize);

        verify(validator, times(1)).validatePageParameters(pageNumber, pageSize);
        verify(calculator, times(1)).calculate(pageNumber, pageSize);
        verify(goodsDao, times(1)).findAll(expectedOffset, pageSize);
    }

    @Test
    public void getByNameWhenExistEntityWithThisNameShouldExecuteValidateStringAndFindByNameOneTime() {
        String goodsName = "name";
        boolean exceptionIfNotFound = true;

        bikeGoodsService.getByName(goodsName, exceptionIfNotFound);

        verify(validator, times(1)).validateString(goodsName, GOODS_NAME_FIELD);
        verify(goodsDao, times(1)).findByName(goodsName);
    }

    @Test(expected = ThereIsNoSuchBikeGoodsException.class)
    public void getByNameWhenNoExistEntityWithThisNameAndExceptionIfNotFoundTrueShouldThrowThereIsNoSuchBikeGoodsException() {
        String goodsName = "name";
        boolean exceptionIfNotFound = true;
        doThrow(EmptyResultDataAccessException.class).when(goodsDao).findByName(goodsName);

        bikeGoodsService.getByName(goodsName, exceptionIfNotFound);

        verify(validator, times(1)).validateString(goodsName, GOODS_NAME_FIELD);
        verify(goodsDao, times(1)).findByName(goodsName);
    }

    @Test
    public void getByNameWhenNoExistEntityWithThisNameAndExceptionIfNotFoundFalseShouldReturnNull() {
        String goodsName = "name";
        boolean exceptionIfNotFound = false;
        doThrow(EmptyResultDataAccessException.class).when(goodsDao).findByName(goodsName);

        BikeGoods nullGoods = bikeGoodsService.getByName(goodsName, exceptionIfNotFound);

        Assert.assertNull(nullGoods);
        verify(validator, times(1)).validateString(goodsName, GOODS_NAME_FIELD);
        verify(goodsDao, times(1)).findByName(goodsName);
    }

    @Test
    public void editWhenUpdatedGoodsNameNoEqualsGoodsNameFromDbShouldExecuteValidatePriceAndValidateBikeGoodsTypeOneTime() {
        long goodsId = bikeGoods.getId();
        BikeGoods bikeGoodsFromDb = new BikeGoods();
        bikeGoodsFromDb.setId(goodsId);
        bikeGoodsFromDb.setName("Aist");
        bikeGoodsFromDb.setGoodsType(BikeGoodsType.CLOTHES);
        bikeGoodsFromDb.setPrice(new BigDecimal("223.431"));
        when(goodsDao.findById(goodsId)).thenReturn(bikeGoodsFromDb);

        bikeGoodsService.edit(bikeGoods);

        verify(validator, times(1)).validateExistenceEntityById(goodsId);
        verify(goodsDao, times(1)).findById(goodsId);
        verify(validator, times(1)).validatePrice(bikeGoods.getPrice());
        verify(validator, times(1)).validateBikeGoodsType(bikeGoods.getGoodsType());
    }

    @Test
    public void editWhenUpdatedGoodsDifferGoodsFromDbShouldChangeGoodsFromDbThatTheyWillBeEquals() {
        BikeGoods bikeGoodsFromDb = new BikeGoods();
        bikeGoodsFromDb.setId(2);
        bikeGoodsFromDb.setName("Aist");
        bikeGoodsFromDb.setGoodsType(BikeGoodsType.CLOTHES);
        bikeGoodsFromDb.setPrice(new BigDecimal("223.431"));

        BikeGoods expectedBikeGoods = new BikeGoods();
        expectedBikeGoods.setId(2);
        expectedBikeGoods.setName("Aist");
        expectedBikeGoods.setGoodsType(BikeGoodsType.BIKE);
        expectedBikeGoods.setPrice(new BigDecimal("213.431"));

        long goodsId = bikeGoods.getId();
        when(goodsDao.findById(goodsId)).thenReturn(bikeGoodsFromDb);

        BikeGoods result = bikeGoodsService.edit(bikeGoods);

        Assert.assertEquals(expectedBikeGoods, result);
        verify(validator, times(1)).validateExistenceEntityById(goodsId);
        verify(goodsDao, times(1)).findById(goodsId);
        verify(validator, times(1)).validatePrice(bikeGoods.getPrice());
        verify(validator, times(1)).validateBikeGoodsType(bikeGoods.getGoodsType());
    }

    @Test
    public void editWhenUpdatedGoodsNameEqualsGoodsNameFromDbShouldExecuteValidateOneTime() {
        BikeGoods bikeGoodsFromDb = new BikeGoods();
        bikeGoodsFromDb.setId(2);
        bikeGoodsFromDb.setName("Giant");
        bikeGoodsFromDb.setGoodsType(BikeGoodsType.CLOTHES);
        bikeGoodsFromDb.setPrice(new BigDecimal("223.431"));

        long goodsId = bikeGoods.getId();
        when(goodsDao.findById(goodsId)).thenReturn(bikeGoodsFromDb);

       bikeGoodsService.edit(bikeGoods);

        verify(validator, times(1)).validateExistenceEntityById(goodsId);
        verify(goodsDao, times(1)).findById(goodsId);
        verify(validator, times(1)).validate(bikeGoods);
    }
}