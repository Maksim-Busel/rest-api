package com.epam.esm.controller;

import com.epam.esm.creator.LinksCreator;
import com.epam.esm.dto.BikeGoodsDto;
import com.epam.esm.entity.BikeGoods;
import com.epam.esm.mapper.Mapper;
import com.epam.esm.security.annotation.IsAdmin;
import com.epam.esm.security.annotation.IsAnyAuthorized;
import com.epam.esm.service.api.BikeGoodsService;
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
@RequestMapping(value = "/bike-goods", produces = MediaType.APPLICATION_JSON_VALUE)
public class BikeGoodsController{
    private final BikeGoodsService service;
    private final Mapper<BikeGoods, BikeGoodsDto> mapper;
    private final LinksCreator<BikeGoodsDto> linksCreator;

    @Autowired
    public BikeGoodsController(BikeGoodsService service, Mapper<BikeGoods, BikeGoodsDto> mapper, LinksCreator<BikeGoodsDto> linksCreator) {
        this.service = service;
        this.mapper = mapper;
        this.linksCreator = linksCreator;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @IsAdmin
    public BikeGoodsDto add(@RequestBody BikeGoodsDto goodsDto) {
        BikeGoods goods = mapper.convertToEntity(goodsDto);

        BikeGoods addedBikeGoods = service.add(goods);
        BikeGoodsDto bikeGoodsDto = mapper.convertToDto(addedBikeGoods);

        linksCreator.createForSingleEntity(bikeGoodsDto);
        bikeGoodsDto.add(linkTo(methodOn(BikeGoodsController.class).add(goodsDto)).withSelfRel());

        return bikeGoodsDto;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @IsAnyAuthorized
    public BikeGoodsDto getById(@PathVariable long id) {
        BikeGoods goods = service.getById(id);
        BikeGoodsDto bikeGoodsDto = mapper.convertToDto(goods);

        linksCreator.createForSingleEntity(bikeGoodsDto);
        bikeGoodsDto.add(linkTo(methodOn(BikeGoodsController.class).getById(id)).withSelfRel());

        return bikeGoodsDto;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @IsAnyAuthorized
    public CollectionModel<BikeGoodsDto> getAll(@RequestParam(required = false, defaultValue = "1") int pageNumber,
                                     @RequestParam(required = false, defaultValue = "10") int pageSize) {
        List<BikeGoods> goods = service.getAll(pageNumber, pageSize);
        List<BikeGoodsDto> bikeGoodsDto = mapper.convertAllToDto(goods);

        linksCreator.createForListEntities(bikeGoodsDto);
        Link selfLink = linkTo(methodOn(BikeGoodsController.class).getAll(pageNumber, pageSize)).withSelfRel();

        return CollectionModel.of(bikeGoodsDto, selfLink);
    }

    @PutMapping("/{bikeGoodsId}")
    @ResponseStatus(HttpStatus.OK)
    @IsAdmin
    public BikeGoodsDto edit(@RequestBody BikeGoodsDto bikeGoodsDto, @PathVariable long bikeGoodsId) {
        BikeGoods bikeGoods = mapper.convertToEntity(bikeGoodsDto);
        bikeGoods.setId(bikeGoodsId);

        BikeGoods editedBikeGoods = service.edit(bikeGoods);
        BikeGoodsDto bikeGoodsDtoFromDb = mapper.convertToDto(editedBikeGoods);

        linksCreator.createForSingleEntity(bikeGoodsDtoFromDb);
        bikeGoodsDtoFromDb.add(linkTo(methodOn(BikeGoodsController.class).edit(bikeGoodsDto, bikeGoodsId)).withSelfRel());

        return bikeGoodsDtoFromDb;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @IsAdmin
    public List<Link> deleteById(@PathVariable long id) {
        service.lock(id);

        return linksCreator.createByEntityId(id);
    }
}
