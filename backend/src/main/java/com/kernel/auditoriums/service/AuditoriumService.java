package com.kernel.auditoriums.service;

import com.kernel.auditoriums.entity.Auditorium;
import com.kernel.auditoriums.repository.AuditoriumRepository;
import com.kernel.auditoriums.repository.BuildingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditoriumService {
    private final AuditoriumRepository auditoriumRepository;
    private final BuildingRepository buildingRepository;

    public AuditoriumService(AuditoriumRepository auditoriumRepository, BuildingRepository buildingRepository) {
        this.auditoriumRepository = auditoriumRepository;
        this.buildingRepository = buildingRepository;
    }

    public ResponseEntity<List<Auditorium>> getAuditoriums() {
        List<Auditorium> auditoriums = auditoriumRepository.findAll();
        return ResponseEntity.ok(auditoriums);
    }

    public ResponseEntity<Auditorium> createAuditorium(Auditorium auditorium) {
        auditorium.setBuilding(buildingRepository.getOne(auditorium.getBuildingId()));
        return new ResponseEntity<>(auditoriumRepository.save(auditorium), HttpStatus.CREATED);
    }

    public void deleteAuditorium(int auditoriumId) {
        auditoriumRepository.deleteById(auditoriumId);
    }

    public ResponseEntity<Auditorium> updateAuditorium(Auditorium auditoriumFromDb, Auditorium auditorium) {
        auditoriumFromDb.setName(auditorium.getName());
        auditoriumFromDb.setCapacity(auditorium.getCapacity());
        auditoriumFromDb.setActive(auditorium.isActive());

        return ResponseEntity.ok(auditoriumRepository.save(auditoriumFromDb));
    }

    public ResponseEntity<Auditorium> getAuditorium(Auditorium auditorium) {
        if (auditorium != null) {
            return ResponseEntity.ok(auditorium);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
