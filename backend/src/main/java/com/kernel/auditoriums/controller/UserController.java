package com.kernel.auditoriums.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.kernel.auditoriums.entity.User;
import com.kernel.auditoriums.entity.utils.Views;
import com.kernel.auditoriums.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    @JsonView(Views.User.class)
    public ResponseEntity<?> login(@RequestBody User user) {
        return userService.login(user);
    }

    @PostMapping("/register")
    @JsonView(Views.User.class)
    public ResponseEntity<User> register(@RequestBody User user) {
        return userService.register(user);
    }

    @PutMapping("/users/{id}")
    @JsonView(Views.User.class)
    public ResponseEntity<User> editUserDetails(@AuthenticationPrincipal Authentication authentication, @PathVariable("id") User userFromDb,
                                                @RequestBody User user) {
        return userService.editUser(authentication.getName(), userFromDb, user);
    }
}
