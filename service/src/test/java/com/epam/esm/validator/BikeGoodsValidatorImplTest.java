package com.epam.esm.validator;

import com.epam.esm.entity.BikeGoods;
import com.epam.esm.entity.BikeGoodsType;
import com.epam.esm.exception.BikeGoodsParametersException;
import com.epam.esm.exception.PriceException;
import com.epam.esm.service.api.BikeGoodsService;
import com.epam.esm.validator.impl.BikeGoodsValidatorImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BikeGoodsValidatorImplTest {
    private BikeGoodsValidator bikeGoodsValidator;
    private BikeGoods goods;
    @Mock
    private BikeGoodsService bikeGoodsService;

    @Before
    public void setUp() {
        bikeGoodsValidator = new BikeGoodsValidatorImpl(bikeGoodsService);
        goods = new BikeGoods();
        goods.setPrice(new BigDecimal("213.431"));
        goods.setName("Aist");
        goods.setGoodsType(BikeGoodsType.BIKE);
    }

    @Test(expected = PriceException.class)
    public void validatePriceWhenNoValidPriceShouldThrowPriceLowerZeroException() {
        BigDecimal noValidPrice = new BigDecimal("-213.232");

        bikeGoodsValidator.validatePrice(noValidPrice);
    }

    @Test(expected = PriceException.class)
    public void validatePriceWhenPriceMoreFiveThousandShouldThrowPriceLowerZeroException() {
        BigDecimal priceLessZero = new BigDecimal("5500");

        bikeGoodsValidator.validatePrice(priceLessZero);
    }

    @Test
    public void validatePriceWhenPriceCorrectShouldEndWithoutErrors() {
        BigDecimal validPrice = new BigDecimal("213.232");

        bikeGoodsValidator.validatePrice(validPrice);
    }

    @Test
    public void validateBikeGoodsTypeWhenGoodsTypeCorrectShouldEndWithoutErrors() {
        BikeGoodsType correctGoodsType = BikeGoodsType.BIKE;

        bikeGoodsValidator.validateBikeGoodsType(correctGoodsType);
    }

    @Test(expected = BikeGoodsParametersException.class)
    public void validateBikeGoodsTypeWhenGoodsTypeNullShouldThrowBikeGoodsParametersException() {
        BikeGoodsType goodsType = null;

        bikeGoodsValidator.validateBikeGoodsType(goodsType);
    }

    @Test
    public void validateExistenceBikeGoodsByNameWhenGoodsWithSuchNameNoExistShouldEndWithoutError() {
        String goodsName = goods.getName();
        boolean exceptionIfNotFound = false;
        when(bikeGoodsService.getByName(goodsName)).thenReturn(null);

        bikeGoodsValidator.validateExistenceBikeGoodsByName(goodsName);
        verify(bikeGoodsService, times(1)).getByName(goodsName, exceptionIfNotFound);
    }
}