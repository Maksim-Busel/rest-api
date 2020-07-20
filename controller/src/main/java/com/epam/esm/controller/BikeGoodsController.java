package com.epam.esm.controller;

import com.epam.esm.creator.LinksCreator;
import com.epam.esm.dto.BikeGoodsDto;
import com.epam.esm.entity.BikeGoods;
import com.epam.esm.mapper.Mapper;
import com.epam.esm.service.api.BikeGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public BikeGoodsDto add(@RequestBody BikeGoodsDto goodsDto) {
        BikeGoods goods = mapper.convertToEntity(goodsDto);
        goods.setId(0);

        BikeGoods addedBikeGoods = service.add(goods);
        BikeGoodsDto bikeGoodsDto = mapper.convertToDto(addedBikeGoods);

        return linksCreator.createForSingleEntity(bikeGoodsDto);
    }

    @GetMapping("/info/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public BikeGoodsDto getById(@PathVariable long id) {
        BikeGoods goods = service.getById(id);
        BikeGoodsDto bikeGoodsDto = mapper.convertToDto(goods);

        return linksCreator.createForSingleEntity(bikeGoodsDto);
    }

    @GetMapping("/info")
    @ResponseStatus(HttpStatus.FOUND)
    public List<BikeGoodsDto> getAll(@RequestParam(required = false, defaultValue = "1") int pageNumber,
                                     @RequestParam(required = false, defaultValue = "10") int pageSize) {
        List<BikeGoods> goods = service.getAll(pageNumber, pageSize);
        List<BikeGoodsDto> bikeGoodsDto = mapper.convertAllToDto(goods);

        return linksCreator.createForListEntities(bikeGoodsDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.LOCKED)
    public List<Link> deleteById(@PathVariable long id) {
        service.lock(id);

        return linksCreator.createByEntityId(id);
    }
}
