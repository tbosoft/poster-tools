package com.github.tbosoft.kernal;

import com.github.tbosoft.contracts.Jsonable;

import java.io.IOException;

public abstract class JsonAble implements Jsonable {

    @Override
    public String toString() {
        String str = this.toJson();
        return str != null ? str : super.toString();
    }

    @Override
    public String toJson() {
        try {
            return Jsonable.encode(this);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
