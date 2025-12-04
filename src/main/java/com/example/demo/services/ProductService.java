package com.example.demo.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Product;

@Service
public class ProductService {

	private static final String TEMPORARY_IMPLEMENTATION = "Temporary implementation";

	public List<Product> getAllProducts() {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public Product getProductById(long id) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public Product insertNewProduct(Product product) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public Product updateProductById(long id, Product updatedProduct) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public void deleteProductById(long id) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}
}