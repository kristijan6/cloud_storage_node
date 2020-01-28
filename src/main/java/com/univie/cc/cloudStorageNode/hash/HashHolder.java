package com.univie.cc.cloudStorageNode.hash;

public class HashHolder {

    private static String hashToUse = "simpleHash";

    public static Hash simpleHash = new SimpleHash();

    public static Hash getHashingAlgorithm() {
        if (hashToUse.equals("simpleHash")) return simpleHash;

        return null;
    }
}
