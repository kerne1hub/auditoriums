package com.kernel.auditoriums.service;

import com.kernel.auditoriums.entity.Lecturer;
import com.kernel.auditoriums.entity.UserType;
import com.kernel.auditoriums.exception.ApiException;
import com.kernel.auditoriums.repository.LecturerRepository;
import com.kernel.auditoriums.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LecturerService {

    private final LecturerRepository repository;
    private final PasswordEncoder encoder;
    private final UserService userService;

    @Autowired
    public LecturerService(LecturerRepository repository, PasswordEncoder encoder, JwtTokenProvider tokenProvider, AuthenticationManager authenticationManager, UserService userService) {
        this.repository = repository;
        this.encoder = encoder;
        this.userService = userService;
    }

    public ResponseEntity<List<Lecturer>> getLecturers() {
        return ResponseEntity.ok(repository.findAll());
    }

    public ResponseEntity<Lecturer> getLecturer(Lecturer lecturer) {
        if (lecturer != null) {
            return ResponseEntity.ok(lecturer);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Lecturer> createLecturer(Lecturer lecturer) {
        lecturer.setUserType(UserType.LECTURER);
        lecturer.setPassword(encoder.encode(lecturer.getPassword()));
        repository.save(lecturer);

        return new ResponseEntity<>(lecturer, HttpStatus.CREATED);
    }

    public ResponseEntity<Lecturer> editLecturer(String login, Lecturer lecturerFromDb, Lecturer lecturer) {
        if (!login.equals(lecturerFromDb.getLogin())) {
            throw new ApiException("Access Denied", HttpStatus.FORBIDDEN);
        }

        if (lecturer.getPassword() == null || !encoder.matches(lecturer.getPassword(), lecturerFromDb.getPassword())) {
            throw new ApiException("Неверный пароль", HttpStatus.FORBIDDEN);
        }

        lecturerFromDb.setFirstName(lecturer.getFirstName());
        lecturerFromDb.setLastName(lecturer.getLastName());
        lecturerFromDb.setPosition(lecturer.getPosition());
        return ResponseEntity.ok(repository.save(lecturerFromDb));
    }

    public void deleteLecturer(Lecturer lecturerFromDb) {
        repository.delete(lecturerFromDb);
    }
}
