package com.epam.esm.mapper;

import com.epam.esm.entity.Identifable;

import java.util.List;

public interface Mapper <E, D extends Identifable> {

    D convertToDto(E entity);
    E convertToEntity(D entityDto);
    List<D> convertAllToDto(List<E> entity);


}
