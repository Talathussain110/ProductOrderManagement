package com.example.demo.services;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.model.Product;

@Service
public class ProductService {

	private final Map<Long, Product> products = new LinkedHashMap<>();

	public ProductService() {
		products.put(1L, new Product(1L, "Laptop", 1500.00));
		products.put(2L, new Product(2L, "Mouse", 25.50));
	}

	public List<Product> getAllProducts() {
		return new ArrayList<>(products.values());
	}
	
	public Product getProductById(long id) {
		return products.get(id);
	}

	public Product insertNewProduct(Product product) {
		long newId = products.size() + 1L;
		product.setId(newId);
		products.put(newId, product);
		return product;
	}

	public Product updateProductById(long id, Product replacement) {
		replacement.setId(id);
		products.put(id, replacement);
		return replacement;
	}

	public void deleteProductById(long id) {
		products.remove(id);
	}
}