package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.model.Order;
import com.example.demo.repository.OrderRepository;

@Service
public class OrderService {

	private final OrderRepository orderRepository;

	public OrderService(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	public Order getOrderById(long id) {
		Optional<Order> order = orderRepository.findById(id);
		return order.orElse(null);
	}

	public Order insertNewOrder(Order order) {
		order.setId(null);
		return orderRepository.save(order);
	}

	public Order updateOrderById(long id, Order updatedOrder) {
		updatedOrder.setId(id);
		return orderRepository.save(updatedOrder);
	}

	public void deleteOrderById(long id) {
		orderRepository.deleteById(id);
	}

	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}
}