package com.example.demo.services;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;

@Service
public class ProductService {

	private final ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public Product getProductById(long id) {
		Optional<Product> product = productRepository.findById(id);
		return product.orElse(null);
	}

	public Product insertNewProduct(Product product) {
		product.setId(null);
		return productRepository.save(product);
	}

	public Product updateProductById(long id, Product updatedProduct) {
		updatedProduct.setId(id);
		return productRepository.save(updatedProduct);
	}

	public void deleteProductById(long id) {
		productRepository.deleteById(id);
	}

	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}
}
