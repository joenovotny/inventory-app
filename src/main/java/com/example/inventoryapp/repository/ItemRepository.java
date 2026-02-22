package com.example.inventoryapp.repository;

import com.example.inventoryapp.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
    boolean existsBySku(String sku);
}