package com.kernel.auditoriums.service;

import com.kernel.auditoriums.entity.Auditorium;
import com.kernel.auditoriums.repository.AuditoriumRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditoriumService {
    private final AuditoriumRepository repository;

    public AuditoriumService(AuditoriumRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<List<Auditorium>> getAuditoriums() {
        return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<Auditorium> createAuditorium(Auditorium auditorium) {
        Auditorium savedAuditorium = repository.save(auditorium);
        return new ResponseEntity<>(savedAuditorium, HttpStatus.CREATED);
    }

    public void deleteAuditorium(int auditoriumId) {
        repository.deleteById(auditoriumId);
    }

    public ResponseEntity<Auditorium> updateAuditorium(Auditorium auditoriumFromDb, Auditorium auditorium) {
        auditoriumFromDb.setName(auditorium.getName());
        auditoriumFromDb.setCapacity(auditorium.getCapacity());
        auditoriumFromDb.setActive(auditorium.isActive());

        return new ResponseEntity<>(repository.save(auditoriumFromDb), HttpStatus.OK);
    }
}
