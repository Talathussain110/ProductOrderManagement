package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	Product findByName(String name);

	List<Product> findByNameAndPrice(String name, double price);

	List<Product> findByNameOrPrice(String name, double price);

	List<Product> findAllByPriceGreaterThan(double price);
}