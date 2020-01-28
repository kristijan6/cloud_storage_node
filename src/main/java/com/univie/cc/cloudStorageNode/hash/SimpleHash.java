package com.univie.cc.cloudStorageNode.hash;

import java.util.Objects;

public class SimpleHash implements Hash {

    @Override
    public String hash(String key) {
        return String.valueOf(Objects.hash(key));
    }
}
