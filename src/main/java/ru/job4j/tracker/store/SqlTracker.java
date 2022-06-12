package ru.job4j.tracker.store;


import ru.job4j.tracker.model.Item;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class SqlTracker implements Store, AutoCloseable {

    private final String tableName = "items";

    private final String addItemQuery = "insert into " + tableName + "(name, created) values (?,?)";
    private final String replaceItemQuery = "update " + tableName + " set name=?, created=? where id=?";
    private final String deleteItemQuery = "delete from " + tableName + " where id =?";
    private final String findItemByNameQuery = "select * from " + tableName + " where name =?";
    private final String findItemByIdQuery = "select * from " + tableName + " where id =?";
    private final String findAllItemsQuery = "select * from " + tableName;

    private Connection cn;

    public void init() {
        try (InputStream in = SqlTracker.class.getClassLoader().getResourceAsStream("app.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("driver-class-name"));
            cn = DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void close() throws Exception {
        if (cn != null) {
            cn.close();
        }
    }

    @Override
    public Item add(Item item) {

        try (PreparedStatement statement = cn.prepareStatement(addItemQuery,
                Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, item.getName());
            statement.setTimestamp(2, Timestamp.valueOf(item.getCreated()));
            statement.execute();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    item.setId(generatedKeys.getInt(1));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return item;
    }

    @Override
    public boolean replace(int id, Item item) {
        boolean result = false;
        try (PreparedStatement statement = cn.prepareStatement(replaceItemQuery)) {

            statement.setString(1, item.getName());
            statement.setTimestamp(2, Timestamp.valueOf(item.getCreated()));
            statement.setInt(3, id);
            statement.execute();
            result = statement.getUpdateCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public boolean delete(int id) {
        boolean result = false;
        try (PreparedStatement statement = cn.prepareStatement(deleteItemQuery)) {

            statement.setInt(1, id);
            statement.execute();
            result = statement.getUpdateCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public List<Item> findAll() {

        List<Item> items = new ArrayList<>();
        try (PreparedStatement statement = cn.prepareStatement(findAllItemsQuery)) {
            statement.execute();

            try (ResultSet resultSet = statement.getResultSet()) {
                fullFillListFromResultSet(items, resultSet);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;

    }

    @Override
    public List<Item> findByName(String key) {
        List<Item> items = new ArrayList<>();
        try (PreparedStatement statement = cn.prepareStatement(findItemByNameQuery)) {
            statement.setString(1, key);
            statement.execute();

            try (ResultSet resultSet = statement.getResultSet()) {
                fullFillListFromResultSet(items, resultSet);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    @Override
    public Item findById(int id) {
        Item resItem = null;
        try (PreparedStatement statement = cn.prepareStatement(findItemByIdQuery)) {

            statement.setInt(1, id);
            statement.execute();

            try (ResultSet resultSet = statement.getResultSet()) {
                if (resultSet.next()) {
                    resItem = new Item(resultSet.getInt(1),
                            resultSet.getString(2),
                            resultSet.getTimestamp(3).toLocalDateTime());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resItem;
    }

    SqlTracker() {
        init();
    }

    private void fullFillListFromResultSet(List<Item> items, ResultSet resultSet) throws SQLException, IllegalArgumentException {
        if (Objects.isNull(resultSet) || Objects.isNull(items)) {
            throw new IllegalArgumentException();
        }
        while (resultSet.next()) {
            Item item = new Item(resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getTimestamp(3).toLocalDateTime());
            items.add(item);
        }
    }
}
