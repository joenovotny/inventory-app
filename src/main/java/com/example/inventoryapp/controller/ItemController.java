package com.example.inventoryapp.controller;

import com.example.inventoryapp.model.Item;
import com.example.inventoryapp.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ItemController {

    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

    private final ItemService service;

    public ItemController(ItemService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String home() {
        logger.info("Entering home()");
        logger.info("Exiting home() with redirect to /items");
        return "redirect:/items";
    }

    @GetMapping("/items")
    public String listItems(Model model) {
        logger.info("Entering listItems()");
        try {
            model.addAttribute("items", service.findAll());
            logger.info("Exiting listItems() successfully");
            return "items/list";
        } catch (Exception e) {
            logger.error("Exception in listItems(): {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/items/new")
    public String newItemForm(Model model) {
        logger.info("Entering newItemForm()");
        model.addAttribute("item", new Item());
        model.addAttribute("mode", "create");
        logger.info("Exiting newItemForm() successfully");
        return "items/form";
    }

    @PostMapping("/items")
    public String createItem(@ModelAttribute Item item, RedirectAttributes ra) {
        logger.info("Entering createItem() with SKU={}", item.getSku());
        try {
            if (item.getSku() != null && service.skuExists(item.getSku())) {
                logger.warn("Duplicate SKU detected in createItem(): {}", item.getSku());
                ra.addFlashAttribute("error", "SKU already exists. Please use a unique SKU.");
                return "redirect:/items/new";
            }

            service.save(item);
            ra.addFlashAttribute("success", "Item added.");
            logger.info("Exiting createItem() successfully");
            return "redirect:/items";
        } catch (Exception e) {
            logger.error("Exception in createItem(): {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/items/{id}/edit")
    public String editItemForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        logger.info("Entering editItemForm() with id={}", id);
        try {
            Item item = service.findById(id);
            if (item == null) {
                logger.warn("Item not found in editItemForm() for id={}", id);
                ra.addFlashAttribute("error", "Item not found.");
                return "redirect:/items";
            }

            model.addAttribute("item", item);
            model.addAttribute("mode", "edit");
            logger.info("Exiting editItemForm() successfully for id={}", id);
            return "items/form";
        } catch (Exception e) {
            logger.error("Exception in editItemForm(): {}", e.getMessage(), e);
            throw e;
        }
    }

    @PostMapping("/items/{id}")
    public String updateItem(@PathVariable Long id, @ModelAttribute Item item, RedirectAttributes ra) {
        logger.info("Entering updateItem() with id={}", id);
        try {
            Item existing = service.findById(id);
            if (existing == null) {
                logger.warn("Item not found in updateItem() for id={}", id);
                ra.addFlashAttribute("error", "Item not found.");
                return "redirect:/items";
            }

            item.setId(id);
            service.save(item);
            ra.addFlashAttribute("success", "Item updated.");
            logger.info("Exiting updateItem() successfully for id={}", id);
            return "redirect:/items";
        } catch (Exception e) {
            logger.error("Exception in updateItem(): {}", e.getMessage(), e);
            throw e;
        }
    }

    @PostMapping("/items/{id}/delete")
    public String deleteItem(@PathVariable Long id, RedirectAttributes ra) {
        logger.info("Entering deleteItem() with id={}", id);
        try {
            service.deleteById(id);
            ra.addFlashAttribute("success", "Item deleted.");
            logger.info("Exiting deleteItem() successfully for id={}", id);
            return "redirect:/items";
        } catch (Exception e) {
            logger.error("Exception in deleteItem(): {}", e.getMessage(), e);
            throw e;
        }
    }
}