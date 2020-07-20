package com.epam.esm.mapper.impl;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Order;
import com.epam.esm.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapperImpl implements Mapper<Order, OrderDto> {
    private final ModelMapper mapper;

    public OrderMapperImpl(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public OrderDto convertToDto(Order order) {
        OrderDto orderDto = mapper.map(order, OrderDto.class);

        return orderDto;
    }

    @Override
    public Order convertToEntity(OrderDto orderDto) {
        Order order = mapper.map(orderDto, Order.class);

        return order;
    }

    @Override
    public List<OrderDto> convertAllToDto(List<Order> orders) {
        return orders.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
