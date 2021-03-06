package com.kernel.auditoriums.service;

import com.kernel.auditoriums.entity.Building;
import com.kernel.auditoriums.repository.BuildingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuildingService {

    private final BuildingRepository repository;

    @Autowired
    public BuildingService(BuildingRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<List<Building>> getBuildings(String keyword) {
        if (keyword != null) {
            return ResponseEntity.ok(repository.findAllByNameContains(keyword));
        }
        return ResponseEntity.ok(repository.findAll());
    }

    public ResponseEntity<Building> getBuilding(Building building) {
        if (building != null) {
            return ResponseEntity.ok(building);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Building> createBuilding(Building building) {
        return new ResponseEntity<>(repository.save(building), HttpStatus.CREATED);
    }

    public void deleteBuilding(Building building) {
        repository.delete(building);
    }

    public ResponseEntity<Building> editBuilding(Building buildingFromDb, Building building) {
        buildingFromDb.setName(building.getName());
        buildingFromDb.setAddress(building.getAddress());
        return ResponseEntity.ok(repository.save(buildingFromDb));
    }
}
