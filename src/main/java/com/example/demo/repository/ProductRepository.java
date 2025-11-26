package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.demo.model.Product;

@Repository
public class ProductRepository {

	public List<Product> findAll() {

		return null;
	}

	public Optional<Product> findById(long id) {

		return Optional.empty();
	}

	public Product save(Product product) {

		return null;
	}

	public void deleteById(long id) {

	}
}