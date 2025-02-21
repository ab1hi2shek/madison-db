package com.madisondb.madison_db.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class SSTable {
    private static final String SSTABLE_DIR = "data/sstable/";

    public SSTable() {
        ensureDirectoryExists();
    }

    private void ensureDirectoryExists() {
        File dir = new File(SSTABLE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public void write(final Map<String, String> data) {
        String fileName = SSTABLE_DIR + "sstable_" + System.currentTimeMillis() + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Map.Entry<String, String> entry : data.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
            System.out.println("SSTable written: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String read(String key) {
        File folder = new File(SSTABLE_DIR);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (parts.length == 2 && parts[0].equals(key)) {
                            System.out.println("✅ Key found in SSTable: " + key + " -> " + parts[1]);
                            return parts[1];
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("❌ Key not found in any SSTable: " + key);
        return null;
    }
}
