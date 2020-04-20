package com.kernel.auditoriums.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.kernel.auditoriums.entity.Auditorium;
import com.kernel.auditoriums.entity.utils.Views;
import com.kernel.auditoriums.service.AuditoriumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("auditoriums")
public class AuditoriumController {

    private final AuditoriumService auditoriumService;

    @Autowired
    public AuditoriumController(AuditoriumService auditoriumService) {
        this.auditoriumService = auditoriumService;
    }

    @GetMapping
    @JsonView({Views.Default.class})
    public ResponseEntity<List<Auditorium>> getAuditoriums() {
        return auditoriumService.getAuditoriums();
    }

    @GetMapping("{id}")
    @JsonView({Views.Extended.class})
    public ResponseEntity<Auditorium> getAuditoriumDescription(@PathVariable("id") Auditorium auditorium) {
        return new ResponseEntity<>(auditorium, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Auditorium> createAuditorium(@RequestBody Auditorium auditorium) {
        return auditoriumService.createAuditorium(auditorium);
    }

    @DeleteMapping("{id}")
    public void deleteAuditorium(@PathVariable("id") int id) {
        auditoriumService.deleteAuditorium(id);
    }

    @PutMapping("{id}")
    @JsonView(Views.Default.class)
    public ResponseEntity<Auditorium> updateAuditorium(@PathVariable("id") Auditorium auditoriumFromDb,
                                                       @RequestBody Auditorium auditorium) {
        return auditoriumService.updateAuditorium(auditoriumFromDb, auditorium);
    }
}
