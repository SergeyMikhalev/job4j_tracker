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
        assertThat(memTracker.findById(item.getId()).getName(), is(item.getName()));
    }

    @Test
    public void whenFindAll() {
        Store memTracker = new MemTracker();
        Item item1 = memTracker.add(new Item("first"));
        Item item2 = memTracker.add(new Item("second"));
        assertThat(memTracker.findAll(), is(List.of(item1, item2)));
    }

    @Test
    public void whenFindByName() {
        Store memTracker = new MemTracker();
        Item item1 = memTracker.add(new Item("first"));
        Item item2 = memTracker.add(new Item("second"));
        Item item3 = memTracker.add(new Item("first"));
        assertThat(memTracker.findByName("first"), is(List.of(item1, item3)));
    }

    @Test
    public void whenFindById() {
        Store memTracker = new MemTracker();
        Item item1 = memTracker.add(new Item("first"));
        Item item2 = memTracker.add(new Item("second"));
        Item item3 = memTracker.add(new Item("first"));
        assertThat(memTracker.findById(item2.getId()), is(item2));
    }

    @Test
    public void whenFindByIdNotFound() {
        Store memTracker = new MemTracker();
        Item item1 = memTracker.add(new Item("first"));
        Item item2 = memTracker.add(new Item("second"));
        Item item3 = memTracker.add(new Item("first"));
        assertThat(memTracker.findById(-1), is(nullValue()));
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
