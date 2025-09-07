package com.sha.tasktrack.service.impl;

import com.sha.tasktrack.dto.UserDto;
import com.sha.tasktrack.entity.User;
import com.sha.tasktrack.exception.BadRequestException;
import com.sha.tasktrack.mapper.UserMapper;
import com.sha.tasktrack.repository.UserRepository;
import com.sha.tasktrack.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDto register(String name, String email, String rawPassword) {
        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email already registered");
        }
        User user = User.builder().name(name).email(email).password(passwordEncoder.encode(rawPassword)).totalTasks(0).build();
        userRepository.save(user);
        return UserMapper.toDTO(user);
    }

    @Override
    public UserDto login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found! Please sign up."));

        if (!passwordEncoder.matches(rawPassword.trim(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials!");
        }

        return UserDto.builder().id(user.getId()).name(user.getName()).email(user.getEmail()).build();
    }


    @Override
    public User findEntityByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new BadRequestException("Invalid user email"));
    }

    @Override
    public UserDto getCurrentUser(String email) {
        return userRepository.findByEmail(email).map(UserMapper::toDTO).orElseThrow(() -> new BadRequestException("User not found"));
    }


}
