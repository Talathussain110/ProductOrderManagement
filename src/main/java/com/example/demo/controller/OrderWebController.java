package com.example.demo.controller;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Order;
import com.example.demo.model.Product;
import com.example.demo.services.OrderService;
import com.example.demo.services.ProductService;

@Controller
@RequestMapping("/orders")
public class OrderWebController {

	private static final String MESSAGE_ATTRIBUTE = "message";
	private static final String ORDER_ATTRIBUTE = "order";
	private static final String ORDERS_ATTRIBUTE = "orders";
	private static final String ALL_PRODUCTS_ATTRIBUTE = "allProducts";

    private final OrderService orderService;
    private final ProductService productService;

    public OrderWebController(OrderService orderService, ProductService productService) {
        this.orderService = orderService;
        this.productService = productService;
    }

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setDisallowedFields("products");
	}

	@GetMapping
	public String listOrders(Model model) {
		List<Order> allOrders = orderService.getAllOrders();
		model.addAttribute(ORDERS_ATTRIBUTE, allOrders);
		model.addAttribute(MESSAGE_ATTRIBUTE, allOrders.isEmpty() ? "No order" : "");
		return ORDER_ATTRIBUTE;
	}

	@GetMapping("/new")
	public String newOrder(Model model) {
	    model.addAttribute(ORDER_ATTRIBUTE, new Order());
	    model.addAttribute(ALL_PRODUCTS_ATTRIBUTE, productService.getAllProducts());
	    model.addAttribute(MESSAGE_ATTRIBUTE, "");
	    model.addAttribute("pageTitle", "New Order");
	    return "edit_order";
	}
	
	@GetMapping("/edit/{id}")
	public String editOrder(@PathVariable long id, Model model) {
	    Order order = orderService.getOrderById(id);

	    model.addAttribute(ORDER_ATTRIBUTE, order);
	    model.addAttribute(ALL_PRODUCTS_ATTRIBUTE, productService.getAllProducts());
	    model.addAttribute(MESSAGE_ATTRIBUTE, order == null ? "No order found with id: " + id : "");
	    model.addAttribute("pageTitle", order == null ? "Edit Order" : "Edit Order");

	    return "edit_order";
	}

	@PostMapping("/save")
	public String saveOrder(@ModelAttribute Order order,
			@RequestParam(value = "products", required = false) List<Long> productIds) {

		if (productIds != null) {
			Set<Product> selectedProducts = new LinkedHashSet<>();
			for (Long id : productIds) {
				Product product = productService.getProductById(id);
				if (product != null) {
					selectedProducts.add(product);
				}
			}
			order.setProducts(selectedProducts);
		}

		if (order.getId() == null) {
			orderService.insertNewOrder(order);
		} else {
			orderService.updateOrderById(order.getId(), order);
		}

		return "redirect:/orders";
	}

	@GetMapping("/delete/{id}")
	public String deleteOrder(@PathVariable long id, Model model) {
		orderService.deleteOrderById(id);
		model.addAttribute("deletedId", id);
		return "delete_order";
	}
}