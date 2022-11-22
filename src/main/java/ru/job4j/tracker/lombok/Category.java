package ru.job4j.tracker.lombok;

import lombok.*;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Category {
    @Getter
    @EqualsAndHashCode.Include
    @NonNull
    private int id;

    @Getter
    @Setter
    private String name;
}