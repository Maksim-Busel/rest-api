package com.epam.esm.validator;

import com.epam.esm.entity.Order;

import java.math.BigDecimal;

public interface OrderValidator extends Validator<Order>{
    void validatePrice(BigDecimal price);
}
