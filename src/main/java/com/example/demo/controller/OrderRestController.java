package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Order;
import com.example.demo.services.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

	@Autowired
	private OrderService orderService;

	@GetMapping
	public List<Order> allOrders() {
		return orderService.getAllOrders();
	}

	@GetMapping("/{id}")
	public Order order(@PathVariable long id) {
		return orderService.getOrderById(id);
	}

	@PostMapping("/new")
	public Order newOrder(@RequestBody Order order) {
		return orderService.insertNewOrder(order);
	}

	@PutMapping("/{id}")
	public Order updateOrder(@PathVariable long id, @RequestBody Order replacement) {
		return orderService.updateOrderById(id, replacement);
	}

	@DeleteMapping("/{id}")
	public void deleteOrder(@PathVariable long id) {
		orderService.deleteOrderById(id);
	}
}