package ru.job4j.tracker.store;

public class StoreFactory {
    public static Store getStore(StoreTypeEnum type) {
        Store result = null;
        switch (type) {
            case STORE_TYPE_MEM -> result = new MemTracker();
            case STORE_TYPE_SQL -> result = new SqlTracker();
        }
        return result;
    }
}
