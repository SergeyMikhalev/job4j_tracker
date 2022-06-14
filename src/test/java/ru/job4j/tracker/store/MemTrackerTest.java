package ru.job4j.tracker.store;

import org.junit.Test;
import ru.job4j.tracker.model.Item;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class MemTrackerTest {

    @Test
    public void whenAddNewItemThenTrackerHasSameItem() {
        Store memTracker = new MemTracker();
        Item item = memTracker.add(new Item("test1"));
        Item result = memTracker.findById(item.getId());
        assertThat(result.getName(), is(item.getName()));
    }

    @Test
    public void whenFindAll() {
        Store memTracker = new MemTracker();
        Item item1 = memTracker.add(new Item("first"));
        Item item2 = memTracker.add(new Item("second"));
        List<Item> expected = List.of(item1, item2);
        List<Item> result = memTracker.findAll();
        assertThat(result, is(expected));
    }

    @Test
    public void whenFindByName() {
        Store memTracker = new MemTracker();
        Item item1 = memTracker.add(new Item("first"));
        Item item2 = memTracker.add(new Item("second"));
        Item item3 = memTracker.add(new Item("first"));
        List<Item> expected = List.of(item1, item3);
        List<Item> result = memTracker.findByName("first");
        assertThat(result, is(expected));
    }

    @Test
    public void whenFindById() {
        Store memTracker = new MemTracker();
        Item item1 = memTracker.add(new Item("first"));
        Item item2 = memTracker.add(new Item("second"));
        Item item3 = memTracker.add(new Item("first"));
        Item result = memTracker.findById(item2.getId());
        assertThat(result, is(item2));
    }

    @Test
    public void whenFindByIdNotFound() {
        Store memTracker = new MemTracker();
        Item item1 = memTracker.add(new Item("first"));
        Item item2 = memTracker.add(new Item("second"));
        Item item3 = memTracker.add(new Item("first"));
        Item result = memTracker.findById(-1);
        assertThat(result, is(nullValue()));
    }

    @Test
    public void whenReplace() {
        Store memTracker = new MemTracker();
        Item item1 = memTracker.add(new Item("first"));
        memTracker.replace(item1.getId(), new Item("second"));
        assertThat(memTracker.findById(item1.getId()).getName(), is("second"));
    }

    @Test
    public void whenDelete() {
        Store memTracker = new MemTracker();
        Item item1 = memTracker.add(new Item("first"));
        memTracker.delete(item1.getId());
        assertThat(memTracker.findById(item1.getId()), is(nullValue()));
    }

}
