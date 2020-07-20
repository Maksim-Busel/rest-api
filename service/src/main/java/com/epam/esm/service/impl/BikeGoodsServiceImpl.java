package com.epam.esm.service.impl;

import com.epam.esm.dao.api.BikeGoodsDao;
import com.epam.esm.entity.BikeGoods;
import com.epam.esm.exception.FailedOperationException;
import com.epam.esm.exception.IncorrectDataException;
import com.epam.esm.exception.ThereIsNoSuchBikeGoodsException;
import com.epam.esm.service.api.BikeGoodsService;
import com.epam.esm.util.OffsetCalculator;
import com.epam.esm.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BikeGoodsServiceImpl implements BikeGoodsService {
    private final BikeGoodsDao dao;
    private final Validator<BikeGoods> validator;
    private final OffsetCalculator offsetCalculator;

    @Autowired
    public BikeGoodsServiceImpl(BikeGoodsDao dao, Validator<BikeGoods> validator, OffsetCalculator offsetCalculator) {
        this.dao = dao;
        this.validator = validator;
        this.offsetCalculator = offsetCalculator;
    }

    @Override
    @Transactional
    public BikeGoods add(BikeGoods goods) {
        validator.validate(goods);

        try {
            return dao.create(goods);
        } catch (DataIntegrityViolationException e) {
            throw new IncorrectDataException("Goods with: " +goods.getName()+ " name already exists.");
        }
    }

    @Override
    public BikeGoods getById(long id, boolean exceptionIfNotFound) {
        validator.validateIdValue(id);

        try {
            return dao.findById(id);
        } catch (EmptyResultDataAccessException e) {
            if (exceptionIfNotFound) {
                throw new ThereIsNoSuchBikeGoodsException("Bike goods: " + id + " doesn't exist", e);
            }

            return null;
        }
    }

    @Override
    public BikeGoods getById(long id) {
        return this.getById(id, true);
    }

    @Override
    public List<BikeGoods> getAll(int pageNumber, int pageSize, boolean exceptionIfNotFound) {
        validator.validatePageParameters(pageNumber, pageSize);
        int offset = offsetCalculator.calculate(pageNumber, pageSize);

        List<BikeGoods> bikeGoods = dao.findAll(offset, pageSize);
        if (bikeGoods.size() == 0 && exceptionIfNotFound) {
            throw new ThereIsNoSuchBikeGoodsException("Not found any goods for your request");
        }

        return bikeGoods;
    }

    @Override
    public List<BikeGoods> getAll(int pageNumber, int pageSize) {
        return this.getAll(pageNumber, pageSize, true);
    }

    @Override
    @Transactional
    public void lock(long id) {
        validator.validateExcitingEntityById(id);
        int result = dao.lockById(id);

        if (result == 0) {
            throw new FailedOperationException("Failed to delete bike goods " + id);
        }
    }
}
