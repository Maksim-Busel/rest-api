package com.epam.esm.service;

import com.epam.esm.dao.api.OrderDao;
import com.epam.esm.entity.Order;
import com.epam.esm.exception.IncorrectDataException;
import com.epam.esm.service.api.CertificateService;
import com.epam.esm.service.api.OrderService;
import com.epam.esm.service.impl.OrderServiceImpl;
import com.epam.esm.util.OffsetCalculator;
import com.epam.esm.validator.OrderValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceImplTest {
    @Mock
    private OrderValidator orderValidator;
    @Mock
    private CertificateService certificateService;
    @Mock
    private OrderDao orderDao;
    @Mock
    private OffsetCalculator calculator;
    private Order order = mock(Order.class);

    private OrderService orderService;

    @Before
    public void setUp() {
        orderService = new OrderServiceImpl(certificateService, calculator, orderValidator, orderDao);
    }

    @Test
    public void addWhenThreeCertificatesIdShouldExecuteGetByIdThreeTimes() {
        int firstCertificateId = 1;
        int secondCertificateId = 2;
        int thirdCertificateId = 3;

        List<Integer> certificatesId = new ArrayList<>();
        certificatesId.add(firstCertificateId);
        certificatesId.add(secondCertificateId);
        certificatesId.add(thirdCertificateId);

        orderService.add(order, certificatesId);

        verify(certificateService, times(1)).getCostCertificates(certificatesId);
        verify(orderValidator, times(1)).validate(order);
        verify(certificateService, times(1)).getById(firstCertificateId);
        verify(certificateService, times(1)).getById(secondCertificateId);
        verify(certificateService, times(1)).getById(thirdCertificateId);
    }

    @Test(expected = IncorrectDataException.class)
    public void addWhenZeroSizeCertificatesIdShouldThrowIncorrectDataException() {
        List<Integer> certificatesId = new ArrayList<>();

        orderService.add(order, certificatesId);
    }

    @Test
    public void getAllShouldExecuteFindAllAndValidatePageParametersOneTime() {
        int pageNumber = 1;
        int pageSize = 10;
        when(calculator.calculate(pageNumber, pageSize)).thenReturn(0);
        int expectedOffset = 0;

        orderService.getAll(pageNumber, pageSize);

        verify(orderValidator, times(1)).validatePageParameters(pageNumber, pageSize);
        verify(calculator, times(1)).calculate(pageNumber, pageSize);
        verify(orderDao, times(1)).findAll(expectedOffset, pageSize);
    }

    @Test
    public void getUserOrdersByUserId() {
        int userId = 1;
        int pageNumber = 1;
        int pageSize = 10;
        when(calculator.calculate(pageNumber, pageSize)).thenReturn(0);
        int expectedOffset = 0;

        orderService.getUserOrdersByUserId(userId, pageNumber, pageSize);

        verify(orderValidator, times(1)).validatePageParameters(pageNumber, pageSize);
        verify(calculator, times(1)).calculate(pageNumber, pageSize);
        verify(orderDao, times(1)).findOrdersByUserId(userId, expectedOffset, pageSize);
    }
}
