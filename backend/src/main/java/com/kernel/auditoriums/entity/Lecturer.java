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
import java.util.Set;

@EqualsAndHashCode(callSuper = true, exclude = {"subjects", "groups", "lectures"})
@Entity
@PrimaryKeyJoinColumn(name = "id")
@NoArgsConstructor
@Data
@ToString
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Lecturer extends User {
    @JsonView({Views.Default.class, Views.Auditorium.class, Views.Lecturer.class})
    private String position;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "lecturer_subject",
            joinColumns = @JoinColumn(name = "subject_id", columnDefinition = "int"),
            inverseJoinColumns = @JoinColumn(name = "lecturer_id", columnDefinition = "bigint")
    )
    @JsonView(Views.Lecturer.class)
    private Set<Subject> subjects;
    @ManyToMany(mappedBy = "lecturers", fetch = FetchType.LAZY)
    @JsonView(Views.Lecturer.class)
    private Set<Group> groups;
    @OneToMany(mappedBy = "lecturer", fetch = FetchType.LAZY)
    @JsonView(Views.Lecturer.class)
    private Set<Lecture> lectures;

    public Lecturer(String firstName, String lastName, String patronymic, String email, String login, String password, String position) {
        super(firstName, lastName, patronymic, email, login, password);
        setUserType(UserType.LECTURER);
        this.position = position;
    }
}
