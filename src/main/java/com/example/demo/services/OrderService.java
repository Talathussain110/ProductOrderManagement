package com.example.demo.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Order;
import com.example.demo.repository.OrderRepository;

@Service
public class OrderService {

	private final OrderRepository orderRepository;

	public OrderService(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}

	public Order getOrderById(long id) {
		return orderRepository.findById(id).orElse(null);
	}

	public Order insertNewOrder(Order order) {
		order.setId(null);
		return orderRepository.save(order);
	}

	public Order updateOrderById(long id, Order replacement) {
		replacement.setId(id);
		return orderRepository.save(replacement);
	}

	public void deleteOrderById(long id) {
		orderRepository.deleteById(id);

	}
}