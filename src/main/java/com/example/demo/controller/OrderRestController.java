package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
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

	private final OrderService orderService;

	public OrderRestController(OrderService orderService) {
		this.orderService = orderService;
	}

	@GetMapping
	List<Order> allOrders() {
		return orderService.getAllOrders();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Order> order(@PathVariable long id) {
		Order order = orderService.getOrderById(id);
		if (order != null) {
			return ResponseEntity.ok(order);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/new")
	Order newOrder(@RequestBody Order order) {
		return orderService.insertNewOrder(order);
	}

	@PutMapping("/{id}")
	Order updateOrder(@PathVariable long id, @RequestBody Order replacement) {
		return orderService.updateOrderById(id, replacement);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteOrder(@PathVariable long id) {
		boolean deleted = orderService.deleteOrderById(id);
		return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
	}

}