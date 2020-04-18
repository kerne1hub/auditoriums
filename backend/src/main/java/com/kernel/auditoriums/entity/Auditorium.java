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
public class Auditorium {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private int capacity;
    private boolean active;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "auditorium_lecture",
            joinColumns = @JoinColumn(name = "lecture_id", columnDefinition = "bigint"),
            inverseJoinColumns = @JoinColumn(name = "auditorium_id", columnDefinition = "int")
    )
    private Set<Lecture> lectures;
}
