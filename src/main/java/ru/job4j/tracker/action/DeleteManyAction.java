package ru.job4j.tracker.action;

import ru.job4j.tracker.input.Input;
import ru.job4j.tracker.model.Item;
import ru.job4j.tracker.output.Output;
import ru.job4j.tracker.store.Store;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DeleteManyAction implements UserAction {
    private final Output out;
    private AtomicInteger counter;
    private final String namePrefix;

    public DeleteManyAction(Output out, AtomicInteger counter, String namePrefix) {
        this.out = out;
        this.counter = counter;
        this.namePrefix = namePrefix;
    }

    @Override
    public String name() {
        return "=== Delete many new Items ====";
    }

    @Override
    public boolean execute(Input input, Store tracker) {
        int cnt = Integer.valueOf(input.askStr("Enter count of items to delete: "));
        int startNum = counter.get();

        for (int i = 0; i < cnt; i++, startNum--) {
            List<Item> items = tracker.findByName(namePrefix + startNum);
            if (items.size() > 0) {
                tracker.delete(items.get(0).getId());
                out.println(namePrefix + startNum + " item deleted!");
            }
        }
        counter.set(startNum);
        out.println("Items successfully deleted!");
        return true;

    }
}
