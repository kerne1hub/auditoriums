package com.kernel.auditoriums.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.kernel.auditoriums.entity.utils.Views;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({Views.Default.class, Views.Auditorium.class, Views.Lecturer.class, Views.Group.class})
    private Integer id;
    @JsonView({Views.Default.class, Views.Auditorium.class, Views.Lecturer.class, Views.Group.class})
    private String name;
    @ManyToMany(mappedBy = "subjects", fetch = FetchType.LAZY)
    private Set<Group> groups;
    @ManyToMany(mappedBy = "subjects", fetch = FetchType.LAZY)
    private Set<Lecturer> lecturers;
}
