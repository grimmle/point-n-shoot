package common;

import java.util.Iterator;
import java.util.LinkedHashMap;

public class LRUCache {

    private int capacity;
    private LinkedHashMap<CoordinatesKey, WorldTile> map;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.map = new LinkedHashMap<>();
    }

    public WorldTile get(CoordinatesKey key) {
    	WorldTile value = this.map.get(key);
        if (value == null) {
//            value = -1;
        } else {
            this.set(key, value);
        }
        return value;
    }

    public void set(CoordinatesKey key, WorldTile tile) {
        if (this.map.containsKey(key)) {
            this.map.remove(key);
        } else if (this.map.size() == this.capacity) {
            Iterator<CoordinatesKey> it = this.map.keySet().iterator();
            it.next();
            it.remove();
        }
        map.put(key, tile);
    }
}