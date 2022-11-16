package ru.job4j.tracker.action;

import ru.job4j.tracker.input.Input;
import ru.job4j.tracker.model.Item;
import ru.job4j.tracker.output.Output;
import ru.job4j.tracker.store.Store;

import java.util.concurrent.atomic.AtomicInteger;

public class CreateManyAction implements UserAction {
    private final Output out;
    private AtomicInteger counter;
    private final String namePrefix;

    public CreateManyAction(Output out, AtomicInteger counter, String namePrefix) {
        this.out = out;
        this.counter = counter;
        this.namePrefix = namePrefix;
    }

    @Override
    public String name() {
        return "=== Create many new Items ====";
    }

    @Override
    public boolean execute(Input input, Store tracker) {
        int cnt = Integer.valueOf(input.askStr("Enter count of items to create: "));
        int startNum = counter.get();

        for (int i = 0; i < cnt; i++, startNum++) {
            tracker.add(new Item(namePrefix + startNum));
            out.println(namePrefix + startNum + " item created!");
        }

        counter.set(startNum);
        out.println("Items successfully added!");
        return true;
    }
}
