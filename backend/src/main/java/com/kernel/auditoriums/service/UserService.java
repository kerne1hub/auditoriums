package com.kernel.auditoriums.service;

import com.kernel.auditoriums.entity.User;
import com.kernel.auditoriums.exception.ApiException;
import com.kernel.auditoriums.repository.UserRepository;
import com.kernel.auditoriums.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder encoder, JwtTokenProvider tokenProvider, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
    }

    public ResponseEntity<?> login(User user) {
        User userFromDb = getByLogin(user.getLogin());

        if (userFromDb == null) {
            throw new UsernameNotFoundException("User " + user.getLogin() + " not found!");
        }

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword()));
            String token = tokenProvider.createToken(user.getLogin());

            Map<Object, Object> response = new HashMap<>();
            response.put("user", userFromDb);
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new ApiException("Invalid login/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public ResponseEntity<User> register(User user) {
        user.setPassword(encoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    public User getByLogin(String login) {
        return userRepository.findByLogin(login);
    }
}
