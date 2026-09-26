package com.foodapp.service;

import com.foodapp.model.FoodItem;
import com.foodapp.repository.FoodItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {

    private final FoodItemRepository foodItemRepository;

    @Autowired
    public MenuService(FoodItemRepository foodItemRepository) {
        this.foodItemRepository = foodItemRepository;
    }

    public List<FoodItem> getAvailableItems() {
        return foodItemRepository.findByAvailableTrue();
    }

    public List<FoodItem> getAllItems() {
        return foodItemRepository.findAll();
    }

    public FoodItem getItemById(Long id) {
        return foodItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Food item not found: " + id));
    }

    public FoodItem addItem(FoodItem item) {
        return foodItemRepository.save(item);
    }

    public FoodItem updateItem(Long id, FoodItem updated) {
        FoodItem existing = getItemById(id);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setPrice(updated.getPrice());
        existing.setCategory(updated.getCategory());
        existing.setAvailable(updated.isAvailable());
        return foodItemRepository.save(existing);
    }

    public void deleteItem(Long id) {
        foodItemRepository.deleteById(id);
    }
}
