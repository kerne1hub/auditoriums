package com.kernel.auditoriums.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.kernel.auditoriums.entity.utils.Views;
import lombok.*;

import javax.persistence.*;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Auditorium {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({Views.Default.class, Views.Auditorium.class, Views.Lecturer.class, Views.Group.class, Views.Lecture.class, Views.Building.class})
    private Integer id;
    @JsonView({Views.Default.class, Views.Auditorium.class, Views.Lecturer.class, Views.Group.class, Views.Lecture.class, Views.Building.class})
    private String name;
    @JsonView({Views.Default.class, Views.Auditorium.class})
    private int capacity;
    @JsonView({Views.Default.class, Views.Auditorium.class})
    private boolean active;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id")
    @JsonView({Views.Auditorium.class, Views.Building.class, Views.Lecture.class})
    private Building building;
    @JsonView({Views.Auditorium.class, Views.Building.class})
    @OneToMany(mappedBy = "auditorium", fetch = FetchType.LAZY)
    private Set<Lecture> lectures = new TreeSet<>();

    @Column(name = "building_id", insertable = false, updatable = false)
    private Integer buildingId;

    public Auditorium(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Auditorium(String name, int capacity, boolean active, int buildingId) {
        this.name = name;
        this.capacity = capacity;
        this.active = active;
        this.buildingId = buildingId;
    }

    public Auditorium(int id, String name, int capacity, boolean active, int buildingId) {
        this(name, capacity, active, buildingId);
        this.id = id;
    }

    public Auditorium(Integer id, String name, int capacity, boolean active, Building building) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.active = active;
        this.building = building;
    }
}
