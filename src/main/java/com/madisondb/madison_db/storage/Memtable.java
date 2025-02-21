package com.madisondb.madison_db.storage;

import java.util.NavigableMap;
import java.util.TreeMap;

import org.springframework.stereotype.Component;

/**
 * A memtable in-memory key-value store using a TreeMap.
 **/
@Component
public class Memtable {
    private static final Integer SIZE_LIMIT = 100;
    private final NavigableMap<String, String> map = new TreeMap<>();

    public void put(String key, String value) {
        map.put(key, value);
    }

    public String get(String key) {
        return map.get(key);
    }

    public void delete(String key) {
        map.remove(key);
    }

    public boolean isFull() {
        return map.size() >= SIZE_LIMIT;
    }

    public NavigableMap<String, String> getData() {
        return map;
    }

    public void clear() {
        map.clear();
    }
}