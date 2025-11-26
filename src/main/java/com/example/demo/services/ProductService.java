package com.example.demo.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;

@Service
public class ProductService {

	private final ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public List<Product> getAllProducts() {

		return null;
	}

	public Product getProductById(long id) {

		return null;
	}

	public Product insertNewProduct(Product product) {

		return null;
	}

	public Product updateProductById(long id, Product updatedProduct) {

		return null;
	}

	public void deleteProductById(long id) {

	}
}