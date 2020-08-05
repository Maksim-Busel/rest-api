package com.epam.esm.creator;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface LinksCreator<T extends RepresentationModel<T>> {

    void createForSingleEntity(T entity);

    void createForListEntities(List<T> entities);

    List<Link> createByEntityId(long entityId);
}
