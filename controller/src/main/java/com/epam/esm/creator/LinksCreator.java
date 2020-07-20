package com.epam.esm.creator;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

public interface LinksCreator<T extends RepresentationModel<T>> {

    T createForSingleEntity(T entity);

    List<T> createForListEntities(List<T> entities);

    List<Link> createByEntityId(long entityId);

}
