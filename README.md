# MadisonDB - Distributed Key-Value Store with LSM Tree

MadisonDB is a **high-performance, distributed key-value store** built using an **LSM Tree architecture**. It supports **fast writes, efficient compaction, and scalable multi-node replication**.

## **🚀 Features**
✅ **LSM Tree-Based Storage** – Optimized for high-speed writes.  
✅ **Write-Ahead Log (WAL)** – Ensures data durability and crash recovery.  
✅ **Memtable & SSTables** – Uses in-memory storage before flushing to disk.  
✅ **Efficient Reads & Caching** [Upcoming] – Implements Bloom Filters for optimized lookups.  
✅ **Sharding & Replication**  [Upcoming] – Supports horizontal scaling.  
✅ **Spring Boot API** – Exposes RESTful endpoints for database operations.  


---

## **📌 Installation & Setup**

### **1️⃣ Clone the Repository**
```sh
git clone https://github.com/ab1hi2shek/madison-db.git
cd madison-db
```

### **2️⃣ Build and run the Project (Using Maven)**
```sh
mvn clean install
mvn spring-boot:run
```

### **4️⃣ Verify API is Running**
Open your browser or use cURL:
```sh
curl -X GET "http://localhost:8080/actuator/health"
```
✅ **Expected Output:** `{ "status": "UP" }`

---

## **📌 Sample API Endpoints & Usage**

### **✅ Insert Key-Value Pair**
```sh
curl -X POST "http://localhost:8080/store?key=name&value=Alice"
```

### **✅ Retrieve Value for a Key**
```sh
curl -X GET "http://localhost:8080/store/name"
```

### **✅ Delete a Key**
```sh
curl -X DELETE "http://localhost:8080/store/name"
```

---

---

## **📌 Development Phases Overview**
| **Phase** | **Focus** |
|-----------|----------|
| **1** | Data Persistence & Recovery
| **2** | Read Performance Optimization
| **3** | Write Scaling & Sharding
| **4** | Multi-Node Distribution & Clustering
| **5** | Transactions & Conflict Resolution
| **6** | Advanced API Features |
| **7** | Benchmarking & Optimization |

---

## **🟢 Phase 1: Data Persistence & Recovery**  
📌 **Goal:** Ensure durability, crash recovery, and efficient storage.  
✅ **Outcome:** MadisonDB should recover from crashes using WAL replay and manage SSTables efficiently.  

### **🛠️ Tasks**
1️⃣ **Write-Ahead Log (WAL) Recovery on Startup**  
   - On startup, scan `wal.log` and replay **unflushed writes** to Memtable.  
   - **Test:** Kill and restart MadisonDB, verify no data loss.  
   
2️⃣ **SSTable Compaction**  
   - Merge **older SSTables** to reduce storage usage.  
   - Implement **Level Compaction (like RocksDB)** to keep **N levels**.  
   - **Test:** Write 10,000 keys and ensure compaction reduces file count.

3️⃣ **Compression (Snappy, Zstd, LZ4) for SSTables**  
   - Use **Snappy compression** for SSTable storage.  
   - **Benchmark before & after compression.**  

### **📌 Deliverables**
✅ WAL replay on startup  
✅ SSTable compaction logic  
✅ Compressed SSTables  

---

## **🟡 Phase 2: Read Performance Optimization**  
📌 **Goal:** Improve read efficiency by avoiding unnecessary SSTable lookups.  
✅ **Outcome:** MadisonDB should **return results faster** by using Bloom Filters and caching.  

### **🛠️ Tasks**
1️⃣ **Implement Bloom Filters**  
   - Store **Bloom Filters in memory** for each SSTable.  
   - If Bloom Filter says **"NO"**, skip SSTable lookup.  
   - **Benchmark improvement in read speed.**  

2️⃣ **Implement Block Cache**  
   - Cache **frequently accessed SSTable blocks** in memory.  
   - **Use LRU caching** (Least Recently Used eviction).  
   - **Test with repeated queries** and check cache hit rate.  

3️⃣ **Parallel SSTable Reads**  
   - Allow **multi-threaded lookups** in SSTables.  
   - Use **Java Executors** for parallel scanning.  

### **📌 Deliverables**
✅ Bloom Filters to skip unnecessary reads  
✅ Block cache for faster SSTable lookups  
✅ Parallelized reads  

---

## **🟠 Phase 3: Write Optimization & Scaling**  
📌 **Goal:** Scale writes efficiently while keeping data consistent.  
✅ **Outcome:** MadisonDB should handle **higher write loads with batching & sharding**.  

### **🛠️ Tasks**
1️⃣ **Batching Writes Before Flushing to SSTables**  
   - Group writes together **before persisting** to disk.  
   - **Test:** Compare single vs. batch writes.  

2️⃣ **Implement Sharding**  
   - Distribute keys across **multiple storage nodes**.  
   - Use **Consistent Hashing with Virtual Nodes** to balance data.  
   - **Test:** Run MadisonDB with multiple instances.  

3️⃣ **Tunable Consistency (W, R, N Parameters Like DynamoDB)**  
   - Allow users to **set W (write quorum), R (read quorum), and N (replication factor)**.  
   - **Test:** Write with `W=1`, `W=2` and read with different `R` values.  

### **📌 Deliverables**
✅ Write batching before flush  
✅ Sharding using consistent hashing  
✅ Tunable consistency for better availability  

---

## **🟣 Phase 4: Multi-Node Distribution & Clustering**  
📌 **Goal:** Support **replication & automatic node discovery** for fault tolerance.  
✅ **Outcome:** MadisonDB should **run across multiple nodes** for high availability.  

### **📌 Deliverables**
✅ Raft-based leader election  
✅ Partitioning strategy  
✅ Automatic node discovery via Gossip  

---

## **🟤 Phase 5: Transactions & Conflict Resolution**  
📌 **Goal:** Allow **multi-key transactions & conflict resolution**.  
✅ **Outcome:** MadisonDB should support **atomic updates across multiple keys**.  

### **📌 Deliverables**
✅ Optimistic Concurrency Control  
✅ Vector Clocks for multi-versioning  
✅ Simple multi-key transactions  

---

## **🔵 Phase 6: Exposing Advanced API Features**  
📌 **Goal:** Add advanced features like **range queries, pagination, and indexing**.  
✅ **Outcome:** MadisonDB should **support efficient querying beyond simple key-value lookups**.  

### **📌 Deliverables**
✅ Range Queries & Sorted Iterators  
✅ Pagination & Cursor API  
✅ Secondary Indexing Support  

---

## **🟢 Phase 7: Benchmarking & Stress Testing**
📌 **Goal:** Ensure **MadisonDB is production-ready** with real-world performance tests.  
✅ **Outcome:** MadisonDB should handle **millions of keys efficiently**.  

### **📌 Deliverables**
✅ Performance comparison with RocksDB  
✅ Large-scale workload simulation  
✅ CPU & memory profiling reports  




---

## **📌 Contributing**
1️⃣ **Fork** the repository.  
2️⃣ **Create a feature branch** (`git checkout -b feature-branch`).  
3️⃣ **Commit your changes** (`git commit -m "Added feature X"`).  
4️⃣ **Push to GitHub** (`git push origin feature-branch`).  
5️⃣ **Open a Pull Request**.  

---

## **📌 License**
📜 This project is licensed under the **MIT License**.

---

## **📌 Contact**
📧 Email: `abhishekkr031@gmail.com`  
🐙 GitHub: [ab1hi2shek](https://github.com/ab1hi2shek)  
🚀 Happy Coding! 🎯

