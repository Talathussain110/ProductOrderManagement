package com.example.demo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.example.demo.model.Order;
import com.example.demo.model.Product;

@DataJpaTest
class OrderRepositoryTest {
	
	@Autowired
	private OrderRepository repository;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void firstLearningTest() {
		Product product = entityManager.persistFlushFind(new Product(null, "Laptop", 1500.00));
		Order order = new Order(null, LocalDate.of(2025, 1, 10));
		order.setProducts(Set.of(product));
		Order savedOrder = repository.save(order);

		List<Order> orders = repository.findAll();
		assertThat(orders).containsExactly(savedOrder);
	}

	@Test
	void secondLearningTest() {
		Product product = entityManager.persistFlushFind(new Product(null, "Phone", 500.00));
		Order order = new Order(null, LocalDate.of(2025, 2, 15));
		order.setProducts(Set.of(product));
		Order savedOrder = entityManager.persistFlushFind(order);

		List<Order> orders = repository.findAll();
		assertThat(orders).containsExactly(savedOrder);
	}

	@Test
	void testCreateOrder() {
		Product product = entityManager.persistFlushFind(new Product(null, "Laptop", 1500.00));
		Order order = new Order(null, LocalDate.parse("2025-01-10"));
		order.setProducts(Set.of(product));
		Order saved = repository.save(order);

		assertThat(saved.getId()).isNotNull();
		assertThat(saved.getOrderDate()).isEqualTo(LocalDate.parse("2025-01-10"));
	}

	@Test
	void testFindById() {
		Product product = entityManager.persistFlushFind(new Product(null, "Laptop", 1500.00));
		Order order = new Order(null, LocalDate.parse("2025-01-10"));
		order.setProducts(Set.of(product));
		Order saved = entityManager.persistFlushFind(order);

		Optional<Order> found = repository.findById(saved.getId());
		assertThat(found).isPresent();
		assertThat(found.get().getOrderDate()).isEqualTo(LocalDate.parse("2025-01-10"));
	}

	@Test
	void testFindAllOrders() {
		Product product1 = entityManager.persistFlushFind(new Product(null, "Laptop", 1500.00));
		Product product2 = entityManager.persistFlushFind(new Product(null, "Phone", 500.00));
		Order order1 = new Order(null, LocalDate.parse("2025-01-10"));
		//order1.setProducts(Set.of(product1));
		order1.setProducts(new HashSet<>(List.of(product1)));
		Order order2 = new Order(null, LocalDate.parse("2025-02-15"));
		//order2.setProducts(Set.of(product2));
		order2.setProducts(new HashSet<>(List.of(product2)));
		entityManager.persistFlushFind(order1);
		entityManager.persistFlushFind(order2);

		List<Order> found = repository.findAll();

		assertThat(found).containsExactlyInAnyOrder(order1, order2);
	}

	@Test
	void testUpdateOrder() {
		Product product = entityManager.persistFlushFind(new Product(null, "Laptop", 1500.00));
		Order order = new Order(null, LocalDate.parse("2025-01-10"));
		//order.setProducts(Set.of(product));
		order.setProducts(new HashSet<>(List.of(product)));
		Order saved = entityManager.persistFlushFind(order);

		saved.setOrderDate(LocalDate.parse("2025-02-15"));
		Order updated = repository.save(saved);
		entityManager.flush();
		entityManager.clear();

		Order reloaded = repository.findById(updated.getId()).orElseThrow();
		assertThat(reloaded.getOrderDate()).isEqualTo(LocalDate.parse("2025-02-15"));
	}

	@Test
	void testDeleteOrder() {
		Product product = entityManager.persistFlushFind(new Product(null, "Laptop", 1500.00));
		Order order = new Order(null, LocalDate.parse("2025-01-10"));
		//order.setProducts(Set.of(product));
		order.setProducts(new HashSet<>(List.of(product, product)));
		Order saved = entityManager.persistFlushFind(order);

		repository.deleteById(saved.getId());
		entityManager.flush();

		boolean exists = repository.existsById(saved.getId());
		assertThat(exists).isFalse();
	}
}