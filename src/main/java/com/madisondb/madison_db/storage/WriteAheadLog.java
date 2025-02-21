package com.madisondb.madison_db.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

/**
 * Write-Ahead Log (WAL) - Logs all write operations before saving.
 */
@Component
public class WriteAheadLog {
    private static final String WAL_DIR = "data/wal/";
    private static final String WAL_FILE = WAL_DIR + "madison_wal.log";

    public WriteAheadLog() {
        ensureDirectoryExists();
    }

    private void ensureDirectoryExists() {
        File directory = new File(WAL_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    /**
     * Logs a key-value write operation to the WAL file.
     */
    public synchronized void log(String key, String value) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(WAL_FILE, true))) {
            writer.write(key + "," + value + "\n");
            writer.flush();
        } catch (IOException e) {
            System.err.println("❌ WAL Write Failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Reads all stored WAL entries for recovery.
     */
    public synchronized List<String[]> readAll() {
        List<String[]> entries = new ArrayList<>();
        File walFile = new File(WAL_FILE);

        if (!walFile.exists()) {
            System.out.println("⚠️ WAL file not found, no recovery needed.");
            return entries;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(walFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] entry = line.split(",", 2); // Split into key and value
                if (entry.length == 2) {
                    entries.add(entry);
                }
            }
        } catch (IOException e) {
            System.err.println("❌ WAL Read Failed: " + e.getMessage());
            e.printStackTrace();
        }

        return entries;
    }

    /**
     * Clears WAL after successful persistence to SSTable.
     */
    public synchronized void clear() {
        try {
            new FileWriter(WAL_FILE, false).close(); // Overwrites the file, clearing content
            System.out.println("🧹 WAL Cleared after persistence.");
        } catch (IOException e) {
            System.err.println("❌ Failed to clear WAL: " + e.getMessage());
        }
    }
}
