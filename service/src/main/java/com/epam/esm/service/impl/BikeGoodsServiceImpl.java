package com.epam.esm.service.impl;

import com.epam.esm.dao.api.BikeGoodsDao;
import com.epam.esm.entity.BikeGoods;
import com.epam.esm.exception.ThereIsNoSuchBikeGoodsException;
import com.epam.esm.service.api.BikeGoodsService;
import com.epam.esm.util.OffsetCalculator;
import com.epam.esm.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BikeGoodsServiceImpl extends AbstractService<BikeGoods> implements BikeGoodsService {
    private final BikeGoodsDao bikeGoodsDao;

    @Autowired
    public BikeGoodsServiceImpl(BikeGoodsDao bikeGoodsDao, Validator<BikeGoods> validator, OffsetCalculator offsetCalculator) {
        super(validator, bikeGoodsDao, offsetCalculator);
        this.bikeGoodsDao = bikeGoodsDao;
    }

    @Override
    public List<BikeGoods> getAll(int pageNumber, int pageSize) {
        validator.validatePageParameters(pageNumber, pageSize);
        int offset = offsetCalculator.calculate(pageNumber, pageSize);

        return bikeGoodsDao.findAll(offset, pageSize);
    }
}
