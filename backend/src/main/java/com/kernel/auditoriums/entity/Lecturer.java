package com.kernel.auditoriums.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@DiscriminatorValue("LECTURER")
@Getter
@Setter
@ToString
public class Lecturer extends User {
    private String position;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "lecturer_subject",
            joinColumns = @JoinColumn(name = "subject_id", columnDefinition = "int"),
            inverseJoinColumns = @JoinColumn(name = "lecturer_id", columnDefinition = "bigint")
    )
    private Set<Subject> subjects;
    @ManyToMany(mappedBy = "lecturers", fetch = FetchType.LAZY)
    private Set<Group> groups;
    @OneToMany(mappedBy = "lecturer", fetch = FetchType.LAZY)
    private Set<Lecture> lectures;
}
