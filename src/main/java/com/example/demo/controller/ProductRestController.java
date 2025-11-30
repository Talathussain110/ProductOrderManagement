package com.example.demo.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Product;

@RestController
public class ProductRestController {

	@GetMapping("/api/products")
	public List<Product> allProducts() {
		return Collections.emptyList();
	}
}