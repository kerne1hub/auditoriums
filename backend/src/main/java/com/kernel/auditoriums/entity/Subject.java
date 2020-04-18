package com.kernel.auditoriums.entity;

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
    private Integer id;
    private String name;
    @ManyToMany(mappedBy = "subjects", fetch = FetchType.LAZY)
    private Set<Group> groups;
    @ManyToMany(mappedBy = "subjects", fetch = FetchType.LAZY)
    private Set<Lecturer> lecturers;
}
