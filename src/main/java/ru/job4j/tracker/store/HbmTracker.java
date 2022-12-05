package ru.job4j.tracker.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.tracker.model.Item;

import java.util.List;

public class HbmTracker implements Store, AutoCloseable {

    public static final String UPDATE_ITEM_QUERY = "UPDATE Item as i set i.name = :fName, i.created = :fCreated where i.id = :fId";
    public static final String DELETE_ITEM_QUERY = "DELETE Item  where id = :fId";
    public static final String ALL_ITEMS_QUERY = "from Item ";
    public static final String FIND_ITEMS_BY_NAME_QUERY = "from Item i where i.name = :fName";
    public static final String FIND_ITEM_BY_ID_QUERY = "from Item i where i.id = :fId";

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    @Override
    public Item add(Item item) {
        Session session = sf.openSession();
        try (session) {
            session.beginTransaction();
            session.save(item);
            session.getTransaction().commit();
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        }
        return item;
    }

    @Override
    public boolean replace(int id, Item item) {
        int affected;
        Session session = sf.openSession();
        try (session) {
            session.beginTransaction();
            affected = session
                    .createQuery(UPDATE_ITEM_QUERY)
                    .setParameter("fId", id)
                    .setParameter("fName", item.getName())
                    .setParameter("fCreated", item.getCreated())
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        }
        return (affected > 0);
    }

    @Override
    public boolean delete(int id) {
        int affected;
        Session session = sf.openSession();
        try (session) {
            session.beginTransaction();
            affected = session
                    .createQuery(DELETE_ITEM_QUERY)
                    .setParameter("fId", id)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        }
        return (affected > 0);
    }

    @Override
    public List<Item> findAll() {
        List<Item> result;
        Session session = sf.openSession();
        try (session) {
            session.beginTransaction();
            result = session.createQuery(ALL_ITEMS_QUERY, Item.class).list();
            session.getTransaction().commit();
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        }
        return result;
    }

    @Override
    public List<Item> findByName(String key) {
        List<Item> result;
        Session session = sf.openSession();
        try (session) {
            session.beginTransaction();
            result = session.createQuery(FIND_ITEMS_BY_NAME_QUERY, Item.class)
                    .setParameter("fName", key)
                    .list();
            session.getTransaction().commit();
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        }
        return result;
    }

    @Override
    public Item findById(int id) {
        Item result;
        Session session = sf.openSession();
        try (session) {
            session.beginTransaction();
            result = session.createQuery(FIND_ITEM_BY_ID_QUERY, Item.class)
                    .setParameter("fId", id)
                    .uniqueResult();
            session.getTransaction().commit();
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        }
        return result;
    }

    @Override
    public void close() {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}
