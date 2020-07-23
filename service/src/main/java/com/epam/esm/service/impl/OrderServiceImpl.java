package com.epam.esm.service.impl;

import com.epam.esm.dao.api.OrderDao;
import com.epam.esm.entity.BikeGoods;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.IncorrectDataException;
import com.epam.esm.service.api.CertificateService;
import com.epam.esm.service.api.OrderService;
import com.epam.esm.util.OffsetCalculator;
import com.epam.esm.validator.OrderValidator;
import com.epam.esm.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class OrderServiceImpl extends AbstractService<Order> implements OrderService {
    private final OrderValidator orderValidator;
    private final CertificateService certificateService;
    private final OrderDao orderDao;

    @Autowired
    public OrderServiceImpl(CertificateService certificateService, OffsetCalculator offsetCalculator,
                            OrderValidator orderValidator, OrderDao orderDao) {
        super(orderValidator, orderDao, offsetCalculator);
        this.certificateService = certificateService;
        this.orderValidator = orderValidator;
        this.orderDao = orderDao;
    }

    @Override
    @Transactional
    public Order add(Order order, List<Integer> certificatesId) {
        if (certificatesId.size() == 0) {
            throw new IncorrectDataException("You don't specify any certificate id");
        }

        BigDecimal priceTotal = certificateService.getCostCertificates(certificatesId);
        order.setPriceTotal(priceTotal);
        orderValidator.validate(order);
        order.setOrderDate(LocalDate.now());

        for (long concreteCertificateId : certificatesId) {
            Certificate certificate = certificateService.getById(concreteCertificateId);
            order.addCertificate(certificate);
        }

        return dao.create(order);
    }

    @Override
    public List<Order> getAll(int pageNumber, int pageSize) {
        validator.validatePageParameters(pageNumber, pageSize);
        int offset = offsetCalculator.calculate(pageNumber, pageSize);

        return orderDao.findAll(offset, pageSize);
    }

    @Override
    public List<Order> getUserOrdersByUserId(long userId, int pageNumber, int pageSize) {
        validator.validatePageParameters(pageNumber, pageSize);
        int offset = offsetCalculator.calculate(pageNumber, pageSize);

        return orderDao.findOrdersByUserId(userId, offset, pageSize);
    }
}
