package ru.job4j.tracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name="items")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private LocalDateTime created = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);

    public Item(String name) {
        this.name = name;
    }

    /* При сохранении в базу происходит округление времени,
     поэтому для прохождения тестов округляю время создания */
    public void setCreated(LocalDateTime created) {
        this.created = created.truncatedTo(ChronoUnit.MILLIS);
    }
}
