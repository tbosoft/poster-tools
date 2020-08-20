package com.github.tbosoft.contracts;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.util.HashMap;

public interface Jsonable {
    String toJson() throws IOException;


    static Object decode(String json) throws IOException {
        return  JSON.parse(json);
    }


    static String encode(Object object) throws IOException {
        return JSON.toJSONString(object);
    }

}
