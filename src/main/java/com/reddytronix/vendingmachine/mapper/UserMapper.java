package com.reddytronix.vendingmachine.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.reddytronix.vendingmachine.controller.UserRepresentation;
import com.reddytronix.vendingmachine.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id",
             ignore = true)
    User fromUserRepresentation(final UserRepresentation userRepresentation);

    @Mapping(target = "password",
             ignore = true)
    UserRepresentation toUserRepresentation(final User user);
}
