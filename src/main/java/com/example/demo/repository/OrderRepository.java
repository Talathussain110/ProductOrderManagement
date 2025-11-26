package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.demo.model.Order;

@Repository
public class OrderRepository {

	public List<Order> findAll() {

		return null;
	}

	public Optional<Order> findById(long id) {

		return Optional.empty();
	}

	public Order save(Order order) {

		return null;
	}

	public void deleteById(long id) {

	}
}