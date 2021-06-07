package de.idrinth.waraddonclient.model;

import java.util.ArrayList;
import java.util.HashMap;

public class CaseInsensitiveHashMap<T> extends HashMap<String, T> {

    private final ArrayList<String> keys = new ArrayList<>();

    @Override
    public T put(String key, T value) {
        for (String k : keys) {
            if (k.equalsIgnoreCase(key)) {
                return super.put(k, value);
            }
        }
        keys.add(key);
        return super.put(key, value);
    }
    public T get(String key) {
        for (String k : keys) {
            if (k.equalsIgnoreCase(key)) {
                return super.get(k);
            }
        }
        return super.get(key);
    }
    public boolean containsKey(String key) {
        for (String k : keys) {
            if (k.equalsIgnoreCase(key)) {
                return true;
            }
        }
        return false;
    }
}
