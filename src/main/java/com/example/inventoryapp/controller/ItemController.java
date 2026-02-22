package com.example.inventoryapp.controller;

import com.example.inventoryapp.model.Item;
import com.example.inventoryapp.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ItemController {

    private final ItemService service;

    public ItemController(ItemService service) {
        this.service = service;
    }

    // Nice default route
    @GetMapping("/")
    public String home() {
        return "redirect:/items";
    }

    // READ (all)
    @GetMapping("/items")
    public String listItems(Model model) {
        model.addAttribute("items", service.findAll());
        return "items/list";
    }

    // CREATE (form)
    @GetMapping("/items/new")
    public String newItemForm(Model model) {
        model.addAttribute("item", new Item());
        model.addAttribute("mode", "create");
        return "items/form";
    }

    // CREATE (submit)
    @PostMapping("/items")
    public String createItem(@ModelAttribute Item item, RedirectAttributes ra) {
        if (item.getSku() != null && service.skuExists(item.getSku())) {
            ra.addFlashAttribute("error", "SKU already exists. Please use a unique SKU.");
            return "redirect:/items/new";
        }
        service.save(item);
        ra.addFlashAttribute("success", "Item added.");
        return "redirect:/items";
    }

    // UPDATE (form)
    @GetMapping("/items/{id}/edit")
    public String editItemForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Item item = service.findById(id);
        if (item == null) {
            ra.addFlashAttribute("error", "Item not found.");
            return "redirect:/items";
        }
        model.addAttribute("item", item);
        model.addAttribute("mode", "edit");
        return "items/form";
    }

    // UPDATE (submit)
    @PostMapping("/items/{id}")
    public String updateItem(@PathVariable Long id, @ModelAttribute Item item, RedirectAttributes ra) {
        Item existing = service.findById(id);
        if (existing == null) {
            ra.addFlashAttribute("error", "Item not found.");
            return "redirect:/items";
        }

        // preserve ID and update
        item.setId(id);
        service.save(item);
        ra.addFlashAttribute("success", "Item updated.");
        return "redirect:/items";
    }

    // DELETE
    @PostMapping("/items/{id}/delete")
    public String deleteItem(@PathVariable Long id, RedirectAttributes ra) {
        service.deleteById(id);
        ra.addFlashAttribute("success", "Item deleted.");
        return "redirect:/items";
    }
}