package ru.job4j.tracker.store;


import ru.job4j.tracker.model.Item;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class SqlTracker implements Store, AutoCloseable {

    private static final String ADD_ITEM_QUERY = "insert into items(name, created) values (?,?)";
    private static final String REPLACE_ITEM_QUERY = "update items set name=?, created=? where id=?";
    private static final String DELETE_ITEM_QUERY = "delete from items where id =?";
    private static final String FIND_ITEM_BY_NAME_QUERY = "select * from items where name =?";
    private static final String FIND_ITEM_BY_ID_QUERY = "select * from items where id =?";
    private static final String FIND_ALL_ITEMS_QUERY = "select * from items";

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

        try (PreparedStatement statement = cn.prepareStatement(ADD_ITEM_QUERY,
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
        try (PreparedStatement statement = cn.prepareStatement(REPLACE_ITEM_QUERY)) {
            statement.setString(1, item.getName());
            statement.setTimestamp(2, Timestamp.valueOf(item.getCreated()));
            statement.setInt(3, id);
            result = statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public boolean delete(int id) {
        boolean result = false;
        try (PreparedStatement statement = cn.prepareStatement(DELETE_ITEM_QUERY)) {
            statement.setInt(1, id);
            result = statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public List<Item> findAll() {

        List<Item> items = new ArrayList<>();
        try (PreparedStatement statement = cn.prepareStatement(FIND_ALL_ITEMS_QUERY)) {
            try (ResultSet resultSet = statement.executeQuery()) {
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
        try (PreparedStatement statement = cn.prepareStatement(FIND_ITEM_BY_NAME_QUERY)) {
            statement.setString(1, key);
            try (ResultSet resultSet = statement.executeQuery()) {
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
        try (PreparedStatement statement = cn.prepareStatement(FIND_ITEM_BY_ID_QUERY)) {

            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    resItem = getItem(resultSet);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resItem;
    }

    public SqlTracker() {
        init();
    }

    public SqlTracker(Connection cn) {
        this.cn = cn;
    }

    private void fullFillListFromResultSet(List<Item> items, ResultSet resultSet) throws SQLException, IllegalArgumentException {
        if (Objects.isNull(resultSet) || Objects.isNull(items)) {
            throw new IllegalArgumentException();
        }
        while (resultSet.next()) {
            Item item = getItem(resultSet);
            items.add(item);
        }
    }

    private Item getItem(ResultSet resultSet) throws SQLException {
        return new Item(resultSet.getInt(1),
                resultSet.getString(2),
                resultSet.getTimestamp(3).toLocalDateTime());
    }
}
