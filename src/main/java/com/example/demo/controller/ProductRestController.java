package com.example.demo.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Product;
import com.example.demo.services.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

	private final ProductService productService;

	public ProductRestController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping
	public List<Product> allProducts() {
		return productService.getAllProducts();
	}
}