package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findByOrderDate(LocalDate orderDate);

	List<Order> findByOrderDateBetween(LocalDate startDate, LocalDate endDate);

	List<Order> findByProducts_Id(Long productId);

	List<Order> findByOrderDateBefore(LocalDate date);
}