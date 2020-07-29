package com.epam.esm.validator.impl;

import com.epam.esm.entity.BikeGoods;
import com.epam.esm.entity.BikeGoodsType;
import com.epam.esm.exception.BikeGoodsParametersException;
import com.epam.esm.exception.ParameterException;
import com.epam.esm.exception.PriceException;
import com.epam.esm.service.api.BikeGoodsService;
import com.epam.esm.validator.BikeGoodsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BikeGoodsValidatorImpl extends AbstractValidator<BikeGoods> implements BikeGoodsValidator {
    private final BikeGoodsService bikeGoodsService;

    private static final String NAME = "name";
    private static final String MAX_PRICE = "5000";

    @Autowired
    public BikeGoodsValidatorImpl(@Lazy BikeGoodsService bikeGoodsService) {
        super(bikeGoodsService);
        this.bikeGoodsService=bikeGoodsService;
    }

    @Override
    public void validate(BikeGoods goods) {
        String name = goods.getName();
        BigDecimal price = goods.getPrice();
        BikeGoodsType type = goods.getGoodsType();

        validateString(name, NAME, false);
        validateExistenceBikeGoodsByName(name);
        validatePrice(price);
        validateBikeGoodsType(type);
    }

    @Override
    public void validateExistenceBikeGoodsByName(String goodsName) {
        BikeGoods goods = bikeGoodsService.getByName(goodsName.trim(), false);

        if (goods != null) {
            throw new ParameterException("Goods with: " + goodsName + " name already exists");
        }
    }

    @Override
    public void validatePrice(BigDecimal price) {
        BigDecimal maxPrice = new BigDecimal(MAX_PRICE);
        BigDecimal minPrice = BigDecimal.ZERO;

        if (price == null || price.compareTo(minPrice) < 0 || price.compareTo(maxPrice) > 0) {
            throw new PriceException("The price you entered is incorrect.");
        }
    }

    @Override
    public void validateBikeGoodsType(BikeGoodsType type) {
        if (type == null) {
            throw new BikeGoodsParametersException("You don't entered the goods type parameter");
        }
    }
}
