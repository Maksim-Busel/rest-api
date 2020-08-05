package com.epam.esm.controller;

import com.epam.esm.security.annotation.IsAdmin;
import com.epam.esm.security.annotation.IsAuthenticated;
import com.epam.esm.creator.LinksCreator;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Order;
import com.epam.esm.mapper.Mapper;
import com.epam.esm.service.api.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {
    private final OrderService service;
    private final Mapper<Order, OrderDto> mapper;
    private final LinksCreator<OrderDto> linksCreator;

    @Autowired
    public OrderController(OrderService service, Mapper<Order, OrderDto> mapper, LinksCreator<OrderDto> linksCreator) {
        this.service = service;
        this.mapper = mapper;
        this.linksCreator = linksCreator;
    }

    @PostMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("authentication.principal.id == #userId")
    public OrderDto add(@RequestBody final OrderDto orderDto, @PathVariable long userId) {
        Order order = mapper.convertToEntity(orderDto);
        order.setUserId(userId);
        List<Integer> certificatesId = orderDto.getCertificatesId();

        Order addedOrder = service.add(order, certificatesId);
        OrderDto orderDtoFromDb = mapper.convertToDto(addedOrder);

        linksCreator.createForSingleEntity(orderDtoFromDb);
        orderDtoFromDb.add(linkTo(methodOn(OrderController.class).add(orderDto, userId)).withSelfRel());

        return orderDtoFromDb;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @IsAuthenticated
    public OrderDto getById(@PathVariable long id) {
        Order order = service.getById(id);
        OrderDto orderDto = mapper.convertToDto(order);

        linksCreator.createForSingleEntity(orderDto);
        orderDto.add(linkTo(methodOn(OrderController.class).getById(id)).withSelfRel());

        return orderDto;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @IsAuthenticated
    public CollectionModel<OrderDto> getAll(@RequestParam(required = false, defaultValue = "1") int pageNumber,
                                            @RequestParam(required = false, defaultValue = "10") int pageSize) {
        List<Order> orders = service.getAll(pageNumber, pageSize);
        List<OrderDto> ordersDto = mapper.convertAllToDto(orders);

        linksCreator.createForListEntities(ordersDto);
        Link selfLink = linkTo(methodOn(OrderController.class).getAll(pageNumber, pageSize)).withSelfRel();

        return CollectionModel.of(ordersDto, selfLink);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @IsAdmin
    public List<Link> deleteById(@PathVariable long id) {
        service.getById(id);
        service.lock(id);

        return linksCreator.createByEntityId(id);
    }
}
