package com.kernel.auditoriums.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.kernel.auditoriums.entity.utils.Views;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "`group`")
@Data
@NoArgsConstructor
@ToString
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({Views.Default.class, Views.Auditorium.class, Views.Lecturer.class, Views.Group.class})
    private Long id;
    @JsonView({Views.Default.class, Views.Auditorium.class, Views.Lecturer.class, Views.Group.class})
    private String name;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "group_subject",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    @JsonView(Views.Group.class)
    private Set<Subject> subjects;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "group_lecturer",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "lecturer_id")
    )
    @JsonView(Views.Group.class)
    private Set<Lecturer> lecturers;
    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    @JsonView(Views.Group.class)
    private Set<Lecture> lectures;

    public Group(String name) {
        this.name = name;
    }
}
