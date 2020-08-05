package com.epam.esm.creator.impl;

import com.epam.esm.controller.BikeGoodsController;
import com.epam.esm.creator.LinksCreator;
import com.epam.esm.dto.BikeGoodsDto;
import com.epam.esm.entity.BikeGoods;
import com.epam.esm.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BikeGoodsLinksCreatorImpl implements LinksCreator<BikeGoodsDto> {
    private static final int DEFAULT_PAGE_NUMBER = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final String GET_GOODS = "getGoods";
    private static final String DELETE_GOODS = "deleteGoods";
    private static final String ALL_GOODS = "allGoods";
    private static final String CREATE_GOODS = "createGoods";

    private final Validator<BikeGoods> validator;

    @Autowired
    public BikeGoodsLinksCreatorImpl(Validator<BikeGoods> validator) {
        this.validator = validator;
    }

    @Override
    public void createForSingleEntity(BikeGoodsDto bikeGoodsDto) {
        long goodsId = bikeGoodsDto.getId();

        if (validator.isAdmin()) {
            bikeGoodsDto.add(
                    linkTo(methodOn(BikeGoodsController.class).deleteById(goodsId)).withRel(DELETE_GOODS),
                    linkTo(methodOn(BikeGoodsController.class).add(new BikeGoodsDto())).withRel(CREATE_GOODS));
        }

        bikeGoodsDto.add(
                linkTo(methodOn(BikeGoodsController.class).getById(goodsId)).withRel(GET_GOODS),
                linkTo(methodOn(BikeGoodsController.class).getAll(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE)).withRel(ALL_GOODS));
    }

    @Override
    public void createForListEntities(List<BikeGoodsDto> bikeGoodsDto) {
        for (BikeGoodsDto goodsDto : bikeGoodsDto) {
            long goodsId = goodsDto.getId();
            if (validator.isAdmin()) {
                goodsDto.add(linkTo(methodOn(BikeGoodsController.class).deleteById(goodsId)).withRel(DELETE_GOODS));
            }
            goodsDto.add(linkTo(methodOn(BikeGoodsController.class).getById(goodsId)).withRel(GET_GOODS));
        }
    }

    @Override
    public List<Link> createByEntityId(long goodsId) {
        List<Link> links = new ArrayList<>();

        if (validator.isAdmin()) {
            links.add(linkTo(methodOn(BikeGoodsController.class).deleteById(goodsId)).withSelfRel());
            links.add(linkTo(methodOn(BikeGoodsController.class).add(new BikeGoodsDto())).withRel(CREATE_GOODS));
        }
        links.add(linkTo(methodOn(BikeGoodsController.class).getById(goodsId)).withRel(GET_GOODS));
        links.add(linkTo(methodOn(BikeGoodsController.class).getAll(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE)).withRel(ALL_GOODS));

        return links;
    }
}
