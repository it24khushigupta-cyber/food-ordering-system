package com.foodapp.service;

import com.foodapp.model.FoodItem;
import com.foodapp.model.Order;
import com.foodapp.model.OrderItem;
import com.foodapp.model.User;
import com.foodapp.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuService menuService;

    @Autowired
    public OrderService(OrderRepository orderRepository, MenuService menuService) {
        this.orderRepository = orderRepository;
        this.menuService = menuService;
    }

    /**
     * Places an order given a cart map of foodItemId -> quantity.
     */
    public Order placeOrder(User user, Map<Long, Integer> cart) {
        if (cart == null || cart.isEmpty()) {
            throw new IllegalArgumentException("Cart cannot be empty");
        }

        Order order = new Order();
        order.setUser(user);

        BigDecimal total = BigDecimal.ZERO;

        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            FoodItem item = menuService.getItemById(entry.getKey());
            int qty = entry.getValue();
            if (qty <= 0) continue;

            BigDecimal lineTotal = item.getPrice().multiply(BigDecimal.valueOf(qty));
            total = total.add(lineTotal);

            OrderItem orderItem = new OrderItem(item, qty, item.getPrice());
            order.addItem(orderItem);
        }

        order.setTotalAmount(total);
        order.setStatus(Order.Status.PLACED);

        return orderRepository.save(order);
    }

    public List<Order> getOrdersForUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));
    }

    public Order updateStatus(Long id, Order.Status status) {
        Order order = getOrderById(id);
        order.setStatus(status);
        return orderRepository.save(order);
    }
}
