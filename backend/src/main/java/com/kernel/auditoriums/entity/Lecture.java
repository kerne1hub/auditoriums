package com.kernel.auditoriums.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date date;
    @Transient
    private final int duration = 90;
    @ManyToOne(cascade = CascadeType.ALL)
    private Lecturer lecturer;
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;
}
