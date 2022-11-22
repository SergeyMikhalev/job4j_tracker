package ru.job4j.tracker.lombok;

public class LombokUsage {
    public static void main(String[] args) {
        var bird = new BirdData();
        bird.setAge(1);
        System.out.println(bird);

        var category = new Category(1);
        System.out.println(category);

        var role = Role.of()
                .id(1)
                .name("ADMIN")
                .accessBy("create")
                .accessBy("update")
                .accessBy("read")
                .accessBy("delete")
                .build();
        System.out.println(role);

        var permission = Permission.of()
                .id(34).name("TOTAL")
                .addRule("rule1")
                .addRule("rule34")
                .build();
        System.out.println(permission);
    }
}
