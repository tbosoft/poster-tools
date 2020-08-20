package com.github.tbosoft.kernal;

import com.github.tbosoft.contracts.Data;
import com.github.tbosoft.contracts.Result;

import java.util.HashMap;
import java.util.Map;

public class MapData extends JsonAble implements Data {

    private Map<String, Result> map = new HashMap<>();

    @Override
    public boolean save(String key, Result result) {
        map.put(key, result);
        return true;
    }

    @Override
    public Result find(String key) {
        return map.get(key);
    }

    @Override
    public boolean delete(String key) {
        if (has(key)) {
            map.remove(key);
            return true;
        }

        return false;
    }

    @Override
    public Map<String, Result> list() {
        return map;
    }

    @Override
    public boolean has(String key) {
        return map.get(key) != null;
    }
}
