package com.kernel.auditoriums.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.kernel.auditoriums.entity.utils.Views;
import lombok.*;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({Views.Default.class, Views.Auditorium.class, Views.Lecturer.class, Views.Group.class, Views.Subject.class, Views.User.class, Views.Lecture.class})
    private Long id;
    @Column(name = "firstname")
    @JsonView({Views.Default.class, Views.Auditorium.class, Views.Lecturer.class, Views.Group.class, Views.Subject.class, Views.User.class, Views.Lecture.class})
    private String firstName;
    @Column(name = "lastname")
    @JsonView({Views.Default.class, Views.Auditorium.class, Views.Lecturer.class, Views.Group.class, Views.Subject.class, Views.User.class, Views.Lecture.class})
    private String lastName;
    @JsonView({Views.Default.class, Views.Auditorium.class, Views.Lecturer.class, Views.Group.class, Views.Subject.class, Views.User.class, Views.Lecture.class})
    private String patronymic;
    @JsonView({Views.Extended.class, Views.User.class, Views.Lecturer.class})
    private String email;
    @JsonView({Views.Extended.class, Views.User.class, Views.Lecturer.class})
    private String login;
    @JsonView(Views.Credentials.class)
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    @JsonView({Views.Default.class, Views.User.class, Views.Lecturer.class})
    private UserType userType;

    public User(String firstName, String lastName, String patronymic, String email, String login, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.email = email;
        this.login = login;
        this.password = password;
    }
}
