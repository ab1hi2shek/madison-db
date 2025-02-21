package com.madisondb.madison_db.api.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.madisondb.madison_db.storage.LSMTree;

@RestController
@RequestMapping("/store")
public class LSMTreeController {
    private final LSMTree lsmTree;

    public LSMTreeController(LSMTree lsmTree) {
        this.lsmTree = lsmTree;
    }

    @PostMapping
    public String put(@RequestParam String key, @RequestParam String value) {
        lsmTree.put(key, value);
        return "Inserted: " + key;
    }

    @GetMapping("/{key}")
    public String get(@PathVariable String key) {
        String value = lsmTree.get(key);
        return (value != null) ? value : "Key not found: " + key + "\n";
    }

    @DeleteMapping("/{key}")
    public String delete(@PathVariable String key) {
        lsmTree.put(key, null); // Logical deletion
        return "Deleted: " + key;
    }
}
