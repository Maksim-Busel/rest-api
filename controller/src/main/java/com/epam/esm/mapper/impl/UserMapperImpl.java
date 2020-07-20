package com.epam.esm.mapper.impl;

import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import com.epam.esm.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapperImpl implements Mapper<User, UserDto> {
    private final ModelMapper mapper;

    @Autowired
    public UserMapperImpl(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public UserDto convertToDto(User user) {
        UserDto userDto = mapper.map(user, UserDto.class);

        return userDto;
    }

    @Override
    public User convertToEntity(UserDto userDto) {
        User user = mapper.map(userDto, User.class);

        return user;
    }

    @Override
    public List<UserDto> convertAllToDto(List<User> users) {
        return users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
