package com.kernel.auditoriums.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.kernel.auditoriums.entity.Building;
import com.kernel.auditoriums.entity.utils.Views;
import com.kernel.auditoriums.service.BuildingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/buildings")
public class BuildingController {

    private final BuildingService buildingService;

    public BuildingController(BuildingService buildingService) {
        this.buildingService = buildingService;
    }

    @GetMapping
    @JsonView(Views.Default.class)
    public ResponseEntity<List<Building>> getBuildings(@RequestParam(value = "name", required = false) String keyword) {
        return buildingService.getBuildings(keyword);
    }

    @GetMapping("/{id}")
    @JsonView(Views.Building.class)
    public ResponseEntity<Building> getBuildingDetails(@PathVariable("id") Building Building) {
        return buildingService.getBuilding(Building);
    }

    @PostMapping
    @JsonView(Views.Default.class)
    public ResponseEntity<Building> createBuilding(@RequestBody Building Building) {
        return buildingService.createBuilding(Building);
    }
}
