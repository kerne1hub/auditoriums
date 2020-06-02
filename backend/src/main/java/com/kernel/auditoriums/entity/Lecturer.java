package com.kernel.auditoriums.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.kernel.auditoriums.entity.utils.Views;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@EqualsAndHashCode(callSuper = true, exclude = {"subjects", "groups", "lectures"})
@Entity
@PrimaryKeyJoinColumn(name = "id")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Lecturer.class)
public class Lecturer extends User {

    @Builder
    public Lecturer(Long id, String firstName, String lastName, String patronymic, String email, String login, String password, UserType userType) {
        super(id, firstName, lastName, patronymic, email, login, password, userType);
    }

    public Lecturer(String firstName, String lastName, String patronymic, String email, String login, String password, String position) {
        super(firstName, lastName, patronymic, email, login, password);
        setUserType(UserType.LECTURER);
        this.position = position;
    }

    @JsonView({Views.Default.class, Views.Auditorium.class, Views.Lecturer.class, Views.Group.class, Views.Subject.class, Views.Lecture.class})
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
    @JsonView(Views.Extended.class)
    private Set<Lecture> lectures;
}
