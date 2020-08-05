package com.epam.esm.creator.impl;

import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.UserController;
import com.epam.esm.creator.UserLinksCreator;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import com.epam.esm.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final Validator<User> validator;

    @Autowired
    public UserLinksCreatorImpl(Validator<User> validator) {
        this.validator = validator;
    }


    @Override
    public void createForSingleEntity(UserDto userDto) {
        long userId = userDto.getId();

        if (validator.isAdmin()) {
            userDto.add(linkTo(methodOn(UserController.class).deleteById(userId)).withRel(DELETE_USER));
        }

        userDto.add(
                linkTo(methodOn(UserController.class).getById(userId)).withRel(GET_USER),
                linkTo(methodOn(UserController.class).getAll(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE)).withRel(ALL_USERS),
                linkTo(methodOn(UserController.class).register(new UserDto())).withRel(REGISTRATION),
                linkTo(methodOn(UserController.class).getUserOrders(userId, DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE)).withRel(USER_ORDERS)
        );
    }

    @Override
    public void createForListEntities(List<UserDto> usersDto) {
        for (UserDto dto : usersDto) {
            long userId = dto.getId();

            if (validator.isAdmin()) {
                dto.add(linkTo(methodOn(UserController.class).deleteById(userId)).withRel(DELETE_USER));
            }

            dto.add(
                    linkTo(methodOn(UserController.class).getById(userId)).withRel(GET_USER),
                    linkTo(methodOn(UserController.class).getUserOrders(userId, DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE)).withRel(USER_ORDERS));
        }
    }

    @Override
    public List<Link> createByEntityId(long userId) {
        List<Link> links = new ArrayList<>();

        if (validator.isAdmin()) {
            links.add(linkTo(methodOn(UserController.class).deleteById(userId)).withSelfRel());
        }
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

            if (validator.isAdmin()) {
                dto.add(
                        linkTo(methodOn(OrderController.class).deleteById(orderId)).withRel(DELETE_ORDER),
                        linkTo(methodOn(UserController.class).deleteById(userId)).withRel(DELETE_USER)
                        );
            }

            dto.add(
                    linkTo(methodOn(OrderController.class).getById(orderId)).withRel(GET_ORDER),
                    linkTo(methodOn(UserController.class).getById(userId)).withRel(GET_USER),
                    linkTo(methodOn(UserController.class).getUserOrders(userId, DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE)).withSelfRel());
        }
    }
}
