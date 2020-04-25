package com.kernel.auditoriums.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.kernel.auditoriums.entity.utils.Views;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Auditorium {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({Views.Default.class, Views.Auditorium.class, Views.Lecturer.class, Views.Group.class})
    private Integer id;
    @JsonView({Views.Default.class, Views.Auditorium.class, Views.Lecturer.class, Views.Group.class})
    private String name;
    @JsonView({Views.Default.class, Views.Auditorium.class})
    private int capacity;
    @JsonView({Views.Default.class, Views.Auditorium.class})
    private boolean active;
    @JsonView(Views.Auditorium.class)
    @OneToMany(mappedBy = "auditorium", fetch = FetchType.LAZY)
    private Set<Lecture> lectures;

    public Auditorium(String name, int capacity, boolean active) {
        this.name = name;
        this.capacity = capacity;
        this.active = active;
    }
}
