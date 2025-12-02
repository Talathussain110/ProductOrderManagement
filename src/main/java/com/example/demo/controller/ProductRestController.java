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

import com.example.demo.model.Product;
import com.example.demo.services.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

	@Autowired
	private ProductService productService;

	@GetMapping
	public List<Product> allProducts() {
		return productService.getAllProducts();
	}

	@GetMapping("/{id}")
	public Product product(@PathVariable long id) {
		return productService.getProductById(id);
	}

	@PostMapping("/new")
	public Product newProduct(@RequestBody Product product) {
		return productService.insertNewProduct(product);
	}

	@PutMapping("/{id}")
	public Product updateProduct(@PathVariable long id, @RequestBody Product replacement) {
		return productService.updateProductById(id, replacement);
	}

	@DeleteMapping("/{id}")
	public void deleteProduct(@PathVariable long id) {
		productService.deleteProductById(id);
	}
}