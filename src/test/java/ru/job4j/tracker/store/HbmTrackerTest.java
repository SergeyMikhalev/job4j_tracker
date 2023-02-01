package ru.job4j.tracker.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import ru.job4j.tracker.model.Item;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class HbmTrackerTest {
    private static final StandardServiceRegistry REGISTRY = new StandardServiceRegistryBuilder().configure().build();
    private static final SessionFactory SF = new MetadataSources(REGISTRY).buildMetadata().buildSessionFactory();

    @After
    public void clearItems() {
        Session session = SF.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.createQuery("delete from Item").executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
    }

    @AfterClass
    public static void destroy() {
        StandardServiceRegistryBuilder.destroy(REGISTRY);
    }

    @Test
    public void whenAddNewItemThenTrackerHasSameItem() {
        var tracker = new HbmTracker();
        Item item = new Item();
        item.setName("test1");
        tracker.add(item);
        Item result = tracker.findById(item.getId());
        assertThat(result.getName(), is(item.getName()));
    }

    @Test
    public void whenFindByIdWhichNotExists() {
        var tracker = new HbmTracker();
        Item item = new Item();
        item.setName("test1");
        tracker.add(item);
        Item items2 = tracker.findById(item.getId() + 1);
        assertThat(items2, is(nullValue()));
    }

    @Test
    public void whenAddAndThenDelete() {
        var tracker = new HbmTracker();
        Item item = new Item();
        item.setName("test1");
        tracker.add(item);
        assertThat(tracker.delete(item.getId()), is(true));
        assertThat(tracker.findById(item.getId()), is(nullValue()));
    }

    @Test
    public void whenDeleteWitchNotExists() {
        var tracker = new HbmTracker();
        Item item = new Item();
        item.setName("test1");
        tracker.add(item);
        Item item2 = new Item();
        item2.setName("test2");
        assertThat(tracker.findById(item2.getId()), is(nullValue()));
        assertThat(tracker.delete(item2.getId()), is(false));
    }

    @Test
    public void whenFindByNameOnEmpty() {
        var tracker = new HbmTracker();
        List<Item> items = tracker.findByName("test1");
        assertThat(items.size(), is(0));
    }

    @Test
    public void whenFindByNameEmptyOnNonEmpty() {
        var tracker = new HbmTracker();
        for (int i = 2; i < 5; i++) {
            Item item = new Item();
            item.setName("test" + i);
            tracker.add(item);
        }
        List<Item> items = tracker.findByName("test1");
        assertThat(items.size(), is(0));
    }

    @Test
    public void whenFindByName2From5() {
        var tracker = new HbmTracker();
        Item item = new Item();
        item.setName("test2");
        tracker.add(item);
        for (int i = 1; i < 5; i++) {
            item = new Item();
            item.setName("test" + i);
            tracker.add(item);
        }
        List<Item> items = tracker.findByName("test2");
        assertThat(items.size(), is(2));
        assertThat(items.get(0).getName(), is("test2"));
        assertThat(items.get(1).getName(), is("test2"));
    }

    @Test
    public void whenFindAllOnEmpty() {
        var tracker = new HbmTracker();
        List<Item> items = tracker.findAll();
        assertThat(items.size(), is(0));
    }

    @Test
    public void whenFindAllOnNonEmpty() {
        var tracker = new HbmTracker();
        Set<String> names = new HashSet<>();
        for (int i = 1; i < 5; i++) {
            Item item = new Item();
            item.setName("test" + i);
            tracker.add(item);
            names.add(item.getName());
        }
        List<Item> items = tracker.findAll();
        assertThat(items.size(), is(4));
        List<String> namesFromDb = items.stream().map(Item::getName).collect(Collectors.toList());
        for (int i = 1; i < 5; i++) {
            assertThat(namesFromDb.containsAll(names), is(true));
        }

    }

    @Test
    public void whenReplaceNotExisting() {
        var tracker = new HbmTracker();
        Item item = new Item();
        item.setName("item1");
        Item item2 = new Item();
        item2.setName("item2");
        tracker.add(item);
        assertThat(tracker.replace(item.getId() + 1, item2), is(false));
        assertThat(tracker.findById(item.getId()).getName(), is("item1"));
    }

    @Test
    public void whenReplaceExisting() {
        var tracker = new HbmTracker();
        Item item = new Item();
        item.setName("item1");
        item = tracker.add(item);
        Item item2 = new Item();
        item2.setName("item2");
        tracker.add(item);
        item2.setId(item.getId());
        assertThat(tracker.replace(item.getId(), item2), is(true));
        assertThat(tracker.findById(item.getId()).getName(), is("item2"));
    }

}
