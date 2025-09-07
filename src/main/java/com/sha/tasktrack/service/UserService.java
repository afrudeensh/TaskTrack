package com.sha.tasktrack.service;

import com.sha.tasktrack.dto.UserDto;
import com.sha.tasktrack.entity.User;

public interface UserService {
    UserDto register(String name, String email, String rawPassword);
    User findEntityByEmail(String email);
    UserDto getCurrentUser(String email);
    UserDto login(String email, String password);
}
