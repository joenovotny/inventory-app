package com.example.inventoryapp.service;

import com.example.inventoryapp.model.Item;
import com.example.inventoryapp.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository repo;

    public ItemService(ItemRepository repo) {
        this.repo = repo;
    }

    public List<Item> findAll() {
        return repo.findAll();
    }

    public Item findById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public Item save(Item item) {
        return repo.save(item);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    public boolean skuExists(String sku) {
        return repo.existsBySku(sku);
    }
}