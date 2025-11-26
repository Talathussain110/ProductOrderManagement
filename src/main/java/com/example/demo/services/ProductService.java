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
		return productRepository.findAll();
	}

	public Product getProductById(long id) {
		return productRepository.findById(id).orElse(null);
	}

	public Product insertNewProduct(Product product) {
		product.setId(null);
		return productRepository.save(product);
	}

	public Product updateProductById(long id, Product replacement) {
		replacement.setId(id);
		return productRepository.save(replacement);
	}

	public void deleteProductById(long id) {
		productRepository.deleteById(id);

	}
}