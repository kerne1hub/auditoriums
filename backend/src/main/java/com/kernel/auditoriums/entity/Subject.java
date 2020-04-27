package com.kernel.auditoriums.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.kernel.auditoriums.entity.utils.Views;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@ToString
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({Views.Default.class, Views.Auditorium.class, Views.Lecturer.class, Views.Group.class, Views.Subject.class, Views.Lecture.class})
    private Integer id;
    @JsonView({Views.Default.class, Views.Auditorium.class, Views.Lecturer.class, Views.Group.class, Views.Subject.class, Views.Lecture.class})
    private String name;
    @ManyToMany(mappedBy = "subjects", fetch = FetchType.LAZY)
    @JsonView(Views.Subject.class)
    private Set<Group> groups;
    @ManyToMany(mappedBy = "subjects", fetch = FetchType.LAZY)
    @JsonView(Views.Subject.class)
    private Set<Lecturer> lecturers;

    public Subject(String name) {
        this.name = name;
    }
}
