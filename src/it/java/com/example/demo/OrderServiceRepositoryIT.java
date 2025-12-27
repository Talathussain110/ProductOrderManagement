package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.example.demo.model.Order;
import com.example.demo.model.Product;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.services.OrderService;

@Testcontainers
@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class OrderServiceRepositoryIT {

	@SuppressWarnings("resource")
	@Container
	static final MySQLContainer<?> MYSQL_CONTAINER = new MySQLContainer<>("mysql:8.0")
			.withDatabaseName("productorder_management").withUsername("admin").withPassword("pass");

	@DynamicPropertySource
	static void overrideProps(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", MYSQL_CONTAINER::getJdbcUrl);
		registry.add("spring.datasource.username", MYSQL_CONTAINER::getUsername);
		registry.add("spring.datasource.password", MYSQL_CONTAINER::getPassword);
		registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
		registry.add("spring.jpa.show-sql", () -> "true");
	}

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ProductRepository productRepository;

	@BeforeEach
	void setup() {
		orderRepository.deleteAll();
		productRepository.deleteAll();

		orderRepository.flush();
		productRepository.flush();
	}

	@Test
	void testInsertNewOrder() {
		Product product1 = productRepository.save(new Product(null, "Laptop", 1200.0));
		Product product2 = productRepository.save(new Product(null, "Mouse", 25.0));
		productRepository.flush();

		Order order = new Order(null, LocalDate.of(2025, 9, 5));
		order.setProducts(new HashSet<>(List.of(product1, product2)));

		Order savedOrder = orderService.insertNewOrder(order);

		assertThat(savedOrder.getId()).isNotNull();
		assertThat(savedOrder.getProducts()).hasSize(2);
		assertThat(savedOrder.getProducts()).extracting(Product::getName).containsExactlyInAnyOrder("Laptop", "Mouse");
	}

	@Test
	void testGetAllOrders() {
		Product product1 = productRepository.save(new Product(null, "Laptop", 1200.0));
		Product product2 = productRepository.save(new Product(null, "Mouse", 25.0));
		productRepository.flush();

		Order order1 = new Order(null, LocalDate.of(2025, 9, 5));
		order1.setProducts(new HashSet<>(List.of(product1)));

		Order order2 = new Order(null, LocalDate.of(2025, 9, 6));
		order2.setProducts(new HashSet<>(List.of(product2)));

		orderService.insertNewOrder(order1);
		orderService.insertNewOrder(order2);

		List<Order> allOrders = orderService.getAllOrders();

		assertThat(allOrders).hasSize(2).extracting(Order::getOrderDate)
				.containsExactlyInAnyOrder(LocalDate.of(2025, 9, 5), LocalDate.of(2025, 9, 6));
	}

	@Test
	void testUpdateOrderById() {
		Product product = productRepository.save(new Product(null, "Laptop", 1200.0));
		productRepository.flush();

		Order order = new Order(null, LocalDate.of(2025, 9, 5));
		order.setProducts(new HashSet<>(List.of(product)));

		Order savedOrder = orderService.insertNewOrder(order);
		savedOrder.setOrderDate(LocalDate.of(2025, 9, 6));

		Order updatedOrder = orderService.updateOrderById(savedOrder.getId(), savedOrder);
		Order fromDb = orderRepository.findById(savedOrder.getId()).orElseThrow();

		assertThat(updatedOrder.getOrderDate()).isEqualTo(LocalDate.of(2025, 9, 6));
		assertThat(fromDb.getOrderDate()).isEqualTo(LocalDate.of(2025, 9, 6));
	}

	@Test
	void testDeleteOrderById() {
		Product product = productRepository.save(new Product(null, "Laptop", 1200.0));
		productRepository.flush();

		Order order = new Order(null, LocalDate.of(2025, 9, 5));
		order.setProducts(new HashSet<>(List.of(product)));

		Order savedOrder = orderService.insertNewOrder(order);

		orderService.deleteOrderById(savedOrder.getId());

		assertThat(orderRepository.findById(savedOrder.getId())).isEmpty();
	}
}