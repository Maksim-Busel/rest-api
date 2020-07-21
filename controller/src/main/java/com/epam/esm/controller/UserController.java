package com.epam.esm.controller;

import com.epam.esm.creator.UserLinksCreator;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.mapper.Mapper;
import com.epam.esm.service.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    private final UserService service;
    private final Mapper<User, UserDto> userMapper;
    private final Mapper<Order, OrderDto> orderMapper;
    private final UserLinksCreator linksCreator;

    @Autowired
    public UserController(UserService service, Mapper<User, UserDto> userMapper, Mapper<Order, OrderDto> orderMapper, UserLinksCreator linksCreator) {
        this.service = service;
        this.userMapper = userMapper;
        this.orderMapper = orderMapper;
        this.linksCreator = linksCreator;
    }

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto register(@RequestBody final UserDto userDto) {
        User user = userMapper.convertToEntity(userDto);

        User addedUser = service.add(user);
        UserDto userDtoFromDb = userMapper.convertToDto(addedUser);

        linksCreator.createForSingleEntity(userDtoFromDb);
        userDtoFromDb.add(linkTo(methodOn(UserController.class).register(userDto)).withSelfRel());

        return userDtoFromDb;
    }

    @GetMapping("/info/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public UserDto getById(@PathVariable long id) {
        User user = service.getById(id);
        UserDto userDto = userMapper.convertToDto(user);

        linksCreator.createForSingleEntity(userDto);
        userDto.add(linkTo(methodOn(UserController.class).getById(id)).withSelfRel());

        return userDto;
    }

    @GetMapping("/info")
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<UserDto> getAll(@RequestParam(required = false, defaultValue = "1") int pageNumber,
                                           @RequestParam(required = false, defaultValue = "10") int pageSize) {

        List<User> users = service.getAll(pageNumber, pageSize);
        List<UserDto> usersDto = userMapper.convertAllToDto(users);

        linksCreator.createForListEntities(usersDto);
        Link selfLink = linkTo(methodOn(UserController.class).getAll(pageNumber, pageSize)).withSelfRel();

        return CollectionModel.of(usersDto, selfLink);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<Link> deleteById(@PathVariable long id) {
        service.lock(id);

        return linksCreator.createByEntityId(id);
    }

    @GetMapping("/info/{userId}/orders")
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<OrderDto> getUserOrders(@PathVariable long userId,
                                                   @RequestParam(required = false, defaultValue = "1") int pageNumber,
                                                   @RequestParam(required = false, defaultValue = "10") int pageSize) {

        List<Order> orders = service.getUserOrdersByUserId(userId, pageNumber, pageSize);
        List<OrderDto> ordersDto = orderMapper.convertAllToDto(orders);

        linksCreator.createForUserOrders(ordersDto, userId);
        Link selfLink = linkTo(methodOn(UserController.class).getUserOrders(userId, pageNumber, pageSize)).withSelfRel();

        return CollectionModel.of(ordersDto, selfLink);
    }

    @GetMapping("/user/largest-cost-orders")
    @ResponseStatus(HttpStatus.FOUND)
    public UserDto getUserWithLargestAmountOrders(){
        User user = service.getUserWithLargestAmountOrders();
        UserDto userDto = userMapper.convertToDto(user);

        linksCreator.createForSingleEntity(userDto);
        userDto.add(linkTo(methodOn(UserController.class).getUserWithLargestAmountOrders()).withSelfRel());

        return userDto;
    }
}
