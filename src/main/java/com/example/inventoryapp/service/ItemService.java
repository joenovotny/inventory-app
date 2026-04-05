package com.example.inventoryapp.service;

import com.example.inventoryapp.model.Item;
import com.example.inventoryapp.repository.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private static final Logger logger = LoggerFactory.getLogger(ItemService.class);

    private final ItemRepository repo;

    public ItemService(ItemRepository repo) {
        this.repo = repo;
    }

    public List<Item> findAll() {
        logger.info("Entering findAll()");
        try {
            List<Item> items = repo.findAll();
            logger.info("Exiting findAll() successfully, count={}", items.size());
            return items;
        } catch (Exception e) {
            logger.error("Exception in findAll(): {}", e.getMessage(), e);
            throw e;
        }
    }

    public Item findById(Long id) {
        logger.info("Entering findById() with id={}", id);
        try {
            Item item = repo.findById(id).orElse(null);
            logger.info("Exiting findById() successfully for id={}", id);
            return item;
        } catch (Exception e) {
            logger.error("Exception in findById(): {}", e.getMessage(), e);
            throw e;
        }
    }

    public Item save(Item item) {
        logger.info("Entering save() for item with SKU={}", item.getSku());
        try {
            Item savedItem = repo.save(item);
            logger.info("Exiting save() successfully with id={}", savedItem.getId());
            return savedItem;
        } catch (Exception e) {
            logger.error("Exception in save(): {}", e.getMessage(), e);
            throw e;
        }
    }

    public void deleteById(Long id) {
        logger.info("Entering deleteById() with id={}", id);
        try {
            repo.deleteById(id);
            logger.info("Exiting deleteById() successfully for id={}", id);
        } catch (Exception e) {
            logger.error("Exception in deleteById(): {}", e.getMessage(), e);
            throw e;
        }
    }

    public boolean skuExists(String sku) {
        logger.info("Entering skuExists() with SKU={}", sku);
        try {
            boolean exists = repo.existsBySku(sku);
            logger.info("Exiting skuExists() successfully, exists={}", exists);
            return exists;
        } catch (Exception e) {
            logger.error("Exception in skuExists(): {}", e.getMessage(), e);
            throw e;
        }
    }
}