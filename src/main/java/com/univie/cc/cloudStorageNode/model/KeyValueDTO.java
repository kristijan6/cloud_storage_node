package com.univie.cc.cloudStorageNode.model;

import com.univie.cc.cloudStorageNode.hash.Hash;

public class KeyValueDTO {
    private String key;
    private String value;

    public KeyValueDTO(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String getHash(Hash hashingAlgorithm) {
        return hashingAlgorithm.hash(key);
    }
}
