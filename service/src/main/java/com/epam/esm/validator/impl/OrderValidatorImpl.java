package com.epam.esm.validator.impl;

import com.epam.esm.entity.Order;
import com.epam.esm.exception.PriceException;
import com.epam.esm.service.api.OrderService;
import com.epam.esm.validator.OrderValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OrderValidatorImpl extends AbstractValidatorImpl<Order> implements OrderValidator {
    private static final String MAX_PRICE = "50000";

    @Autowired
    public OrderValidatorImpl(@Lazy OrderService orderService) {
        super(orderService);
    }

    @Override
    public void validate(Order order) {
        long userId = order.getUser().getId();
        BigDecimal priceTotal = order.getPriceTotal();

        validateIdValue(userId);
        validatePrice(priceTotal);
    }

    public void validatePrice(BigDecimal price) {
        BigDecimal maxPrice = new BigDecimal(MAX_PRICE);
        BigDecimal minPrice = BigDecimal.ZERO;

        if (price == null || price.compareTo(minPrice) < 0 || price.compareTo(maxPrice) > 0) {
            throw new PriceException("The price you entered is incorrect.");
        }
    }
}
