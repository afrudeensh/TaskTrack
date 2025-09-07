package com.sha.tasktrack.mapper;

import com.sha.tasktrack.dto.UserDto;
import com.sha.tasktrack.entity.User;

public class UserMapper {//Entity to DTO

    public static UserDto toDTO(User user) {
        if (user == null) return null;
        return UserDto.builder().id(user.getId()).name(user.getName()).email(user.getEmail()).totalTasks(user.getTotalTasks()).build();
    }
}
