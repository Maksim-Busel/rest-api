package com.epam.esm.mapper.impl;

import com.epam.esm.dto.BikeGoodsDto;
import com.epam.esm.entity.BikeGoods;
import com.epam.esm.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BikeGoodsMapperImpl implements Mapper<BikeGoods, BikeGoodsDto> {
    private final ModelMapper mapper;

    @Autowired
    public BikeGoodsMapperImpl(ModelMapper mapper) {
        this.mapper = mapper;
    }


    public BikeGoodsDto convertToDto(BikeGoods goods){
        return mapper.map(goods, BikeGoodsDto.class);
    }

    public BikeGoods convertToEntity(BikeGoodsDto goodsDto){
        return mapper.map(goodsDto, BikeGoods.class);
    }

    @Override
    public List<BikeGoodsDto> convertAllToDto(List<BikeGoods> goods) {
        return goods.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
