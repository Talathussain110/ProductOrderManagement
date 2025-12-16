package com.example.demo.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.demo.model.Order;
import com.example.demo.model.Product;

@Service
public class OrderService {

	private final Map<Long, Order> orders = new LinkedHashMap<>();

	public OrderService() {
		Product p1 = new Product(1L, "Laptop", 1500.00);
		Product p2 = new Product(2L, "Mouse", 25.00);

		Order o1 = new Order();
		o1.setId(1L);
		o1.setOrderDate(LocalDate.of(2025, 1, 10));
		o1.setProducts(Set.of(p1, p2));

		Order o2 = new Order();
		o2.setId(2L);
		o2.setOrderDate(LocalDate.of(2025, 2, 15));
		o2.setProducts(Set.of(p2));

		orders.put(1L, o1);
		orders.put(2L, o2);
	}

	public List<Order> getAllOrders() {
		return new ArrayList<>(orders.values());
	}

	public Order getOrderById(long id) {
		return orders.get(id);
	}

	public Order insertNewOrder(Order order) {
		long newId = orders.size() + 1L;
		order.setId(newId);
		orders.put(newId, order);
		return order;
	}

	public Order updateOrderById(long id, Order replacement) {
		replacement.setId(id);
		orders.put(id, replacement);
		return replacement;
	}

	public void deleteOrderById(long id) {
		orders.remove(id);
	}
}