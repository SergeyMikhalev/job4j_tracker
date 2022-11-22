package ru.job4j.tracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Objects;

@Data
@AllArgsConstructor
public class Item {

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
