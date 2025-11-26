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

		return null;
	}

	public Order getOrderById(long id) {

		return null;
	}

	public Order insertNewOrder(Order order) {

		return null;
	}

	public Order updateOrderById(long id, Order updatedOrder) {

		return null;
	}

	public void deleteOrderById(long id) {

	}
}