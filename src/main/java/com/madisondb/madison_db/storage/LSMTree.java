package com.madisondb.madison_db.storage;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class LSMTree {
    private final Memtable memtable;
    private final List<SSTable> sstables;
    private final WriteAheadLog wal;

    public LSMTree(Memtable memtable, WriteAheadLog wal, SSTable sstable) {
        this.memtable = memtable;
        this.wal = wal;
        this.sstables = new ArrayList<>();
        this.sstables.add(sstable);
    }

    public void put(String key, String value) {
        wal.log(key, value);
        memtable.put(key, value);

        // if memtable is full, flush the database entries to SStable.
        if (memtable.isFull()) {
            flushToSSTable();
        }
    }

    public String get(String key) {
        // First check the memory i.e. memtable
        String value = memtable.get(key);
        if (value != null) {
            return value;
        }

        // if not found in memtable, check all the sstables in order.
        for (final SSTable sstable : sstables) {
            value = sstable.read(key);
            if (value != null)
                return value;
        }
        return null;
    }

    public void flushToSSTable() {
        if (memtable.getData().isEmpty()) {
            System.out.println("Flush aborted: Memtable is empty.");
            return;
        }

        SSTable newSSTable = new SSTable();
        newSSTable.write(memtable.getData());

        sstables.add(newSSTable);
        memtable.clear();
    }
}
