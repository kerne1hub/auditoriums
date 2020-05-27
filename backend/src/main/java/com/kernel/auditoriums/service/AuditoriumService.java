package com.kernel.auditoriums.service;

import com.kernel.auditoriums.entity.Auditorium;
import com.kernel.auditoriums.entity.Lecture;
import com.kernel.auditoriums.repository.AuditoriumRepository;
import com.kernel.auditoriums.repository.BuildingRepository;
import com.kernel.auditoriums.repository.LectureRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuditoriumService {
    private final AuditoriumRepository auditoriumRepository;
    private final BuildingRepository buildingRepository;
    private final LectureRepository lectureRepository;

    public AuditoriumService(AuditoriumRepository auditoriumRepository, BuildingRepository buildingRepository, LectureRepository lectureRepository) {
        this.auditoriumRepository = auditoriumRepository;
        this.buildingRepository = buildingRepository;
        this.lectureRepository = lectureRepository;
    }


    private Date setDayOfWeek(Date date, int dayOfWeek) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        return calendar.getTime();
    }

    public ResponseEntity<List<Auditorium>> getAuditoriums(Integer buildingId, Date date, String keyword) {
        Map<Integer, Auditorium>  auditoriumMap = new HashMap<>();
        List<Auditorium> auditoriums;

        if (keyword != null) {
            auditoriums = auditoriumRepository.findAllByNameContains(keyword);
            return ResponseEntity.ok(auditoriums);
        }

        if (buildingId == null) {
            auditoriums = auditoriumRepository.findAll();
        } else {
            if (date != null) {
                Date startWeekDate = setDayOfWeek(date, Calendar.MONDAY);
                Date endWeekDate = setDayOfWeek(date, Calendar.SATURDAY);

                auditoriums = auditoriumRepository.findAllByBuildingIdCustom(buildingId);

                for (Auditorium auditorium: auditoriums) {
                    auditoriumMap.put(auditorium.getId(), auditorium);
                }

                List<Lecture> lectures = lectureRepository.findAllByAuditoriumIdInAndDateBetween(auditoriumMap.keySet(), startWeekDate, endWeekDate);

                for (Lecture lecture: lectures) {
                    auditoriumMap.get(lecture.getAuditoriumId()).getLectures().add(lecture);
                }

            } else {
                auditoriums = auditoriumRepository.findAllByBuildingId(buildingId);
            }
        }
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
