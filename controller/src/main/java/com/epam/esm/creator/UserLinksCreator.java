package com.epam.esm.creator;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;

import java.util.List;

public interface UserLinksCreator extends LinksCreator<UserDto>{

    void createForUserOrders(List<OrderDto> orderDto, long userId);
}
