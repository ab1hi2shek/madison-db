package com.madisondb.madison_db.storage;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class LSMTree {
    private final Memtable memtable;
    private final List<SSTable> sstables;
    private final WriteAheadLog wal;
    private final BloomFilter bloomFilter;

    public LSMTree(Memtable memtable, WriteAheadLog wal, SSTable sstable) {
        this.memtable = memtable;
        this.wal = wal;
        this.sstables = new ArrayList<>();
        this.sstables.add(sstable);
        this.bloomFilter = new BloomFilter(10_000, 5);

        replayWAL();
    }

    public void put(String key, String value) {
        wal.log(key, value);
        memtable.put(key, value);
        bloomFilter.add(key);

        // if memtable is full, flush the database entries to SStable.
        if (memtable.isFull()) {
            flushToSSTable();
            wal.clear(); // Clear WAL after flushing to SSTable
        }
    }

    public String get(String key) {

        // Skip SSTables if key is not in Bloom Filter
        if (!bloomFilter.mightContain(key)) {
            System.out.println("❌ Key not in Bloom Filter: " + key);
            return null;
        }
        // First check the memory i.e. memtable
        System.out.println("🔍 Checking Memtable first...");
        String value = memtable.get(key);
        if (value != null) {
            return value;
        }

        // if not found in memtable, check all the sstables in order.
        System.out.println("📂 Checking SSTables...");
        for (final SSTable sstable : sstables) {
            value = sstable.read(key);
            if (value != null)
                return value;
        }
        return null;
    }

    private void flushToSSTable() {
        if (memtable.getData().isEmpty()) {
            System.out.println("Flush aborted: Memtable is empty.");
            return;
        }

        SSTable newSSTable = new SSTable();
        newSSTable.write(memtable.getData());

        sstables.add(newSSTable);
        memtable.clear();
    }

    private void replayWAL() {
        List<String[]> entries = wal.readAll();
        for (String[] entry : entries) {
            memtable.put(entry[0], entry[1]);
        }
        System.out.println("✅ WAL Replay Complete. for file items: " + entries.size());
    }
}
