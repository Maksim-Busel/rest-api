package com.epam.esm.creator.impl;

import com.epam.esm.controller.OrderController;
import com.epam.esm.creator.LinksCreator;
import com.epam.esm.dto.OrderDto;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderLinksCreatorImpl implements LinksCreator<OrderDto> {
    private static final int DEFAULT_PAGE_NUMBER = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final String GET_ORDER = "getOrder";
    private static final String DELETE_ORDER = "deleteOrder";
    private static final String ALL_ORDERS = "allOrders";

    @Override
    public void createForSingleEntity(OrderDto orderDto) {
        long orderId = orderDto.getId();
        orderDto.add(
                linkTo(methodOn(OrderController.class).getById(orderId)).withRel(GET_ORDER),
                linkTo(methodOn(OrderController.class).deleteById(orderId)).withRel(DELETE_ORDER),
                linkTo(methodOn(OrderController.class).getAll(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE)).withRel(ALL_ORDERS)
        );
    }

    @Override
    public void createForListEntities(List<OrderDto> ordersDto) {
        for (OrderDto dto : ordersDto) {
            long orderId = dto.getId();
            dto.add(
                    linkTo(methodOn(OrderController.class).getById(orderId)).withRel(GET_ORDER),
                    linkTo(methodOn(OrderController.class).deleteById(orderId)).withRel(DELETE_ORDER)
            );
        }
    }

    @Override
    public List<Link> createByEntityId(long orderId) {
        List<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(OrderController.class).deleteById(orderId)).withSelfRel());
        links.add(linkTo(methodOn(OrderController.class).getById(orderId)).withRel(GET_ORDER));
        links.add(linkTo(methodOn(OrderController.class).getAll(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE)).withRel(ALL_ORDERS));

        return links;
    }
}
