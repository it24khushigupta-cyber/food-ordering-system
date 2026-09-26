package com.foodapp.controller;

import com.foodapp.model.FoodItem;
import com.foodapp.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MenuController {

    private final MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    // Web page: show menu
    @GetMapping("/menu")
    public String showMenu(Model model) {
        List<FoodItem> items = menuService.getAvailableItems();
        model.addAttribute("items", items);
        return "menu";
    }

    // Home page redirects to menu
    @GetMapping("/")
    public String home() {
        return "redirect:/menu";
    }

    // REST API: list available items
    @GetMapping("/api/menu")
    @ResponseBody
    public List<FoodItem> getMenuApi() {
        return menuService.getAvailableItems();
    }

    // REST API (admin): add item
    @PostMapping("/api/menu")
    @ResponseBody
    public FoodItem addItem(@RequestBody FoodItem item) {
        return menuService.addItem(item);
    }

    // REST API (admin): update item
    @PutMapping("/api/menu/{id}")
    @ResponseBody
    public FoodItem updateItem(@PathVariable Long id, @RequestBody FoodItem item) {
        return menuService.updateItem(id, item);
    }

    // REST API (admin): delete item
    @DeleteMapping("/api/menu/{id}")
    @ResponseBody
    public void deleteItem(@PathVariable Long id) {
        menuService.deleteItem(id);
    }
}
