package com.epam.esm.validator;

import com.epam.esm.entity.BikeGoods;
import com.epam.esm.entity.BikeGoodsType;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.PriceException;
import com.epam.esm.service.api.OrderService;
import com.epam.esm.validator.impl.BikeGoodsValidatorImpl;
import com.epam.esm.validator.impl.OrderValidatorImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class OrderValidatorImplTest {
    private OrderValidator orderValidator;
    @Mock
    private OrderService orderService;
    @Mock
    private Validator<User> userValidator;

    @Before
    public void setUp() {
        orderValidator = new OrderValidatorImpl(orderService, userValidator);
    }

    @Test(expected = PriceException.class)
    public void validatePriceWhenPriceLessZeroShouldThrowPriceLowerZeroException() {
        BigDecimal priceLessZero = new BigDecimal("-213.232");

        orderValidator.validatePrice(priceLessZero);
    }

    @Test(expected = PriceException.class)
    public void validatePriceWhenPriceMoreFiftyThousandShouldThrowPriceLowerZeroException() {
        BigDecimal priceLessZero = new BigDecimal("55000");

        orderValidator.validatePrice(priceLessZero);
    }

    @Test
    public void validatePriceWhenPriceCorrectShouldEndWithoutErrors() {
        BigDecimal validPrice = new BigDecimal("213.232");

        orderValidator.validatePrice(validPrice);
    }
}
