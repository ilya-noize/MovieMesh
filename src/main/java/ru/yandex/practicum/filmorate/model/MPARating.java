package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Entity
@Table(name = "mpa_rating")
@Data
public class MPARating {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @Positive
    Long id;

    @Column(name = "rating")
    @NotNull
    String rating;

    @Column(name = "description")
    String description;
}
