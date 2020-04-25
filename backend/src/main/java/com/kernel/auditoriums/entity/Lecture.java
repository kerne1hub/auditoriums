package com.kernel.auditoriums.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.kernel.auditoriums.entity.utils.Views;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@EqualsAndHashCode(exclude = {"lecturer", "subject", "group", "auditorium"})
@ToString
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({Views.Default.class, Views.Auditorium.class, Views.Lecturer.class, Views.Group.class})
    private Long id;
    @JsonView({Views.Default.class, Views.Auditorium.class, Views.Lecturer.class, Views.Group.class})
    private Date date;
    @Transient
    private final int duration = 90;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "lecturer_id")
    @JsonView({Views.Default.class, Views.Auditorium.class, Views.Group.class})
    private Lecturer lecturer;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    @JsonView({Views.Default.class, Views.Auditorium.class, Views.Lecturer.class, Views.Group.class})
    private Subject subject;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    @JsonView({Views.Default.class, Views.Auditorium.class, Views.Lecturer.class})
    private Group group;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "auditorium_id", referencedColumnName = "id")
    @JsonView({Views.Default.class, Views.Lecturer.class, Views.Group.class})
    private Auditorium auditorium;
}
