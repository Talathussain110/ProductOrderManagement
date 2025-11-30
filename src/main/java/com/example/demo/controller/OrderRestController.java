package com.example.demo.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Order;

@RestController
public class OrderRestController {

	@GetMapping("/api/orders")
	public List<Order> allOrders() {
		return Collections.emptyList();
	}
}