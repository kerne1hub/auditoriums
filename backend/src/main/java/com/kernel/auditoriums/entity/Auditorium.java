package com.kernel.auditoriums.entity;

import com.fasterxml.jackson.annotation.*;
import com.kernel.auditoriums.entity.utils.Views;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Auditorium {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Default.class)
    private Integer id;
    @JsonView(Views.Default.class)
    private String name;
    @JsonView(Views.Default.class)
    private int capacity;
    @JsonView(Views.Default.class)
    private boolean active;
    @JsonView(value = Views.Extended.class)
    @JsonBackReference
    @OneToMany(mappedBy = "auditorium", fetch = FetchType.LAZY)
    private Set<Lecture> lectures;
}
