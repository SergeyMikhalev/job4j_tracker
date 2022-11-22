package ru.job4j.tracker.lombok;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@Builder(builderMethodName = "of")
public class Permission {
    private int id;
    private String name;
    @Singular("addRule")
    private List<String> rules;
}
