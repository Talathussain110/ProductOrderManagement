package com.example.demo.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.example.demo.model.Order;
import com.example.demo.model.Product;

@DataJpaTest
class OrderJpaTest {

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void testJpaMapping() {
		Product product = entityManager.persistFlushFind(new Product(null, "Laptop", 1500.00));
		Product product2 = entityManager.persistFlushFind(new Product(null, "Mouse", 25.00));

		Order order = new Order(null, LocalDate.of(2025, 6, 20));
		order.setProducts(new HashSet<>(List.of(product, product2)));

		Order saved = entityManager.persistFlushFind(order);

		assertThat(saved.getOrderDate()).isEqualTo(LocalDate.of(2025, 6, 20));
		assertThat(saved.getProducts()).extracting(Product::getName).contains("Laptop", "Mouse");
		assertThat(saved.getId()).isNotNull();
		assertThat(saved.getId()).isPositive();

		LoggerFactory.getLogger(OrderJpaTest.class).info("Saved Order: {}" + saved);
	}
}