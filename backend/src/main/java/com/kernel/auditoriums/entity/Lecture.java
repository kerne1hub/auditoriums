package com.kernel.auditoriums.entity;

import com.fasterxml.jackson.annotation.*;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Lecture.class)
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({Views.Default.class, Views.Auditorium.class, Views.Lecturer.class, Views.Group.class, Views.Lecture.class})
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "GMT+06:00")
    @JsonView({Views.Default.class, Views.Auditorium.class, Views.Lecturer.class, Views.Group.class, Views.Lecture.class})
    private Date date;

    @Transient
    private final int duration = 90;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecturer_id")
    @JsonView({Views.Default.class, Views.Auditorium.class, Views.Group.class, Views.Lecture.class})
    private Lecturer lecturer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    @JsonView({Views.Default.class, Views.Auditorium.class, Views.Lecturer.class, Views.Group.class, Views.Lecture.class})
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    @JsonView({Views.Default.class, Views.Auditorium.class, Views.Lecturer.class, Views.Lecture.class})
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auditorium_id")
    @JsonView({Views.Default.class, Views.Lecturer.class, Views.Group.class, Views.Lecture.class})
    private Auditorium auditorium;

    @Column(name = "lecturer_id", insertable = false, updatable = false)
    private Long lecturerId;

    @Column(name = "subject_id", insertable = false, updatable = false)
    private Integer subjectId;

    @Column(name = "group_id", insertable = false, updatable = false)
    private Long groupId;

    @Column(name = "auditorium_id", insertable = false, updatable = false)
    private Integer auditoriumId;

    public Lecture(Date date, Long lecturerId, int subjectId, long groupId, int auditoriumId) {
        this.date = date;
        this.lecturerId = lecturerId;
        this.subjectId = subjectId;
        this.groupId = groupId;
        this.auditoriumId = auditoriumId;
    }
}
