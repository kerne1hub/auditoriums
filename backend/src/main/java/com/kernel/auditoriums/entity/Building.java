package com.kernel.auditoriums.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.kernel.auditoriums.entity.utils.Views;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@ToString
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({Views.Default.class, Views.Auditorium.class, Views.Building.class, Views.Lecture.class})
    private Integer id;
    @JsonView({Views.Default.class, Views.Auditorium.class, Views.Building.class, Views.Lecture.class})
    private String name;
    @JsonView({Views.Default.class, Views.Auditorium.class, Views.Building.class})
    private String address;
    @OneToMany(mappedBy = "building", fetch = FetchType.LAZY)
    @JsonView(Views.Building.class)
    private Set<Auditorium> auditoriums;

    public Building(String name) {
        this.name = name;
    }
}
