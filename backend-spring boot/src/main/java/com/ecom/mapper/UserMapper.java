package com.ecom.mapper;

import com.ecom.dto.OrderDto;
import com.ecom.dto.OrderItemDto;
import com.ecom.dto.UserDto;
import com.ecom.model.Order;
import com.ecom.model.OrderItem;
import com.ecom.model.User;

public class UserMapper {

    public static UserDto toUserDto(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFullName(user.getFullName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

}
