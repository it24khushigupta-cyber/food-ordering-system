package com.foodapp.controller;

import com.foodapp.model.Order;
import com.foodapp.model.User;
import com.foodapp.repository.UserRepository;
import com.foodapp.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final UserRepository userRepository;

    @Autowired
    public OrderController(OrderService orderService, UserRepository userRepository) {
        this.orderService = orderService;
        this.userRepository = userRepository;
    }

    // Web page: order confirmation after placing an order (demo uses first user)
    @PostMapping("/cart/checkout")
    public String checkout(@RequestParam Map<String, String> cartParams, Model model) {
        User user = userRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("No demo user found"));

        Map<Long, Integer> cart = new java.util.HashMap<>();
        cartParams.forEach((k, v) -> {
            if (k.startsWith("qty_")) {
                Long itemId = Long.parseLong(k.substring(4));
                int qty = Integer.parseInt(v);
                if (qty > 0) cart.put(itemId, qty);
            }
        });

        Order order = orderService.placeOrder(user, cart);
        model.addAttribute("order", order);
        return "order-confirmation";
    }

    @GetMapping("/orders")
    public String myOrders(Model model) {
        User user = userRepository.findAll().stream().findFirst().orElse(null);
        List<Order> orders = user != null ? orderService.getOrdersForUser(user.getId()) : List.of();
        model.addAttribute("orders", orders);
        return "orders";
    }

    // REST API: place order
    @PostMapping("/api/orders")
    @ResponseBody
    public Order placeOrderApi(@RequestParam Long userId, @RequestBody Map<Long, Integer> cart) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return orderService.placeOrder(user, cart);
    }

    // REST API: get all orders (admin)
    @GetMapping("/api/orders")
    @ResponseBody
    public List<Order> getAllOrdersApi() {
        return orderService.getAllOrders();
    }

    // REST API: update order status (admin)
    @PutMapping("/api/orders/{id}/status")
    @ResponseBody
    public Order updateStatus(@PathVariable Long id, @RequestParam Order.Status status) {
        return orderService.updateStatus(id, status);
    }
}
