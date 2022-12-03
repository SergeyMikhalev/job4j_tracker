package ru.job4j.tracker.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.tracker.model.Item;

import java.util.List;

public class HbmTracker implements Store, AutoCloseable {
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    @Override
    public Item add(Item item) {
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                session.save(item);
                session.getTransaction().commit();
            } catch (final Exception e) {
                session.getTransaction().rollback();
                throw e;
            }
        }
        return item;
    }

    @Override
    public boolean replace(int id, Item item) {
        int affected;
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                affected = session
                        .createQuery("UPDATE Item as i set i.name = :fName, i.created = :fCreated where i.id = :fId")
                        .setParameter("fId", id)
                        .setParameter("fName", item.getName())
                        .setParameter("fCreated", item.getCreated())
                        .executeUpdate();
                session.getTransaction().commit();
            } catch (final Exception e) {
                session.getTransaction().rollback();
                throw e;
            }
        }
        return (affected > 0);
    }

    @Override
    public boolean delete(int id) {
        int affected;
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                affected = session
                        .createQuery("DELETE Item  where id = :fId")
                        .setParameter("fId", id)
                        .executeUpdate();
                session.getTransaction().commit();
            } catch (final Exception e) {
                session.getTransaction().rollback();
                throw e;
            }
        }
        return (affected > 0);
    }

    @Override
    public List<Item> findAll() {
        List<Item> result;
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                result = session.createQuery("from Item ", Item.class).list();
                session.getTransaction().commit();
            } catch (final Exception e) {
                session.getTransaction().rollback();
                throw e;
            }
        }
        return result;
    }

    @Override
    public List<Item> findByName(String key) {
        List<Item> result;
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                result = session.createQuery("from Item i where i.name = :fName", Item.class)
                        .setParameter("fName", key)
                        .list();
                session.getTransaction().commit();
            } catch (final Exception e) {
                session.getTransaction().rollback();
                throw e;
            }
        }
        return result;
    }

    @Override
    public Item findById(int id) {
        Item result;
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                result = session.createQuery("from Item i where i.id = :fId", Item.class)
                        .setParameter("fId", id)
                        .uniqueResult();
                session.getTransaction().commit();
            } catch (final Exception e) {
                session.getTransaction().rollback();
                throw e;
            }
        }
        return result;
    }

    @Override
    public void close() {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}
