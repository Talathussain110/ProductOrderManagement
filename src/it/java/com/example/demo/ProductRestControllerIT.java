package com.example.demo;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

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

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;

import io.restassured.response.Response;

@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = Replace.NONE)
class ProductRestControllerIT {

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
		registry.add("spring.jpa.show-sql", () -> "true");
	}

	@Autowired
	private ProductRepository productRepository;

	@LocalServerPort
	private int port;

	@BeforeEach
	void setup() {
		io.restassured.RestAssured.port = port;
		productRepository.deleteAll();
		productRepository.flush();
	}

	@Test
	void testNewProduct() {
		Product product = new Product(null, "Laptop", 1500.0);

		Response response = given().contentType(MediaType.APPLICATION_JSON_VALUE).body(product).when()
				.post("/api/products/new");

		Product saved = response.getBody().as(Product.class);

		assertThat(saved.getId()).isNotNull();
		assertThat(productRepository.findById(saved.getId())).isPresent();
	}

	@Test
	void testGetProductById() {
		Product saved = productRepository.save(new Product(null, "Mouse", 25.0));

		Product fetched = given().when().get("/api/products/" + saved.getId()).then().statusCode(200).extract()
				.as(Product.class);

		assertThat(fetched.getId()).isEqualTo(saved.getId());
		assertThat(fetched.getName()).isEqualTo("Mouse");
		assertThat(fetched.getPrice()).isEqualTo(25.0);
	}

	@Test
	void testUpdateProduct() {
		Product saved = productRepository.save(new Product(null, "Old Name", 10.0));
		Product updated = new Product(null, "New Name", 99.0);

		Product result = given().contentType(MediaType.APPLICATION_JSON_VALUE).body(updated).when()
				.put("/api/products/" + saved.getId()).then().statusCode(200).extract().as(Product.class);

		assertThat(result.getName()).isEqualTo("New Name");
		assertThat(result.getPrice()).isEqualTo(99.0);
	}

	@Test
	void testDeleteProduct() {
		Product saved = productRepository.save(new Product(null, "Delete Me", 1.0));

		given().when().delete("/api/products/" + saved.getId()).then().statusCode(200);

		assertThat(productRepository.findById(saved.getId())).isEmpty();
	}
}