package com.kernel.auditoriums.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.kernel.auditoriums.entity.Auditorium;
import com.kernel.auditoriums.entity.utils.Views;
import com.kernel.auditoriums.service.AuditoriumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/auditoriums")
public class AuditoriumController {

    private final AuditoriumService auditoriumService;

    @Autowired
    public AuditoriumController(AuditoriumService auditoriumService) {
        this.auditoriumService = auditoriumService;
    }

    @GetMapping
    @JsonView(Views.Auditorium.class)
    public ResponseEntity<List<Auditorium>> getAuditoriums(@RequestParam(value = "buildingId", required = false) Integer buildingId,
                                                           @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") Date date) {

        return auditoriumService.getAuditoriums(buildingId, date);
    }

    @GetMapping("/{id}")
    @JsonView(Views.Auditorium.class)
    public ResponseEntity<Auditorium> getAuditoriumDetails(@PathVariable("id") Auditorium auditorium) {
        return auditoriumService.getAuditorium(auditorium);
    }

    @PostMapping
    @JsonView(Views.Default.class)
    public ResponseEntity<Auditorium> createAuditorium(@RequestBody Auditorium auditorium) {
        return auditoriumService.createAuditorium(auditorium);
    }

    @DeleteMapping("/{id}")
    public void deleteAuditorium(@PathVariable("id") int id) {
        auditoriumService.deleteAuditorium(id);
    }

    @PutMapping("/{id}")
    @JsonView(Views.Default.class)
    public ResponseEntity<Auditorium> updateAuditorium(@PathVariable("id") Auditorium auditoriumFromDb,
                                                       @RequestBody Auditorium auditorium) {
        return auditoriumService.updateAuditorium(auditoriumFromDb, auditorium);
    }
}
