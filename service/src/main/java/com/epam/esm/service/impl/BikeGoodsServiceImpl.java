package com.epam.esm.service.impl;

import com.epam.esm.dao.api.BikeGoodsDao;
import com.epam.esm.entity.BikeGoods;
import com.epam.esm.entity.BikeGoodsType;
import com.epam.esm.exception.ThereIsNoSuchBikeGoodsException;
import com.epam.esm.service.api.BikeGoodsService;
import com.epam.esm.util.OffsetCalculator;
import com.epam.esm.validator.BikeGoodsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BikeGoodsServiceImpl extends AbstractService<BikeGoods> implements BikeGoodsService {
    private final BikeGoodsDao bikeGoodsDao;
    private final BikeGoodsValidator bikeGoodsValidator;

    private static final String GOODS_NAME_FIELD = "goods name";

    @Autowired
    public BikeGoodsServiceImpl(BikeGoodsDao bikeGoodsDao, OffsetCalculator offsetCalculator,
                                BikeGoodsValidator bikeGoodsValidator) {
        super(bikeGoodsValidator, bikeGoodsDao, offsetCalculator);
        this.bikeGoodsDao = bikeGoodsDao;
        this.bikeGoodsValidator = bikeGoodsValidator;
    }

    @Override
    public BikeGoods getByName(String goodsName, boolean exceptionIfNotFound) {
        bikeGoodsValidator.validateString(goodsName, GOODS_NAME_FIELD);

        try {
            return bikeGoodsDao.findByName(goodsName);
        } catch (EmptyResultDataAccessException e) {
            if (exceptionIfNotFound) {
                throw new ThereIsNoSuchBikeGoodsException("Goods: " + goodsName + " not found", e);
            }

            return null;
        }
    }

    @Override
    public BikeGoods getByName(String goodsName) {
        return this.getByName(goodsName, true);
    }

    @Override
    @Transactional
    public BikeGoods edit(BikeGoods updatedBikeGoods) {
        long goodsId = updatedBikeGoods.getId();
        bikeGoodsValidator.validateExistenceEntityById(goodsId);
        BikeGoods bikeGoodsFromDb = dao.findById(goodsId);

        String updatedGoodsName = updatedBikeGoods.getName().trim();
        BigDecimal updatedPrice = updatedBikeGoods.getPrice();
        BikeGoodsType updatedGoodsType = updatedBikeGoods.getGoodsType();

        if (bikeGoodsFromDb.getName().trim().equals(updatedGoodsName)) {
            bikeGoodsValidator.validatePrice(updatedPrice);
            bikeGoodsValidator.validateBikeGoodsType(updatedGoodsType);
        } else {
            bikeGoodsValidator.validate(updatedBikeGoods);
            bikeGoodsFromDb.setName(updatedGoodsName);
        }

        bikeGoodsFromDb.setPrice(updatedPrice);
        bikeGoodsFromDb.setGoodsType(updatedGoodsType);

        return bikeGoodsFromDb;
    }

    @Override
    public List<BikeGoods> getAll(int pageNumber, int pageSize) {
        bikeGoodsValidator.validatePageParameters(pageNumber, pageSize);
        int offset = offsetCalculator.calculate(pageNumber, pageSize);

        return bikeGoodsDao.findAll(offset, pageSize);
    }
}
