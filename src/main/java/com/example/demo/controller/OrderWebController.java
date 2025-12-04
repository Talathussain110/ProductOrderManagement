package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.Order;
import com.example.demo.services.OrderService;

@Controller
@RequestMapping("/orders")
public class OrderWebController {

	@Autowired
	private OrderService orderService;

	private static final String MESSAGE_ATTRIBUTE = "message";
	private static final String ORDER_ATTRIBUTE = "order";
	private static final String ORDERS_ATTRIBUTE = "orders";

	@GetMapping
	public String listOrders(Model model) {
		List<Order> allOrders = orderService.getAllOrders();
		model.addAttribute(ORDERS_ATTRIBUTE, allOrders);
		model.addAttribute(MESSAGE_ATTRIBUTE, allOrders.isEmpty() ? "No order" : "");
		return "order";
	}

	@GetMapping("/edit/{id}")
	public String editOrder(@PathVariable long id, Model model) {
		Order order = orderService.getOrderById(id);
		model.addAttribute(ORDER_ATTRIBUTE, order);
		model.addAttribute(MESSAGE_ATTRIBUTE, order == null ? "No order found with id: " + id : "");
		return "edit_order";
	}

	@GetMapping("/new")
	public String newOrder(Model model) {
		model.addAttribute(ORDER_ATTRIBUTE, new Order());
		model.addAttribute(MESSAGE_ATTRIBUTE, "");
		return "order";
	}

	@PostMapping("/save")
	public String saveOrder(Order order) {
		if (order.getId() == null) {
			orderService.insertNewOrder(order);
		} else {
			orderService.updateOrderById(order.getId(), order);
		}
		return "redirect:/orders";
	}

	@DeleteMapping("/delete/{id}")
	public String deleteOrder(@PathVariable long id) {
		orderService.deleteOrderById(id);
		return "redirect:/orders";
	}
}