package com.kernel.auditoriums.service;

import com.kernel.auditoriums.entity.User;
import com.kernel.auditoriums.entity.UserType;
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
        user.setUserType(UserType.USER);
        user.setPassword(encoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    public User getByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public ResponseEntity<User> editUser(String login, User userFromDb, User user) {
        User authUser = getByLogin(login);

        if (authUser.getUserType().equals(UserType.ADMIN) && !login.equals(userFromDb.getLogin())) {
            return editUserByAdmin(authUser, userFromDb, user);
        }

        if (!login.equals(userFromDb.getLogin())) {
            throw new ApiException("Access Denied", HttpStatus.FORBIDDEN);
        }

        if (user.getPassword() == null || !encoder.matches(user.getPassword(), userFromDb.getPassword())) {
            throw new ApiException("Неверный пароль", HttpStatus.FORBIDDEN);
        }

        userFromDb.setFirstName(user.getFirstName());
        userFromDb.setLastName(user.getLastName());
        userFromDb.setPatronymic(user.getPatronymic());
        return ResponseEntity.ok(userRepository.save(userFromDb));
    }

    private ResponseEntity<User> editUserByAdmin(User authUser, User userFromDb, User user) {
        if (user.getPassword() == null || !encoder.matches(user.getPassword(), authUser.getPassword())) {
            throw new ApiException("Неверный пароль", HttpStatus.FORBIDDEN);
        }

        userFromDb.setUserType(user.getUserType());
        return ResponseEntity.ok(userRepository.save(userFromDb));
    }
}
