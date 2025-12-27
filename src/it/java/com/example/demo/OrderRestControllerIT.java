package com.example.demo;

import static io.restassured.RestAssured.given;
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
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.example.demo.model.Order;
import com.example.demo.model.Product;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;

import io.restassured.response.Response;

@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = Replace.NONE)
class OrderRestControllerIT {

	@SuppressWarnings("resource")
	@Container
	static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0").withDatabaseName("productorder_management")
			.withUsername("admin").withPassword("pass");

	@DynamicPropertySource
	static void overrideProps(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", mysql::getJdbcUrl);
		registry.add("spring.datasource.username", mysql::getUsername);
		registry.add("spring.datasource.password", mysql::getPassword);
		registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
	}

	@LocalServerPort
	private int port;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ProductRepository productRepository;

	@BeforeEach
	void setup() {
		io.restassured.RestAssured.port = port;
		orderRepository.deleteAll();
		productRepository.deleteAll();
		orderRepository.flush();
		productRepository.flush();
	}

	@Test
	void testNewOrder() {
		Product p1 = productRepository.save(new Product(null, "Laptop", 1500.0));
		Product p2 = productRepository.save(new Product(null, "Mouse", 25.0));

		Order order = new Order(null, LocalDate.of(2025, 1, 10));
		// order.setProducts(Set.of(p1, p2));
		order.setProducts(new HashSet<>(List.of(p1, p2)));

		Response response = given().contentType(MediaType.APPLICATION_JSON_VALUE).body(order).when()
				.post("/api/orders/new");

		Order saved = response.getBody().as(Order.class);

		assertThat(saved.getId()).isNotNull();
		assertThat(orderRepository.findById(saved.getId())).isPresent();

	}

	@Test
	void testGetOrderById() {
		Product p1 = productRepository.save(new Product(null, "Phone", 500.0));

		Order saved = new Order(null, LocalDate.of(2025, 2, 15));
		// saved.setProducts(Set.of(p1));
		saved.setProducts(new HashSet<>(List.of(p1)));
		saved = orderRepository.save(saved);

		Order fetched = given().when().get("/api/orders/" + saved.getId()).then().statusCode(200).extract()
				.as(Order.class);

		assertThat(fetched.getId()).isEqualTo(saved.getId());
		assertThat(fetched.getOrderDate()).isEqualTo(LocalDate.of(2025, 2, 15));
	}

	@Test
	void testUpdateOrder() {
		Product p1 = productRepository.save(new Product(null, "Keyboard", 80.0));
		Product p2 = productRepository.save(new Product(null, "Monitor", 200.0));

		Order saved = new Order(null, LocalDate.of(2025, 3, 1));
		// saved.setProducts(Set.of(p1));
		saved.setProducts(new HashSet<>(List.of(p1)));
		saved = orderRepository.save(saved);

		Order updated = new Order(null, LocalDate.of(2025, 3, 2));
		// updated.setProducts(Set.of(p1, p2));
		updated.setProducts(new HashSet<>(List.of(p1, p2)));

		Order result = given().contentType(MediaType.APPLICATION_JSON_VALUE).body(updated).when()
				.put("/api/orders/" + saved.getId()).then().statusCode(200).extract().as(Order.class);

		assertThat(result.getOrderDate()).isEqualTo(LocalDate.of(2025, 3, 2));
	}

	@Test
	void testDeleteOrder() {
		Product p1 = productRepository.save(new Product(null, "To Delete Product", 1.0));

		Order saved = new Order(null, LocalDate.of(2025, 4, 1));
		// saved.setProducts(Set.of(p1));
		saved.setProducts(new HashSet<>(List.of(p1)));
		saved = orderRepository.save(saved);

		given().when().delete("/api/orders/" + saved.getId()).then().statusCode(200);

		assertThat(orderRepository.findById(saved.getId())).isEmpty();
	}
}