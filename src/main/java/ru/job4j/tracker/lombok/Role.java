package ru.job4j.tracker.lombok;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.ToString;

import java.util.List;

@Builder(builderMethodName = "of")
@ToString
@Getter
public class Role {
    private int id;
    private String name;

    @Singular("accessBy")
    private List<String> accessBy;

}