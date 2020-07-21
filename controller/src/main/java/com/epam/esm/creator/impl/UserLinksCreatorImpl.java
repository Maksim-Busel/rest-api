package com.epam.esm.creator.impl;

import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.UserController;
import com.epam.esm.creator.LinksCreator;
import com.epam.esm.creator.UserLinksCreator;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserLinksCreatorImpl implements UserLinksCreator {
    private static final int DEFAULT_PAGE_NUMBER = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final String GET_USER = "getUser";
    private static final String DELETE_USER = "deleteUser";
    private static final String ALL_USERS = "allUsers";
    private static final String USER_ORDERS = "userOrders";
    private static final String REGISTRATION = "registration";
    private static final String GET_ORDER = "getOrder";
    private static final String DELETE_ORDER = "deleteOrder";

    @Override
    public void createForSingleEntity(UserDto userDto) {
        long userId = userDto.getId();
        userDto.add(
                linkTo(methodOn(UserController.class).getById(userId)).withRel(GET_USER),
                linkTo(methodOn(UserController.class).deleteById(userId)).withRel(DELETE_USER),
                linkTo(methodOn(UserController.class).getAll(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE)).withRel(ALL_USERS),
                linkTo(methodOn(UserController.class).register(new UserDto())).withRel(REGISTRATION),
                linkTo(methodOn(UserController.class).getUserOrders(userId, DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE)).withRel(USER_ORDERS)
        );
    }

    @Override
    public void createForListEntities(List<UserDto> usersDto) {
        for (UserDto dto : usersDto) {
            dto.setPassword(null);
            long userId = dto.getId();
            dto.add(
                    linkTo(methodOn(UserController.class).getById(userId)).withRel(GET_USER),
                    linkTo(methodOn(UserController.class).deleteById(userId)).withRel(DELETE_USER),
                    linkTo(methodOn(UserController.class).getUserOrders(userId, DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE)).withRel(USER_ORDERS)
            );
        }
    }

    @Override
    public List<Link> createByEntityId(long userId) {
        List<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(UserController.class).deleteById(userId)).withSelfRel());
        links.add(linkTo(methodOn(UserController.class).getById(userId)).withRel(GET_USER));
        links.add(linkTo(methodOn(UserController.class).register(new UserDto())).withRel(REGISTRATION));
        links.add(linkTo(methodOn(UserController.class).getAll(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE)).withRel(ALL_USERS));
        links.add(linkTo(methodOn(UserController.class).getUserOrders(userId, DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE)).withRel(USER_ORDERS));

        return links;
    }

    @Override
    public void createForUserOrders(List<OrderDto> ordersDto, long userId) {
        for (OrderDto dto : ordersDto) {
            long orderId = dto.getId();
            dto.add(
                    linkTo(methodOn(OrderController.class).getById(orderId)).withRel(GET_ORDER),
                    linkTo(methodOn(OrderController.class).deleteById(orderId)).withRel(DELETE_ORDER),
                    linkTo(methodOn(UserController.class).getById(userId)).withRel(GET_USER),
                    linkTo(methodOn(UserController.class).deleteById(userId)).withRel(DELETE_USER),
                    linkTo(methodOn(UserController.class).getUserOrders(userId, DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE)).withSelfRel()
            );
        }
    }
}
