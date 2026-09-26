package com.foodapp;

import com.foodapp.model.FoodItem;
import com.foodapp.model.Order;
import com.foodapp.model.User;
import com.foodapp.repository.FoodItemRepository;
import com.foodapp.repository.UserRepository;
import com.foodapp.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private FoodItem pizza;
    private FoodItem burger;

    @BeforeEach
    void setUp() {
        testUser = userRepository.findAll().stream().findFirst()
                .orElseGet(() -> userRepository.save(
                        new User("Test User", "test@example.com", "pass", User.Role.CUSTOMER)));

        pizza = foodItemRepository.save(
                new FoodItem("Test Pizza", "Testing pizza", new BigDecimal("200.00"), "Pizza", true));
        burger = foodItemRepository.save(
                new FoodItem("Test Burger", "Testing burger", new BigDecimal("100.00"), "Burgers", true));
    }

    @Test
    void placeOrder_calculatesCorrectTotal() {
        Map<Long, Integer> cart = new HashMap<>();
        cart.put(pizza.getId(), 2);   // 2 x 200 = 400
        cart.put(burger.getId(), 1);  // 1 x 100 = 100

        Order order = orderService.placeOrder(testUser, cart);

        assertEquals(new BigDecimal("500.00"), order.getTotalAmount());
        assertEquals(2, order.getItems().size());
        assertEquals(Order.Status.PLACED, order.getStatus());
    }

    @Test
    void placeOrder_throwsExceptionForEmptyCart() {
        Map<Long, Integer> emptyCart = new HashMap<>();
        assertThrows(IllegalArgumentException.class,
                () -> orderService.placeOrder(testUser, emptyCart));
    }

    @Test
    void placeOrder_ignoresZeroQuantityItems() {
        Map<Long, Integer> cart = new HashMap<>();
        cart.put(pizza.getId(), 0);
        cart.put(burger.getId(), 3); // 3 x 100 = 300

        Order order = orderService.placeOrder(testUser, cart);

        assertEquals(new BigDecimal("300.00"), order.getTotalAmount());
        assertEquals(1, order.getItems().size());
    }

    @Test
    void updateStatus_changesOrderStatus() {
        Map<Long, Integer> cart = new HashMap<>();
        cart.put(pizza.getId(), 1);
        Order order = orderService.placeOrder(testUser, cart);

        Order updated = orderService.updateStatus(order.getId(), Order.Status.DELIVERED);

        assertEquals(Order.Status.DELIVERED, updated.getStatus());
    }
}
