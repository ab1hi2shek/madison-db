package com.madisondb.madison_db.storage;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.util.BitSet;
import java.util.Random;

/**
 * A simple Bloom Filter implementation.
 */
public class BloomFilter {
    private final BitSet bitset;
    private final int size;
    private final int hashCount;

    public BloomFilter(int size, int hashCount) {
        this.size = size;
        this.hashCount = hashCount;
        this.bitset = new BitSet(size);
    }

    public void add(String key) {
        int[] hashes = getHashes(key);
        for (int hash : hashes) {
            bitset.set(hash);
        }
    }

    public boolean mightContain(String key) {
        int[] hashes = getHashes(key);
        for (int hash : hashes) {
            if (!bitset.get(hash)) {
                return false; // ✅ Definitely not present
            }
        }
        return true; // ✅ Might be present (false positives possible)
    }

    private int[] getHashes(String key) {
        int[] hashes = new int[hashCount];

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(key.getBytes(StandardCharsets.UTF_8));

            for (int i = 0; i < hashCount; i++) {
                int hash = ((hashBytes[i] & 0xFF) << 8) | (hashBytes[i + 1] & 0xFF);
                hashes[i] = Math.abs(hash % size);
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }

        return hashes;
    }
}
