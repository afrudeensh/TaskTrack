package com.sha.tasktrack.controller;

import com.sha.tasktrack.dto.LoginRequest;
import com.sha.tasktrack.dto.UserDto;
import com.sha.tasktrack.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestParam String name, @RequestParam String email, @RequestParam String password) {
        return ResponseEntity.ok(userService.register(name, email, password));
    }

    /**
     * With HTTP Basic, the actual authentication happens by the filter.
     * This endpoint allows clients to POST credentials and get minimal profile back.
     */
    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password) {
        try {
            UserDto user = userService.login(email, password);
            return "Login successful! Welcome " + user.getName();
        } catch (RuntimeException ex) {
            return ex.getMessage();
        }
    }

}
