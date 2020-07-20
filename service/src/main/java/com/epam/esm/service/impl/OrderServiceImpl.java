package com.epam.esm.service.impl;

import com.epam.esm.dao.api.OrderDao;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.FailedOperationException;
import com.epam.esm.exception.IncorrectDataException;
import com.epam.esm.exception.ThereIsNoSuchOrderException;
import com.epam.esm.service.api.CertificateService;
import com.epam.esm.service.api.OrderService;
import com.epam.esm.util.OffsetCalculator;
import com.epam.esm.validator.OrderValidator;
import com.epam.esm.validator.Validator;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderDao dao;
    private final OrderValidator orderValidator;
    private final Validator<User> userValidator;
    private final Validator<Certificate> certificateValidator;
    private final CertificateService certificateService;
    private final OffsetCalculator offsetCalculator;

    public OrderServiceImpl(CertificateService certificateService, OrderValidator orderValidator,
                            OrderDao dao, Validator<User> userValidator, Validator<Certificate> certificateValidator, OffsetCalculator offsetCalculator) {
        this.dao = dao;
        this.orderValidator = orderValidator;
        this.certificateService = certificateService;
        this.userValidator = userValidator;
        this.certificateValidator = certificateValidator;
        this.offsetCalculator = offsetCalculator;
    }

    @Override
    @Transactional
    public Order add(Order order, List<Integer> certificatesId) {
        if (certificatesId.size() == 0) {
            throw new IncorrectDataException("You don't specify any certificate id");
        }

        long userId = order.getUser().getId();
        userValidator.validateExcitingEntityById(userId);

        BigDecimal priceTotal = certificateService.getCostCertificates(certificatesId);
        orderValidator.validatePrice(priceTotal);

        order.setPriceTotal(priceTotal);
        order.setOrderDate(LocalDate.now());
        Order savedOrder = dao.create(order);

        long orderId = savedOrder.getId();
        for (long concreteCertificateId : certificatesId) {
            certificateValidator.validateExcitingEntityById(concreteCertificateId);

            dao.createCertificateOrders(concreteCertificateId, orderId);
        }

        return savedOrder;
    }

    @Override
    public Order getById(long id, boolean exceptionIfNotFound) {
        orderValidator.validateIdValue(id);

        try {
            return dao.findById(id);
        } catch (EmptyResultDataAccessException e) {

            if (exceptionIfNotFound) {
                throw new ThereIsNoSuchOrderException("Order: " + id + " doesn't exist", e);
            }
            return null;
        }
    }

    @Override
    public Order getById(long id) {
        return this.getById(id, true);
    }

    @Override
    public List<Order> getAll(int pageNumber, int pageSize, boolean exceptionIfNotFound) {
        orderValidator.validatePageParameters(pageNumber, pageSize);
        int offset = offsetCalculator.calculate(pageNumber, pageSize);

        List<Order> orders = dao.findAll(offset, pageSize);
        if (orders.size() == 0 && exceptionIfNotFound) {
            throw new ThereIsNoSuchOrderException("Not found any orders for your request");
        }

        return orders;
    }

    @Override
    public List<Order> getAll(int pageNumber, int pageSize) {
        return this.getAll(pageNumber, pageSize, true);
    }

    @Override
    @Transactional
    public void lock(long id) {
        orderValidator.validateExcitingEntityById(id);

        int result = dao.lockById(id);

        if (result == 0) {
            throw new FailedOperationException("Failed to delete order " + id);
        }
    }

    @Override
    public List<Order> getUserOrdersByUserId(long userId, int pageNumber, int pageSize) {
        orderValidator.validatePageParameters(pageNumber, pageSize);
        int offset = offsetCalculator.calculate(pageNumber, pageSize);

        List<Order> orders = dao.findOrdersByUserId(userId, offset, pageSize);
        if (orders.size() == 0) {
            throw new ThereIsNoSuchOrderException("Orders for user id: " + userId + " doesn't exist");
        }

        return orders;
    }
}
