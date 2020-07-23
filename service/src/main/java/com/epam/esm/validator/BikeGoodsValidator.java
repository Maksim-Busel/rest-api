package com.epam.esm.validator;

import com.epam.esm.entity.BikeGoods;
import com.epam.esm.entity.BikeGoodsType;

import java.math.BigDecimal;

public interface BikeGoodsValidator extends Validator<BikeGoods>{

    void validatePrice(BigDecimal price);

    void validateBikeGoodsType(BikeGoodsType type);
}
