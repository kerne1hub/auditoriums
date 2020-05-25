package com.kernel.auditoriums.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.kernel.auditoriums.entity.User;
import com.kernel.auditoriums.entity.utils.Views;
import com.kernel.auditoriums.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("auth/login")
    @JsonView(Views.Default.class)
    public ResponseEntity<Object> login(@RequestBody User user) {

        User userFromDb = userService.getByLogin(user.getLogin());

        if (userFromDb == null) {
            throw new UsernameNotFoundException("User " + user.getLogin() + " not found!");
        }

        String token = userService.login(user.getLogin(), user.getPassword());

        Map<Object, Object> response = new HashMap<>();
        response.put("user", userFromDb);
        response.put("token", token);

        return ResponseEntity.ok(response);
    }
}
